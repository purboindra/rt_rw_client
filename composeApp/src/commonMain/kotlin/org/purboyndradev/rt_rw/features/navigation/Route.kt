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