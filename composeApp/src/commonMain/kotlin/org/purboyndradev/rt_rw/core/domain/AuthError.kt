package org.purboyndradev.rt_rw.core.domain

sealed interface AuthError : Error {
    object InvalidOtp : AuthError
    object Network : AuthError
    object InvalidResponse : AuthError
    object InvalidJwt : AuthError
    data class Server(val code: Int, val message: String) : AuthError
}
