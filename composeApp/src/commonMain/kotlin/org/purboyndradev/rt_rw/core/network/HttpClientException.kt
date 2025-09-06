package org.purboyndradev.rt_rw.core.network

import io.ktor.client.call.body
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText
import io.ktor.client.statement.request
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.contentOrNull
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import org.purboyndradev.rt_rw.core.domain.AppError
import org.purboyndradev.rt_rw.core.domain.Result

private val errJson = Json { ignoreUnknownKeys = true; isLenient = true }


fun mapHttpError(status: Int, bodyText: String): AppError.Remote {
    val json =
        runCatching { errJson.parseToJsonElement(bodyText).jsonObject }.getOrNull()
    val serverMessage = json?.get("message")?.jsonPrimitive?.contentOrNull
    val msg = serverMessage?.takeIf { it.isNotBlank() } ?: "HTTP $status"
    
    return when (status) {
        401 -> AppError.Remote.Unauthorized
        403 -> AppError.Remote.Http(403, msg, bodyText)
        404 -> AppError.Remote.Http(404, msg, bodyText)
        408 -> AppError.Remote.RequestTimeout
        429 -> AppError.Remote.TooManyRequests
        in 500..599 -> AppError.Remote.Http(status, msg, bodyText)
        400 -> AppError.Remote.Http(400, msg, bodyText)
        else -> AppError.Remote.Unknown(message = msg)
    }
}

suspend inline fun <reified T : Any> safeCallWrapped(
    noinline call: suspend () -> HttpResponse,
    json: Json = Json { ignoreUnknownKeys = true; isLenient = true }
): Result<T, AppError.Remote> {
    val resp = try {
        call()
    } catch (t: Throwable) {
        println("Throwable: $t")
        return Result.Error(AppError.Remote.Network)
    }
    
    val status = resp.status.value
    val text = resp.bodyAsText()
    
    println("Raw Response Body: $text, status_code: $status, url: ${resp.request.url}")
    
    return if (status in 200..299) {
        runCatching {
            val dto = json.decodeFromString<T>(text)
            Result.Success(dto)
        }.getOrElse {
            Result.Error(AppError.Remote.Serialization)
        }
    } else {
        Result.Error(mapHttpError(status, text))
    }
}


suspend inline fun <reified T> responseToResult(response: HttpResponse): Result<T, AppError.Remote> {
    
    val bodyText = response.bodyAsText()
    println("Raw Response Body: $bodyText, status_code: ${response.status.value}")
    println("Response hit URL: ${response.request.url}")
    
    return when (val status = response.status.value) {
        in 200..299 -> try {
            Result.Success(response.body<T>())
        } catch (e: Exception) {
            Result.Error(AppError.Remote.Serialization)
        }
        
        400 -> Result.Error(AppError.Remote.Http(400, "Bad Request"))
        401 -> Result.Error(AppError.Remote.Unauthorized)
        403 -> Result.Error(AppError.Remote.Http(403, "Forbidden"))
        404 -> Result.Error(AppError.Remote.NotFound)
        408 -> Result.Error(AppError.Remote.RequestTimeout)
        429 -> Result.Error(AppError.Remote.TooManyRequests)
        in 500..599 -> Result.Error(
            AppError.Remote.Http(
                status,
                "Server error"
            )
        )
        
        else -> Result.Error(AppError.Remote.Unknown(message = "HTTP $status"))
    }
}