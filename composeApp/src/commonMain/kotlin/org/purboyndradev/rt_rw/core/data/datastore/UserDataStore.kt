package org.purboyndradev.rt_rw.core.data.datastore


import androidx.datastore.core.DataStore
import androidx.datastore.core.DataStoreFactory
import androidx.datastore.core.okio.OkioStorage
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import okio.FileSystem
import okio.IOException
import okio.Path.Companion.toPath
import okio.SYSTEM
import org.purboyndradev.rt_rw.database.UserJsonSerializer
import org.purboyndradev.rt_rw.di.AuthKeys
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

class AppAuthRepository(private val dataStore: DataStore<Preferences>) {
    suspend fun saveTokens(accessToken: String, refreshToken: String) {
        dataStore.edit { tokens ->
            tokens[AuthKeys.ACCESS_TOKEN] = accessToken
            tokens[AuthKeys.REFRESH_TOKEN] = refreshToken
            println("Access Token: $accessToken, Refresh Token: $refreshToken")
        }
    }
    
    suspend fun saveUserId(userId: String) {
        dataStore.edit {
            it[AuthKeys.USER_ID] = userId
        }
    }
    
    suspend fun saveUsername(username: String) {
        dataStore.edit {
            it[AuthKeys.USERNAME] = username
        }
    }
    
    val userIdFlow: Flow<String?> = dataStore.data.catch { exception ->
        if (exception is IOException) {
            emit(emptyPreferences())
        } else {
            throw exception
        }
    }.map { preferences ->
        preferences[AuthKeys.USER_ID]
    }
    
    val userNameFlow: Flow<String?> = dataStore.data.catch { exception ->
        if (exception is IOException) {
            emit(emptyPreferences())
        } else {
            throw exception
        }
    }.map { preferences ->
        preferences[AuthKeys.USERNAME]
    }
    
    val accessTokenFlow: Flow<String?> = dataStore.data
        .catch { exception ->
            if (exception is IOException) {
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }.map { preferences ->
            preferences[AuthKeys.ACCESS_TOKEN]
        }
    
    val refreshTokenFlow: Flow<String?> = dataStore.data
        .catch { exception ->
            if (exception is IOException) {
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }.map { preferences ->
            preferences[AuthKeys.REFRESH_TOKEN]
        }
    
    suspend fun clearUserPrefs() {
        dataStore.edit { settings ->
            settings.remove(AuthKeys.ACCESS_TOKEN)
            settings.remove(AuthKeys.REFRESH_TOKEN)
            settings.remove(AuthKeys.USER_ID)
            settings.remove(AuthKeys.USERNAME)
            
        }
    }
}