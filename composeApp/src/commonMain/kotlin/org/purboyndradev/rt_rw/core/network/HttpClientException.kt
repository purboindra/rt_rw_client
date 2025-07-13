package org.purboyndradev.rt_rw.core.network

import io.ktor.client.call.body
import io.ktor.client.network.sockets.SocketTimeoutException
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText
import io.ktor.client.statement.request
import org.purboyndradev.rt_rw.core.domain.Result
import io.ktor.util.network.UnresolvedAddressException
import kotlinx.coroutines.ensureActive
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json
import org.purboyndradev.rt_rw.core.domain.DataError
import kotlin.coroutines.coroutineContext

suspend inline fun <reified T> safeCall(execute: () -> HttpResponse): Result<T, DataError.Remote> {
    val response = try {
        execute()
    } catch (e: SocketTimeoutException) {
        e.printStackTrace()
        return Result.Error(DataError.Remote.REQUEST_TIMEOUT)
    } catch (e: UnresolvedAddressException) {
        e.printStackTrace()
        return Result.Error(DataError.Remote.NO_INTERNET)
    } catch (e: SerializationException) {
        e.printStackTrace()
        return Result.Error(DataError.Remote.SERIALIZATION)
    } catch (e: Exception) {
        e.printStackTrace()
        coroutineContext.ensureActive()
        return Result.Error(DataError.Remote.UNKNOWN)
    }
    
    return responseToResult(response)
}


suspend inline fun <reified T> responseToResult(response: HttpResponse): Result<T, DataError.Remote> {
    
    val bodyText = response.bodyAsText()
    println("Raw Response Body: $bodyText")
    println("Response hit URL: ${response.request.url}")
    
    return when (response.status.value) {
        in 200..299 -> {
            try {
                Result.Success(response.body<T>())
            } catch (e: Exception) {
                Result.Error(DataError.Remote.SERIALIZATION)
            }
        }
        
        400 -> Result.Error(DataError.Remote.REQUEST_TIMEOUT)
        401, 403 -> {
            println("Unauthorized or forbidden access")
            Result.Error(DataError.Remote.UNAUTHORIZED)
        }
        
        429 -> Result.Error(DataError.Remote.TOO_MANY_REQUESTS)
        404 -> Result.Error(DataError.Remote.NOT_FOUND)
        in 500..599 -> Result.Error(DataError.Remote.SERVER)
        else -> Result.Error(DataError.Remote.UNKNOWN)
    }
}