package org.purboyndradev.rt_rw

import android.annotation.SuppressLint
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.util.Log
import androidx.core.content.ContextCompat.startActivity
import androidx.core.net.toUri

class AndroidPlatform : Platform {
    override val name: String = "Android ${Build.VERSION.SDK_INT}"
}

actual fun getPlatform(): Platform = AndroidPlatform()

actual object TelegramLauncher {
    private var applicationContext: Context? = null
    
    fun init(ctx: Context) {
        applicationContext = ctx.applicationContext
    }
    
    /// Function return Boolean
    /// If Telegram App is installed return true
    private fun isTelegramInstalled(context: Context): Boolean {
        val installed =
            listOf("org.telegram.messenger", "org.thunderdog.challegram")
        return installed.any {
            try {
                context.packageManager.getPackageInfo(
                    it,
                    PackageManager.GET_ACTIVITIES
                )
                true
            } catch (e: PackageManager.NameNotFoundException) {
                Log.e(
                    "TelegramLauncher",
                    "Error package manager not found: $e"
                )
                false
            }
        }
    }
    
    @SuppressLint("QueryPermissionsNeeded")
    actual fun open(url: String) {
        val currentContext = applicationContext
        
        val isInstalled = isTelegramInstalled(currentContext!!)
        
        if (!isInstalled) {
            Log.w(
                "TelegramLauncher",
                "Telegram app not found or no app to handle tg:// scheme."
            )
            try {
                val playStoreIntent = Intent(
                    Intent.ACTION_VIEW,
                    "market://details?id=org.telegram.messenger".toUri()
                )
                playStoreIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                currentContext.startActivity(playStoreIntent)
            } catch (e: ActivityNotFoundException) {
                Log.e(
                    "TelegramLauncher",
                    "Play Store not found to install Telegram.",
                    e
                )
                val browserIntent = Intent(
                    Intent.ACTION_VIEW,
                    "https://play.google.com/store/apps/details?id=org.telegram.messenger".toUri()
                )
                browserIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                if (browserIntent.resolveActivity(currentContext.packageManager) != null) {
                    currentContext.startActivity(browserIntent)
                } else {
                    Log.e(
                        "TelegramLauncher",
                        "No browser found to open Play Store link."
                    )
                }
            }
            return
        }
        
        if (false) {
            Log.e("TelegramLauncher", "Application context is not initialized.")
            return
        }
        
        try {
            val intent = Intent(Intent.ACTION_VIEW, url.toUri())
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            currentContext.startActivity(intent)
        } catch (e: Exception) {
            Log.e("TelegramLauncher", "Error opening URL: $url", e)
        }
        
    }
}
