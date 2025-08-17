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
