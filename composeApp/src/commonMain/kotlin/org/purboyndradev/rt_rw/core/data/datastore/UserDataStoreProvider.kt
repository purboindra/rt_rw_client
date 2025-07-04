package org.purboyndradev.rt_rw.core.data.datastore


import androidx.datastore.core.DataStore
import androidx.datastore.core.DataStoreFactory
import androidx.datastore.core.okio.OkioStorage // Use okio variant
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import okio.FileSystem
import okio.Path.Companion.toPath
import okio.SYSTEM
import org.purboyndradev.rt_rw.core.data.model.UserDBModel
import org.purboyndradev.rt_rw.database.UserJsonSerializer
import org.purboyndradev.rt_rw.database.produceUserDataStorePath

object UserDataStoreProvider {
    
    private val datastoreScope =
        CoroutineScope(Dispatchers.Default + SupervisorJob())
    
    val userDataStore: DataStore<UserDBModel?> by lazy {
        DataStoreFactory.create(
            storage = OkioStorage(
                fileSystem = FileSystem.SYSTEM,
                serializer = UserJsonSerializer,
                producePath = { produceUserDataStorePath().toPath() }
            ),
            corruptionHandler = null,
            migrations = listOf(),
            scope = datastoreScope
        )
    }
}

/// TODO: MOVE TO USER REPOSITORY FILE
class UserRepository(
    private val userDataStore: DataStore<UserDBModel?> = UserDataStoreProvider.userDataStore
) {
    val currentUserData: kotlinx.coroutines.flow.Flow<UserDBModel?> =
        userDataStore.data
    
    suspend fun saveUser(user: UserDBModel) {
        userDataStore.updateData {
            // It replaces the current data with this new user object
            user
        }
    }
    
    suspend fun clearUserData() {
        userDataStore.updateData {
            null // This will trigger writeTo in serializer with t = null
        }
    }
    
    suspend fun updateUserAccessToken(newAccessToken: String) {
        userDataStore.updateData { currentUser ->
            currentUser?.copy(accessToken = newAccessToken)
            // If currentUser is null, this will keep it null (or you could throw an error)
        }
    }
}