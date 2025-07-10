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
object Home

@Serializable
object Profile

@Serializable
object News

@Serializable
object Activity