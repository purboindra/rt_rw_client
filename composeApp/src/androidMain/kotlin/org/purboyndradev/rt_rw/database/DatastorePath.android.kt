package org.purboyndradev.rt_rw.database

import android.content.Context
import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import org.purboyndradev.rt_rw.AndroidContextProvider
import org.purboyndradev.rt_rw.core.data.datastore.UserDataStore

private const val USER_DATASTORE_FILE_NAME =
    "user_prefs.pb"

fun getPreferencesDataStorePath(appContext: Context): String =
    appContext.filesDir.resolve(dataStoreFileName).absolutePath

actual fun createPreferencesDataStore(): DataStore<Preferences> {
    val androidContext = AndroidContextProvider.getContext()
    
    val path = getPreferencesDataStorePath(androidContext)
    return getPreferencesDataStore(path)
}

actual fun createCurrentUserDataStore(): UserDataStore {
    val androidContext = AndroidContextProvider.getContext()
    return UserDataStore {
        androidContext.filesDir.resolve(
            "current_user_prefs.json"
        ).absolutePath
    }
}