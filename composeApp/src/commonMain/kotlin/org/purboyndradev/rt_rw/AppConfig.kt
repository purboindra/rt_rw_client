package org.purboyndradev.rt_rw

import kotlin.concurrent.Volatile

enum class Environment { DEBUG, STAGING, RELEASE }

interface AppConfig {
    val baseUrl: String
    val environment: Environment
}

expect object PlatformConfig : AppConfig

object AppConfigProvider : AppConfig {
    @Volatile
    private var overrideBaseUrl: String? = null
    override val baseUrl: String get() = overrideBaseUrl ?: PlatformConfig.baseUrl
    override val environment: Environment get() = PlatformConfig.environment
    
    fun overrideBaseUrl(newUrl: String?) { overrideBaseUrl = newUrl }
}
