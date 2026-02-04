package org.purboyndradev.rt_rw.core.network

import androidx.datastore.core.IOException
import io.ktor.client.call.NoTransformationFoundException
import io.ktor.client.call.body
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.plugins.RedirectResponseException
import io.ktor.client.plugins.ResponseException
import io.ktor.client.plugins.ServerResponseException
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText
import io.ktor.client.statement.request
import io.ktor.http.HttpStatusCode
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.contentOrNull
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import org.purboyndradev.rt_rw.core.data.remote.mapper.toRes
import org.purboyndradev.rt_rw.core.domain.AppError
import org.purboyndradev.rt_rw.core.domain.Result
import co.touchlab.kermit.Logger as KermitLogger

@Serializable
data class MessageResponse(val message: String)

private val errJson = Json { ignoreUnknownKeys = true; isLenient = true }

class DataNotFoundException(message: String) : Exception(message)
class ApiException(message: String) : Exception(message)

suspend fun mapKtorExceptionToAppError(e: Exception): AppError.Remote {
    return when (e) {

        is ApiException -> {
            AppError.Remote.Unknown(message = e.message)
        }

        is DataNotFoundException -> {
            AppError.Remote.NotFound
        }

        is ResponseException -> {
            val statusCode = e.response.status.value
            val errorBody = e.response.bodyAsText()
            mapHttpError(statusCode, errorBody)
        }

        is ClientRequestException -> {
            val statusCode = e.response.status.value
            val errorBody = e.response.bodyAsText()
            mapHttpError(statusCode, errorBody)
        }

        is NullPointerException -> {
            AppError.Remote.Serialization
        }

        is ServerResponseException -> {
            val statusCode = e.response.status.value
            val errorBody = e.response.bodyAsText()
            mapHttpError(statusCode, errorBody)
        }

        is kotlinx.serialization.SerializationException -> {
            AppError.Remote.Serialization
        }

        is IOException -> {
            AppError.Remote.Network
        }

        else -> {
            val message = e.message

            val decodeMessage: MessageResponse? =
                errJson.decodeFromString<MessageResponse?>(message ?: "")

            AppError.Remote.Unknown(
                message = decodeMessage?.message ?: AppError.Remote.RequestTimeout.toRes(),
                cause = e.cause
            )
        }
    }
}

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
    noinline call: suspend () -> T
): Result<T, AppError.Remote> {
    return try {
        val resultData = call()
        Result.Success(resultData)
    } catch (e: RedirectResponseException) {
        KermitLogger.e("safeCallWrapped Error - Redirect") { e.message.orEmpty() }
        Result.Error(
            AppError.Remote.Http(
                e.response.status.value,
                "Redirect: ${e.message}",
                e.response.bodyAsText()
            )
        )
    } catch (e: ClientRequestException) {
        val statusCode = e.response.status.value
        val errorBody = e.response.bodyAsText()
        KermitLogger.e("safeCallWrapped Error - Client") { "Status: $statusCode, Message: ${e.message}, Body: $errorBody" }
        if (statusCode == HttpStatusCode.Unauthorized.value) {
            KermitLogger.w("safeCallWrapped") { "Received 401 Unauthorized. This typically means token refresh also failed or was not attempted for this path." }
        }
        Result.Error(mapHttpError(statusCode, errorBody))
    } catch (e: ServerResponseException) {
        KermitLogger.e("safeCallWrapped Error - Server") { "Status: ${e.response.status.value}, Message: ${e.message}, Body: ${e.response}" }
        Result.Error(mapHttpError(e.response.status.value, e.response.bodyAsText()))
    } catch (e: NoTransformationFoundException) {
        KermitLogger.e("safeCallWrapped Error - Serialization (NoTransformation)") { e.message.orEmpty() }
        Result.Error(AppError.Remote.Serialization)
    } catch (e: kotlinx.serialization.SerializationException) {
        KermitLogger.e("safeCallWrapped Error - Serialization (Kotlinx)") { e.message.orEmpty() }
        Result.Error(AppError.Remote.Serialization)
    } catch (e: IOException) {
        KermitLogger.e("safeCallWrapped Error - Network IO") { "${e::class.simpleName}: ${e.message.orEmpty()}" }
        Result.Error(AppError.Remote.Network)
    } catch (t: Throwable) {
        KermitLogger.e("safeCallWrapped Error - Unknown") { "${t::class.simpleName}: ${t.message.orEmpty()}" }
        Result.Error(AppError.Remote.Unknown(t))
    }
}

// Your mapHttpError function
//fun mapHttpError(status: Int, errorBody: String?): AppError.Remote {
//    // You might want to parse the errorBody if it's JSON and contains more details
//    return when (status) {
//        HttpStatusCode.Unauthorized.value -> AppError.Remote.Unauthorized(errorBody)
//        HttpStatusCode.Forbidden.value -> AppError.Remote.Forbidden(errorBody)
//        HttpStatusCode.NotFound.value -> AppError.Remote.NotFound(errorBody)
//        in 400..499 -> AppError.Remote.HttpError(status, "Client error: $status", errorBody)
//        in 500..599 -> AppError.Remote.HttpError(status, "Server error: $status", errorBody)
//        else -> AppError.Remote.Unknown("HTTP error: $status")
//    }
//}


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