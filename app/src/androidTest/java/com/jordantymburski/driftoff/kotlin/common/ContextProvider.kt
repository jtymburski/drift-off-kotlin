package com.jordantymburski.driftoff.kotlin.common

import android.content.Context
import androidx.test.platform.app.InstrumentationRegistry

object ContextProvider {
    fun get(): Context {
        return InstrumentationRegistry.getInstrumentation().context
    }
}