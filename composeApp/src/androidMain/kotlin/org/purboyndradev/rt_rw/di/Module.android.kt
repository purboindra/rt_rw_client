package org.purboyndradev.rt_rw.di

import io.ktor.client.engine.HttpClientEngine
import org.koin.core.module.Module
import org.koin.dsl.module
import io.ktor.client.engine.android.Android

actual val platformModule: Module = module {
    single<HttpClientEngine> {
        Android.create()
    }
}