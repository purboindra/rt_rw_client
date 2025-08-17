package org.purboyndradev.rt_rw.core.data.remote.mapper

import org.jetbrains.compose.resources.getString
import org.purboyndradev.rt_rw.core.domain.AppError
import rtrwapps.composeapp.generated.resources.Res
import rtrwapps.composeapp.generated.resources.err_http_code
import rtrwapps.composeapp.generated.resources.err_http_code_message
import rtrwapps.composeapp.generated.resources.err_invalid_jwt
import rtrwapps.composeapp.generated.resources.err_invalid_otp
import rtrwapps.composeapp.generated.resources.err_invalid_response
import rtrwapps.composeapp.generated.resources.err_no_internet
import rtrwapps.composeapp.generated.resources.err_not_found
import rtrwapps.composeapp.generated.resources.err_remote_unknown
import rtrwapps.composeapp.generated.resources.err_remote_unknown_with_message
import rtrwapps.composeapp.generated.resources.err_request_timeout
import rtrwapps.composeapp.generated.resources.err_serialization
import rtrwapps.composeapp.generated.resources.err_timeout
import rtrwapps.composeapp.generated.resources.err_too_many_requests
import rtrwapps.composeapp.generated.resources.err_unauthorized

suspend fun AppError.toRes(): String = when (this) {
    is AppError.Remote -> {
        when (this) {
            is AppError.Remote.NoInternet -> getString(Res.string.err_no_internet)
            is AppError.Remote.TooManyRequests -> getString(Res.string.err_too_many_requests)
            is AppError.Remote.Unauthorized -> getString(Res.string.err_unauthorized)
            is AppError.Remote.NotFound -> getString(Res.string.err_not_found)
            is AppError.Remote.RequestTimeout -> getString(Res.string.err_request_timeout)
            is AppError.Remote.Serialization -> getString(Res.string.err_serialization)
            is AppError.Remote.Timeout -> getString(Res.string.err_timeout)
            
            is AppError.Remote.InvalidJwt -> getString(Res.string.err_invalid_jwt)
            is AppError.Remote.InvalidOtp -> getString(Res.string.err_invalid_otp)
            is AppError.Remote.InvalidResponse -> getString(Res.string.err_invalid_response)
            is AppError.Remote.Network -> getString(Res.string.err_no_internet)
            
            is AppError.Remote.Http ->
                if (message.isNullOrBlank())
                    getString(Res.string.err_http_code)
                else
                    getString(
                        Res.string.err_http_code_message,
                    )
            
            is AppError.Remote.Unknown ->
                if (message.isNullOrBlank())
                    getString(Res.string.err_remote_unknown)
                else
                    getString(
                        Res.string.err_remote_unknown_with_message,
                        listOf(message)
                    )
        }
    }
    
    is AppError.Local -> {
        when (this) {
            AppError.Local.DiskFull -> getString(Res.string.err_no_internet)
            AppError.Local.InsufficientFunds -> getString(Res.string.err_too_many_requests)
            is AppError.Local.Unknown ->
                if (cause?.message.isNullOrBlank())
                    getString(Res.string.err_remote_unknown)
                else
                    getString(
                        Res.string.err_remote_unknown_with_message,
                        listOf(cause.message)
                    )
        }
    }
}