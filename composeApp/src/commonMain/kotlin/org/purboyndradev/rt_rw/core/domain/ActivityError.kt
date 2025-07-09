package org.purboyndradev.rt_rw.core.domain

sealed interface ActivityError : Error {
    object InvalidOtp : ActivityError
    object Network : ActivityError
    object InvalidResponse : ActivityError
    object InvalidJwt : ActivityError
    data class Server(val code: Int, val message: String) : ActivityError
}