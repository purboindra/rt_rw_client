package org.purboyndradev.rt_rw.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import org.purboyndradev.rt_rw.AndroidContextProvider

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(
    name = "app_auth"
)

actual fun createDataStore(): DataStore<Preferences> {
    val context = AndroidContextProvider.getContext()
    return context.dataStore
}