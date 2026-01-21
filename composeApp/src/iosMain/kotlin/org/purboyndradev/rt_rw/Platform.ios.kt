package org.purboyndradev.rt_rw

import org.koin.dsl.module
import platform.Foundation.NSURL
import platform.UIKit.UIApplication
import platform.UIKit.UIDevice
import platform.UIKit.UIPasteboard

class IOSPlatform : Platform {
    override val name: String =
        UIDevice.currentDevice.systemName() + " " + UIDevice.currentDevice.systemVersion
}

private const val USER_DATASTORE_FILE_NAME =
    "user_prefs.pb"

actual fun getPlatform(): Platform = IOSPlatform()

actual class PlatformContext

actual fun createPlatformModule() = module {
    single { PlatformContext() }
}

actual object TelegramLauncher {
    actual fun open(url: String) {
        val nsUrl = NSURL.URLWithString(url)
        
        if (nsUrl != null && UIApplication.sharedApplication.canOpenURL(nsUrl)) {
            UIApplication.sharedApplication.openURL(nsUrl)
        } else {
            println("Error: Could not open URL: $url. It might be invalid or the required app (e.g., Telegram) is not installed.")
        }
    }
}

actual object ClipboardReader {
    actual fun getText(): String? {
        val pasteBoard = UIPasteboard.generalPasteboard
        return pasteBoard.string
    }
}

/// DATASTORE
//
//@OptIn(ExperimentalForeignApi::class)
//fun createDataStore(): DataStore<Preferences> = createDataStore(
//    {
//        val documentDirectory: NSURL? =
//            NSFileManager.defaultManager.URLForDirectory(
//                directory = NSDocumentDirectory,
//                inDomain = NSUserDomainMask,
//                appropriateForURL = null,
//                create = false,
//                error = null,
//            )
//        requireNotNull(documentDirectory).path + "/$USER_DATASTORE_FILE_NAME"
//    }
//)