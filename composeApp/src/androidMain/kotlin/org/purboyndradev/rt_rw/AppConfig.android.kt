package org.purboyndradev.rt_rw
import org.purboyndradev.rt_rw.config.BuildKonfig

actual object PlatformConfig: AppConfig{
    override val baseUrl: String = BuildKonfig.BASE_URL
    override val environment: Environment = when (BuildKonfig.ENVIRONMENT) {
        "DEBUG" -> Environment.DEBUG
        "STAGING" -> Environment.STAGING
        else -> Environment.RELEASE
    }
}