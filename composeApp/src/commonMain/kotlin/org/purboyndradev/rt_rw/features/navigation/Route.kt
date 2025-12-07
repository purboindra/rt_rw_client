package org.purboyndradev.rt_rw.features.navigation

import kotlinx.serialization.Serializable
import org.purboyndradev.rt_rw.helper.OTPType

@Serializable
object Login

@Serializable
data class OTP(
    val phoneNumber: String? = null,
    val otpType: OTPType = OTPType.TELEGRAM,
    val email: String? = null
)

@Serializable
object Splash

@Serializable
object Main

@Serializable
object Home {
    const val ROUTE = "home"
}

@Serializable
object Profile {
    const val ROUTE = "profile"
}

@Serializable
object News {
    const val ROUTE = "news"
}

@Serializable
object Activity {
    const val ROUTE = "activity"
}

@Serializable
data class ActivityDetail(val id: String)

@Serializable
data class NewsDetail(val id: String)

@Serializable
object NotificationPermissions

@Serializable
object CreateReport {
    const val ROUTE = "create_report"
}

@Serializable
object CameraPreviewScreen

@Serializable
object VerifyEmailOnBoardingScreen

@Serializable
object Reports {
    const val ROUTE = "reports"
}

@Serializable
data class ReportDetail(val id: String)