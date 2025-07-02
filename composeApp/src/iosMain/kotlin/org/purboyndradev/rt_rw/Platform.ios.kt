package org.purboyndradev.rt_rw

import platform.UIKit.UIDevice
import platform.Foundation.NSURL
import platform.UIKit.UIApplication

class IOSPlatform : Platform {
    override val name: String =
        UIDevice.currentDevice.systemName() + " " + UIDevice.currentDevice.systemVersion
}

actual fun getPlatform(): Platform = IOSPlatform()

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