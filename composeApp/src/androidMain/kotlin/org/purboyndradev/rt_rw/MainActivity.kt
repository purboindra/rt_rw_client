package org.purboyndradev.rt_rw

import android.Manifest
import android.app.ComponentCaller
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.content.ContextCompat
import org.purboyndradev.rt_rw.features.navigation.StartDestinationData

class MainActivity : ComponentActivity() {

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission(),
    ) { isGranted ->
        if (isGranted) {
            // TODO: DO SOMETHING
        } else {
            /// TODO: DO SOMETHING
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {

        askNotificationPermission()

        FirebaseHelper.getTokenFCM { token -> println("FCM Token: $token") }

        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        setContent {
            App(startDestination = getStartDestinationFromIntent(intent))
        }
    }

    override fun onNewIntent(intent: Intent, caller: ComponentCaller) {
        super.onNewIntent(intent, caller)
        handleNavigationFromNotification(intent)
    }

    private fun askNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.POST_NOTIFICATIONS
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                /// TODO: DO SOMETHING
            } else if (shouldShowRequestPermissionRationale(Manifest.permission.POST_NOTIFICATIONS)) {
                /// TODO: DO SOMETHING
            } else {
                requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        }
    }

    private fun getStartDestinationFromIntent(intent: Intent): StartDestinationData? {
        return when {
            intent.hasExtra("screen") -> {
                when (intent.getStringExtra("screen")) {
                    "home" -> StartDestinationData("home")
                    "profile" -> StartDestinationData("profile")
                    "news" -> StartDestinationData("news")
                    "activity" -> StartDestinationData("activity")
                    "details" -> {
                        val itemId = intent.getStringExtra("item_id")
                        StartDestinationData("details", itemId)
                    }

                    else -> StartDestinationData("home")
                }
            }

            else -> null
        }
    }

    private fun handleNavigationFromNotification(intent: Intent) {
        /// TODO: DO SOMETHING
        println("Intent handleNavigationFromNotification: $intent")
    }

}

@Preview
@Composable
fun AppAndroidPreview() {
    App()
}