package org.purboyndradev.rt_rw.core.data.remote.mapper

import org.purboyndradev.rt_rw.core.domain.ActivityError
import org.purboyndradev.rt_rw.core.domain.ActivityError.InvalidOtp
import org.purboyndradev.rt_rw.core.domain.ActivityError.InvalidResponse
import org.purboyndradev.rt_rw.core.domain.ActivityError.Network
import org.purboyndradev.rt_rw.core.domain.ActivityError.Server
import org.purboyndradev.rt_rw.core.domain.DataError

fun DataError.Remote.toActivityError(): ActivityError = when (this) {
    DataError.Remote.REQUEST_TIMEOUT -> Network
    DataError.Remote.TOO_MANY_REQUESTS -> Network
    DataError.Remote.NO_INTERNET -> Network
    DataError.Remote.SERVER -> Server(500, "Unknown error")
    DataError.Remote.SERIALIZATION -> InvalidResponse
    DataError.Remote.UNAUTHORIZED -> InvalidOtp
    DataError.Remote.NOT_FOUND -> InvalidResponse
    DataError.Remote.UNKNOWN -> Server(500, "Unknown error")
}
