package org.purboyndradev.rt_rw

import androidx.compose.runtime.Composable

actual object NotificationService {
    actual fun hasGrantedNotificationPermission(): Boolean {
        TODO("Not yet implemented")
    }

    @Composable
    actual fun rememberRequestNotificationPermissionLauncher(onResult: (Boolean) -> Unit): () -> Unit {
        TODO("Not yet implemented")
    }

    actual fun createDefaultNotificationChannel() {
    }

}