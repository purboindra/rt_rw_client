package org.purboyndradev.rt_rw.core.data.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.core.IOException
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import org.purboyndradev.rt_rw.di.AuthKeys
import co.touchlab.kermit.Logger as KermitLogger


class AppAuthRepository(private val dataStore: DataStore<Preferences>) {
    suspend fun saveTokens(accessToken: String, refreshToken: String) {
        dataStore.edit { tokens ->
            tokens[AuthKeys.ACCESS_TOKEN] = accessToken
            tokens[AuthKeys.REFRESH_TOKEN] = refreshToken

            KermitLogger.w("AuthRepositorySaveTokens") {
                "Access Token: $accessToken, Refresh Token: $refreshToken"
            }

            println("Access Token: $accessToken, Refresh Token: $refreshToken")
        }
    }

    suspend fun saveFCMToken(fcmToken: String) {
        dataStore.edit {
            it[AuthKeys.FCM_TOKEN] = fcmToken
        }
    }

    suspend fun saveEmail(email: String) {
        dataStore.edit {
            it[AuthKeys.EMAIL] = email
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

    suspend fun saveIsEmailVerified(isEmailVerified: Boolean) {
        dataStore.edit {
            it[AuthKeys.IS_EMAIL_VERIFIED] = isEmailVerified
        }
    }

    suspend fun clearTokens() {
        dataStore.edit {
            it.remove(AuthKeys.ACCESS_TOKEN)
            it.remove(AuthKeys.REFRESH_TOKEN)
        }
    }

    val emailFlow: Flow<String?> = dataStore.data.catch { exception ->
        if (exception is IOException) {
            emit(emptyPreferences())
        } else {
            throw exception
        }
    }.map { preferences ->
        preferences[AuthKeys.EMAIL]
    }

    val isEmailVerifiedFlow: Flow<Boolean> = dataStore.data.catch { exception ->
        if (exception is IOException) {
            emit(emptyPreferences())
        } else {
            throw exception
        }
    }.map { preferences ->
        preferences[AuthKeys.IS_EMAIL_VERIFIED] ?: false
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

    val isAuthenticated: Flow<Boolean> =
        combine(accessTokenFlow, refreshTokenFlow) { accessToken, refreshToken ->
            !accessToken.isNullOrBlank() && !refreshToken.isNullOrBlank()
        }.distinctUntilChanged()

    suspend fun clearUserPrefs() {
        dataStore.edit { settings ->
            settings.remove(AuthKeys.ACCESS_TOKEN)
            settings.remove(AuthKeys.REFRESH_TOKEN)
            settings.remove(AuthKeys.USER_ID)
            settings.remove(AuthKeys.USERNAME)
            settings.remove(AuthKeys.EMAIL)
        }
    }
}