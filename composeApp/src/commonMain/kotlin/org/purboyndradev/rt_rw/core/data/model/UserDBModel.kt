package org.purboyndradev.rt_rw.core.data.model


import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@Entity
data class UserDBModel(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    @SerialName("username")
    val name: String,
    @SerialName("access_token")
    val accessToken: String,
    @SerialName("refresh_token")
    val refreshToken: String,
    @SerialName("profile_picture")
    val profilePicture: String? = null,
    @SerialName("email")
    val email: String? = null
)