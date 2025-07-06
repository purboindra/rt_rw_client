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
                produceFilePath().toPath()
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
        println("Saving user to DataStore: $user")
        try {
            println("Saving user to DataStore try: $user")
            userDataStore.addUser(user)
        } catch (e: Exception) {
            println("Failed to write userDataStore: ${e.message}, stackTrace: ${e.stackTraceToString()}")
        }
    }
    
    suspend fun clearUserData() {
        userDataStore.removeUser()
    }
}