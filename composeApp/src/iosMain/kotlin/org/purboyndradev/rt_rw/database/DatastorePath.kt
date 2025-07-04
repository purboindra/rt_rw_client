package org.purboyndradev.rt_rw.database


import kotlinx.cinterop.ExperimentalForeignApi
import platform.Foundation.NSDocumentDirectory
import platform.Foundation.NSFileManager
import platform.Foundation.NSUserDomainMask

private const val USER_DATASTORE_FILE_NAME = "user_prefs.pb"

@OptIn(ExperimentalForeignApi::class)
internal actual fun produceUserDataStorePath(): String {
    val documentDirectory = NSFileManager.defaultManager.URLForDirectory(
        directory = NSDocumentDirectory,
        inDomain = NSUserDomainMask,
        appropriateForURL = null,
        create = true,
        error = null,
    )?.path ?: throw IllegalStateException("Could not get document directory path")
    
    return "$documentDirectory/$USER_DATASTORE_FILE_NAME"
}