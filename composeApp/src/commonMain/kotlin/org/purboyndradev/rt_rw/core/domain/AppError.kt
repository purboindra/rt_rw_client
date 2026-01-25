package org.purboyndradev.rt_rw.core.domain

sealed interface AppError {
    sealed interface Remote : AppError {
        data object NoInternet : Remote
        data object TooManyRequests : Remote
        data object Unauthorized : Remote
        data object NotFound : Remote
        data object RequestTimeout : Remote
        data object Serialization : Remote
        data object Timeout : Remote

        data object InvalidOtp : Remote
        data object Network : Remote
        data object InvalidResponse : Remote
        data object InvalidJwt : Remote

        data class Http(
            val code: Int,
            val message: String? = null,
            val body: String? = null
        ) : Remote

        data class Unknown(
            val cause: Throwable? = null,
            val message: String? = null
        ) : Remote
    }

    sealed interface Local : AppError {
        data object DiskFull : Local
        data object InsufficientFunds : Local
        data class Unknown(val cause: Throwable? = null) : Local
    }
}
