package org.purboyndradev.rt_rw

import android.app.Application
import android.util.Log
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.purboyndradev.rt_rw.di.initKoin

class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        
        /// INIT ANDROID CONTEXT
        // FOR CONTEXT ANDROID PLATFORM NEEDED
        AndroidContextProvider.initialize(this)
        
        initKoin {
            androidLogger()
            androidContext(this@MyApplication)
        }
        
    }
}