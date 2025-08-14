package org.purboyndradev.rt_rw

import android.Manifest
import android.app.AlertDialog
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import org.purboyndradev.rt_rw.features.notification.NotificationOnboardingScreen

class NotificationOnboardingActivity : ComponentActivity() {
    
    private val notificationPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            proceedToChannelSettings()
        } else {
            showPermissionDeniedDialog()
        }
    }
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        setContent {
            NotificationOnboardingScreen(
                onEnableNotifications = { startNotificationSetup() },
                onSkip = { skipOnboarding() }
            )
        }
    }
    
    private fun startNotificationSetup() {
        createNotificationChannel()
        
        when {
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU -> {
                if (ContextCompat.checkSelfPermission(
                        this,
                        Manifest.permission.POST_NOTIFICATIONS
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    notificationPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                } else {
                    proceedToChannelSettings()
                }
            }
            
            else -> {
                proceedToChannelSettings()
            }
        }
    }
    
    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channelId = getString(R.string.default_notification_channel_id)
            val notificationManager =
                getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            
            val channel = NotificationChannel(
                channelId,
                "Community Updates",
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "Important community announcements and activities"
                enableLights(true)
                lightColor = Color.BLUE
                enableVibration(true)
                vibrationPattern = longArrayOf(100, 200, 300, 400, 500)
                setShowBadge(true)
                lockscreenVisibility = Notification.VISIBILITY_PUBLIC
                setBypassDnd(true)
            }
            
            notificationManager.createNotificationChannel(channel)
        }
    }
    
    private fun proceedToChannelSettings() {
        if (needsChannelConfiguration()) {
            showChannelSettingsDialog()
        } else {
            completeOnboarding()
        }
    }
    
    private fun needsChannelConfiguration(): Boolean {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationManager =
                getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            val channelId = getString(R.string.default_notification_channel_id)
            val channel = notificationManager.getNotificationChannel(channelId)
            
            return channel == null ||
                    channel.importance < NotificationManager.IMPORTANCE_HIGH ||
                    !channel.shouldShowLights() ||
                    !channel.shouldVibrate()
        }
        return false
    }
    
    private fun showChannelSettingsDialog() {
        AlertDialog.Builder(this)
            .setTitle("Complete Setup")
            .setMessage(
                "To receive important community updates as pop-up notifications, please enable these settings:\n\n" +
                        "Allow notifications\n" +
                        "Pop-up notifications (or Heads-up)\n" +
                        "Sound\n" +
                        "Vibration\n\n" +
                        "This ensures you won't miss important community activities!"
            )
            .setPositiveButton("Open Settings") { _, _ ->
                openNotificationChannelSettings()
            }
            .setNegativeButton("Complete Later") { _, _ ->
                completeOnboarding()
            }
            .setCancelable(false)
            .show()
    }
    
    private fun openNotificationChannelSettings() {
        try {
            val intent = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                Intent(Settings.ACTION_CHANNEL_NOTIFICATION_SETTINGS).apply {
                    putExtra(Settings.EXTRA_APP_PACKAGE, packageName)
                    putExtra(
                        Settings.EXTRA_CHANNEL_ID,
                        getString(R.string.default_notification_channel_id)
                    )
                }
            } else {
                Intent(Settings.ACTION_APP_NOTIFICATION_SETTINGS).apply {
                    putExtra(Settings.EXTRA_APP_PACKAGE, packageName)
                }
            }
            startActivityForResult(intent, NOTIFICATION_SETTINGS_REQUEST_CODE)
        } catch (e: Exception) {
            val intent =
                Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                    data = "package:$packageName".toUri()
                }
            startActivity(intent)
            completeOnboarding()
        }
    }
    
    override fun onActivityResult(
        requestCode: Int,
        resultCode: Int,
        data: Intent?
    ) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == NOTIFICATION_SETTINGS_REQUEST_CODE) {
            completeOnboarding()
        }
    }
    
    private fun showPermissionDeniedDialog() {
        AlertDialog.Builder(this)
            .setTitle("Notifications Disabled")
            .setMessage(
                "You can enable notifications later in Settings > Apps > ${
                    getString(
                        R.string.app_name
                    )
                } > Notifications"
            )
            .setPositiveButton("Open Settings") { _, _ ->
                openAppSettings()
            }
            .setNegativeButton("Continue") { _, _ ->
                completeOnboarding()
            }
            .show()
    }
    
    private fun openAppSettings() {
        val intent =
            Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                data = "package:$packageName".toUri()
            }
        startActivity(intent)
        completeOnboarding()
    }
    
    private fun skipOnboarding() {
        getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
            .edit()
            .putBoolean("notification_onboarding_skipped", true)
            .putLong("onboarding_skipped_time", System.currentTimeMillis())
            .apply()
        
        completeOnboarding()
    }
    
    private fun completeOnboarding() {
        getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
            .edit()
            .putBoolean("notification_onboarding_completed", true)
            .apply()
        
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }
    
    companion object {
        private const val NOTIFICATION_SETTINGS_REQUEST_CODE = 1001
        
        fun shouldShowOnboarding(context: Context): Boolean {
            val prefs =
                context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
            val completed =
                prefs.getBoolean("notification_onboarding_completed", false)
            
            if (completed) return false
            
            val skipped =
                prefs.getBoolean("notification_onboarding_skipped", false)
            if (skipped) {
                val skippedTime = prefs.getLong("onboarding_skipped_time", 0)
                val daysSinceSkip =
                    (System.currentTimeMillis() - skippedTime) / (24 * 60 * 60 * 1000)
                return daysSinceSkip >= 7
            }
            
            return true
        }
    }
}