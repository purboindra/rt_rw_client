package org.purboyndradev.rt_rw.di

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey

object AuthKeys {
    val ACCESS_TOKEN = stringPreferencesKey("access_token")
    val REFRESH_TOKEN = stringPreferencesKey("refresh_token")
    val USER_ID = stringPreferencesKey("user_id")
    val USERNAME = stringPreferencesKey("username")
    val FCM_TOKEN = stringPreferencesKey("fcm_token")
    val EMAIL = stringPreferencesKey("email")
    val IS_EMAIL_VERIFIED = booleanPreferencesKey("is_email_verified")
}

object NotificationKeys {
    val NOTIF_DENIED_AT_MS = stringPreferencesKey("notif_denied_at_ms")
}

expect fun createDataStore(): DataStore<Preferences>