package org.purboyndradev.rt_rw.database

import org.purboyndradev.rt_rw.AndroidContextProvider

private const val USER_DATASTORE_FILE_NAME =
    "user_prefs.pb"


internal actual fun produceUserDataStorePath(): String {
    
    val androidContext = AndroidContextProvider.getContext()
    
    val filesDir = androidContext.filesDir
    return "$filesDir/$USER_DATASTORE_FILE_NAME"
}
