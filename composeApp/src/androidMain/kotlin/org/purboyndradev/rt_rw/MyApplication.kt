package org.purboyndradev.rt_rw

import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.purboyndradev.rt_rw.di.initKoin

class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        
        initKoin {
            androidLogger()
            androidContext(this@MyApplication)
        }
        
    }
}