package org.purboyndradev.rt_rw

import android.content.Context
import java.lang.ref.WeakReference

actual object AppContext {
    private var value: WeakReference<Context?>? = null
    fun set(context: Context) {
        value = WeakReference(context)
    }

    fun get(): Context? {
        return value?.get() ?: throw RuntimeException("Context is not initialized")
    }
}