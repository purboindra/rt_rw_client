package org.purboyndradev.rt_rw.database

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import okio.Path.Companion.toPath
import org.purboyndradev.rt_rw.core.data.datastore.UserDataStore


/// Datastore
expect fun createPreferencesDataStore(): DataStore<Preferences>

internal const val dataStoreFileName = "dice.preferences_pb"

fun getPreferencesDataStore(path: String) =
    PreferenceDataStoreFactory.createWithPath {
        path.toPath()
    }

expect fun createCurrentUserDataStore(): UserDataStore