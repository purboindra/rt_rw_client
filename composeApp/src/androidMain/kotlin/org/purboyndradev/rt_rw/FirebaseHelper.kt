package org.purboyndradev.rt_rw

import android.util.Log
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging

private const val TAG = "FirebaseHelper"

object FirebaseHelper {
    
    init {
        println("FirebaseHelper...")
    }
    
    fun getTokenFCM(onGetToken: (String) -> Unit) {
        FirebaseMessaging.getInstance().token.addOnCompleteListener(
            OnCompleteListener { task ->
                if (!task.isSuccessful) {
                    Log.w(
                        TAG,
                        "Fetching FCM registration token failed",
                        task.exception
                    )
                    return@OnCompleteListener
                }
                
                val token = task.result
                
                println("FCM Token: $token")
                
                onGetToken(token)
            })
    }
}