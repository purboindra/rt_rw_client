package org.purboyndradev.rt_rw

import androidx.compose.runtime.Composable

expect object NotificationService {
    fun hasGrantedNotificationPermission(): Boolean
    
    @Composable
    fun rememberRequestNotificationPermissionLauncher(onResult: (Boolean) -> Unit): () -> Unit
    
    fun createDefaultNotificationChannel()
}