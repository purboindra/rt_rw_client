package org.purboyndradev.rt_rw.features.navigation

import kotlinx.serialization.Serializable

@Serializable
object Login

@Serializable
data class OTP(val phoneNumber: String)

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