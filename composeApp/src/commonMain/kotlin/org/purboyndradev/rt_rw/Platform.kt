package org.purboyndradev.rt_rw

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import okio.Path.Companion.toPath

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform

expect object TelegramLauncher {
    fun open(url: String)
}

expect object ClipboardReader {
    fun getText(): String?
}