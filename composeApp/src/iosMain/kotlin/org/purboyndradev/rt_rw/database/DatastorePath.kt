package org.purboyndradev.rt_rw.database


import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import kotlinx.cinterop.ExperimentalForeignApi
import org.purboyndradev.rt_rw.core.data.datastore.UserDataStore
import platform.Foundation.NSDocumentDirectory
import platform.Foundation.NSFileManager
import platform.Foundation.NSUserDomainMask
import platform.Foundation.NSURL

private const val USER_DATASTORE_FILE_NAME = "user_prefs.pb"

@OptIn(ExperimentalForeignApi::class)
fun getPreferencesDataStorePath(): String {
    val documentDirectory = NSFileManager.defaultManager.URLForDirectory(
        directory = NSDocumentDirectory,
        inDomain = NSUserDomainMask,
        appropriateForURL = null,
        create = false,
        error = null
    )
    return requireNotNull(documentDirectory).path + "/$dataStoreFileName"
}

actual fun createPreferencesDataStore(): DataStore<Preferences> {
    val path = getPreferencesDataStorePath()
    return getPreferencesDataStore(path)
}

@OptIn(ExperimentalForeignApi::class)
private fun fileDirectory(): String {
    val documentDirectory: NSURL? = NSFileManager.defaultManager.URLForDirectory(
        directory = NSDocumentDirectory,
        inDomain = NSUserDomainMask,
        appropriateForURL = null,
        create = false,
        error = null,
    )
    return requireNotNull(documentDirectory).path!!
}

actual fun createCurrentUserDataStore(): UserDataStore =
    UserDataStore {
        "${fileDirectory()}/cart.json"
    }