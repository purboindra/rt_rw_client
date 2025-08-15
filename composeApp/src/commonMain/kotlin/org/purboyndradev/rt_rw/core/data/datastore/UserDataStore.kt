package org.purboyndradev.rt_rw.core.data.datastore


import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import okio.IOException
import org.purboyndradev.rt_rw.di.AuthKeys

class AppAuthRepository(private val dataStore: DataStore<Preferences>) {
    suspend fun saveTokens(accessToken: String, refreshToken: String) {
        dataStore.edit { tokens ->
            tokens[AuthKeys.ACCESS_TOKEN] = accessToken
            tokens[AuthKeys.REFRESH_TOKEN] = refreshToken
            println("Access Token: $accessToken, Refresh Token: $refreshToken")
        }
    }
    
    suspend fun saveFCMToken(fcmToken: String) {
        dataStore.edit {
            it[AuthKeys.FCM_TOKEN] = fcmToken
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
    
    val fcmTokenFlow: Flow<String?> = dataStore.data.catch { exception ->
        if (exception is IOException) {
            emit(emptyPreferences())
        } else {
            throw exception
        }
    }.map {
        preferences -> preferences[AuthKeys.FCM_TOKEN]
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