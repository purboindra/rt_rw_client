package org.purboyndradev.rt_rw

enum class Environment { DEBUG, STAGING, RELEASE }

interface AppConfig {
    val baseUrl: String
    val environment: Environment
}

expect object PlatformConfig : AppConfig {
    override val baseUrl: String
    override val environment: Environment
}
//
//object AppConfigProvider : AppConfig {
//    @Volatile
//    private var overrideBaseUrl: String? = null
//    override val baseUrl: String get() = overrideBaseUrl ?: PlatformConfig.baseUrl
//    override val environment: Environment get() = PlatformConfig.environment
//
//    fun overrideBaseUrl(newUrl: String?) { overrideBaseUrl = newUrl }
//}
