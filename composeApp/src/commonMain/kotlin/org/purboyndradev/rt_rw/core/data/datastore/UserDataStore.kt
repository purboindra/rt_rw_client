package org.purboyndradev.rt_rw.core.data.datastore


import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import okio.IOException
import org.purboyndradev.rt_rw.di.AuthKeys
import org.purboyndradev.rt_rw.di.NotificationKeys
import kotlin.time.Clock
import kotlin.time.ExperimentalTime


class NotificationRepository(private val dataStore: DataStore<Preferences>) {
    val deniedAtFlow: Flow<Long?> =
        dataStore.data.catch { exception
            ->
            println("Exception deniedAtFlow: ${exception.message}")
            if (exception is IOException) {
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }.map { preferences ->
            preferences[NotificationKeys.NOTIF_DENIED_AT_MS]?.toLong()
        }
    
    @OptIn(ExperimentalTime::class)
    suspend fun markNotificationPermissionDenied(
    ) {
        val nowMs: Long = Clock.System.now().toEpochMilliseconds()
        
        dataStore.edit { preferences ->
            preferences[NotificationKeys.NOTIF_DENIED_AT_MS] = nowMs.toString()
        }
    }
    
    @OptIn(ExperimentalTime::class)
    suspend fun hasAWeekPassedSinceNotificationPermissionDenied(): Boolean {
        val nowMs: Long = Clock.System.now().toEpochMilliseconds()
        val deniedAtMs = dataStore.data.firstOrNull()?.get(
            NotificationKeys.NOTIF_DENIED_AT_MS
        )?.toLong()
        
        if (deniedAtMs == null) return false
        
        return (nowMs - deniedAtMs) > 7 * 24 * 60 * 60 * 1000
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
    }.map { preferences ->
        preferences[AuthKeys.FCM_TOKEN]
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