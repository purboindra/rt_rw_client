package org.purboyndradev.rt_rw

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.content.ContextCompat

actual object NotificationManager {
    actual fun hasGrantedNotificationPermission(): Boolean {
        val context = AndroidContextProvider.getContext()
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            return ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED
        } else {
            false
        }
    }
}