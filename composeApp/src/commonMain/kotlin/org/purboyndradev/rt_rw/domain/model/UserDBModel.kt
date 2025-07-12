package org.purboyndradev.rt_rw.domain.model


import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@Entity
data class UserDBModel(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val username: String? = "",
    val accessToken: String,
    val refreshToken: String,
    val profilePicture: String? = "",
    val email: String? = ""
)

fun UserDBModel.toEmptyModel(): UserDBModel {
    return UserDBModel(
        accessToken = "",
        refreshToken = "",
        username = "",
        email = "",
    );
}