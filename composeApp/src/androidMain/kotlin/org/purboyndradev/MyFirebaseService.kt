package org.purboyndradev

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.media.RingtoneManager
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.purboyndradev.rt_rw.MainActivity
import org.purboyndradev.rt_rw.R
import org.purboyndradev.rt_rw.core.data.datastore.AppAuthRepository

private const val TAG = "MyFirebaseService"

class MyFirebaseService : FirebaseMessagingService(), KoinComponent {
    
    private val authRepo: AppAuthRepository by inject()
    
    override fun onNewToken(token: String) {
        super.onNewToken(token)
        
        print("New Token: $token")
        
        CoroutineScope(Dispatchers.IO).launch {
            authRepo.saveFCMToken(token)
        }
    }
    
    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)
        
        Log.d(TAG, "From: ${remoteMessage.from}")
        
        val navigationData = remoteMessage.data
        
        if (navigationData.isNotEmpty()) {
            Log.d(TAG, "Message data payload: ${remoteMessage.data}")
        }
        
        navigationData?.let {
            Log.d(TAG, "Message Notification Body: $it")
            sendNotification(it)
        }
    }
    
    private fun sendNotification(
        data: Map<String, String>
    ) {
        
        val title = data["title"] ?: ""
        val body = data["body"] ?: ""
        
        Log.d(TAG, "sendNotification: data: $data")
        
        val intent = Intent(this, MainActivity::class.java).apply {
            addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            data.forEach { (key, value) ->
                putExtra(key, value)
            }
        }
        
        val requestCode = System.currentTimeMillis().toInt()
        val pendingIntent = PendingIntent.getActivity(
            this,
            requestCode,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE,
        )
        
        val channelId = getString(R.string.default_notification_channel_id)
        val defaultSoundUri =
            RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        
        val notificationBuilder = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.drawable.ic_stat_notification)
            .setContentTitle(title)
            .setContentText(body)
            .setAutoCancel(true)
            .setSound(defaultSoundUri)
            .setPriority(NotificationCompat.PRIORITY_HIGH).setVisibility(
                NotificationCompat.VISIBILITY_PUBLIC
            )
            .setDefaults(Notification.DEFAULT_ALL)
            .setContentIntent(pendingIntent)
        
        notificationBuilder.setStyle(
            NotificationCompat.BigTextStyle().bigText(body)
        )
        
        val notificationManager =
            getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        
        // Since android Oreo notification channel is needed.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                getString(R.string.default_notification_channel_name),
                NotificationManager.IMPORTANCE_HIGH,
            ).apply {
                enableVibration(true)
                enableLights(true)
                setShowBadge(true)
                lockscreenVisibility = Notification.VISIBILITY_PUBLIC
            }
            notificationManager.createNotificationChannel(channel)
        }
        
        val notificationId = System.currentTimeMillis().toInt()
        notificationManager.notify(notificationId, notificationBuilder.build())
    }
}