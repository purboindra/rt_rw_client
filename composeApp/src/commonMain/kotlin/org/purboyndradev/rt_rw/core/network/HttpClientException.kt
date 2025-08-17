package org.purboyndradev.rt_rw.core.network

import io.ktor.client.call.body
import io.ktor.client.network.sockets.SocketTimeoutException
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText
import io.ktor.client.statement.request
import io.ktor.util.network.UnresolvedAddressException
import kotlinx.coroutines.ensureActive
import kotlinx.serialization.SerializationException
import org.purboyndradev.rt_rw.core.domain.AppError
import org.purboyndradev.rt_rw.core.domain.Result
import kotlin.coroutines.coroutineContext

suspend inline fun <reified T> safeCall(execute: () -> HttpResponse): Result<T, AppError.Remote> {
    val response = try {
        execute()
    } catch (e: SocketTimeoutException) {
        e.printStackTrace()
        return Result.Error(AppError.Remote.Timeout)
    } catch (e: UnresolvedAddressException) {
        e.printStackTrace()
        return Result.Error(AppError.Remote.NoInternet)
    } catch (e: SerializationException) {
        e.printStackTrace()
        return Result.Error(AppError.Remote.Serialization)
    } catch (e: Exception) {
        e.printStackTrace()
        coroutineContext.ensureActive()
        return Result.Error(AppError.Remote.Unknown(e, message = e.message))
    }
    return responseToResult(response)
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