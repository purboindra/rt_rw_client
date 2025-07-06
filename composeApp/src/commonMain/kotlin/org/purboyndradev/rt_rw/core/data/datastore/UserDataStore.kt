package org.purboyndradev.rt_rw.core.data.datastore


import androidx.datastore.core.DataStoreFactory
import androidx.datastore.core.okio.OkioStorage // Use okio variant
import kotlinx.coroutines.flow.Flow
import okio.FileSystem
import okio.Path.Companion.toPath
import okio.SYSTEM
import org.purboyndradev.rt_rw.domain.model.UserDBModel
import org.purboyndradev.rt_rw.database.UserJsonSerializer

class UserDataStore(
    private val produceFilePath: () -> String,
) {
    private val db = DataStoreFactory.create(
        storage = OkioStorage<UserDBModel?>(
            fileSystem = FileSystem.SYSTEM,
            serializer = UserJsonSerializer,
            producePath = {
                val path = produceFilePath().toPath()
                println("producePath Path: $path")
                path
            },
        ),
    )
    
    val user: Flow<UserDBModel?>
        get() = db.data
    
    suspend fun addUser(user: UserDBModel) {
        db.updateData {
            user
        }
    }
    
    suspend fun updateUser(user: UserDBModel) {
        db.updateData { prevUser ->
            println("Updating user. Prev: $prevUser, New: $user")
            
            val updated = prevUser?.copy(
                accessToken = user.accessToken,
                refreshToken = user.refreshToken,
                profilePicture = user.profilePicture,
                username = user.username,
                email = user.email,
                id = user.id
            )
                ?: user
            println("Resulting user after update: $updated")
            updated
        }
    }
    
    suspend fun removeUser() {
        db.updateData {
            null
        }
    }
}

/// TODO: MOVE TO USER REPOSITORY FILE
class UserRepository(
    private val userDataStore: UserDataStore,
) {
    val currentUserData: Flow<UserDBModel?> = userDataStore.user
    
    suspend fun saveUser(user: UserDBModel) {
        try {
            userDataStore.addUser(user)
        } catch (e: Exception) {
            println("Failed to write userDataStore: ${e.message}, stackTrace: ${e.stackTraceToString()}")
        }
    }
    
    suspend fun clearUserData() {
        userDataStore.removeUser()
    }
    
    suspend fun updateUserAccessToken(
        newAccessToken: String,
        newRefreshToken: String
    ) {
        userDataStore.updateUser(
            user = UserDBModel(
                accessToken = newAccessToken,
                refreshToken = newRefreshToken,
            )
        )
    }
    
}