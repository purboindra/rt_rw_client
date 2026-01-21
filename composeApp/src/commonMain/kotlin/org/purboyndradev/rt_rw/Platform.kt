package org.purboyndradev.rt_rw

import org.koin.core.module.Module

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform

expect object TelegramLauncher {
    fun open(url: String)
}

expect object ClipboardReader {
    fun getText(): String?
}

expect class PlatformContext

expect fun createPlatformModule(): Module
