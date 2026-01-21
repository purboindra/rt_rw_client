package org.purboyndradev.rt_rw.helper

import org.koin.core.context.startKoin
import org.purboyndradev.rt_rw.createPlatformModule

fun initKoin() {
    startKoin {
        createPlatformModule()
    }
}