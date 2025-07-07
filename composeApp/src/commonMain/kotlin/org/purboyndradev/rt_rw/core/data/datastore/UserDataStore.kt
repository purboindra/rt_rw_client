package org.purboyndradev.rt_rw.core.data.datastore


import androidx.datastore.core.DataStoreFactory
import androidx.datastore.core.okio.OkioStorage
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import okio.FileSystem
import okio.Path.Companion.toPath
import okio.SYSTEM
import org.purboyndradev.rt_rw.database.UserJsonSerializer
import org.purboyndradev.rt_rw.domain.model.UserDBModel

class UserDataStore(
    private val produceFilePath: () -> String,
) {
    
    private val lock = Mutex()
    
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
        lock.withLock {
            db.updateData { user }
        }
    }
    
    suspend fun updateUser(user: UserDBModel) {
        lock.withLock {
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
    }
    
    suspend fun removeUser() {
        lock.withLock {
            db.updateData {
                null
            }
        }
    }
}

/// TODO: MOVE TO USER REPOSITORY FILE
class UserRepository(
    private val userDataStore: UserDataStore,
) {
    @OptIn(ExperimentalCoroutinesApi::class)
    val currentUserData: Flow<UserDBModel?>
        get() = userDataStore.user
    
    suspend fun saveUser(user: UserDBModel) {
        println("Saving user: $user")
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