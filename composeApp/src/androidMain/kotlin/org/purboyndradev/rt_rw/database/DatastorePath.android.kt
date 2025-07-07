package org.purboyndradev.rt_rw.database

import android.content.Context
import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import org.purboyndradev.rt_rw.AndroidContextProvider
import org.purboyndradev.rt_rw.core.data.datastore.UserDataStore

 const val USER_DATASTORE_FILE_NAME =
    "user_prefs.json"

fun getPreferencesDataStorePath(appContext: Context): String {
    val path = appContext.filesDir.resolve(USER_DATASTORE_FILE_NAME).absolutePath
    Log.d("PreferencesDataStore", "Path: $path")
    return path
}

actual fun createPreferencesDataStore(): DataStore<Preferences> {
    val androidContext = AndroidContextProvider.getContext()
    val path = getPreferencesDataStorePath(androidContext)
    return getPreferencesDataStore(path)
}

actual fun createCurrentUserDataStore(): UserDataStore {
    val androidContext = AndroidContextProvider.getContext()
    val path = getPreferencesDataStorePath(androidContext)
    return UserDataStore {
        path
    }
}