package org.purboyndradev.rt_rw

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform