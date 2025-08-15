package org.purboyndradev.rt_rw

expect object NotificationManager {
    fun hasGrantedNotificationPermission(): Boolean
}