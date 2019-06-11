package com.jordantymburski.driftoff.kotlin.common

import android.content.Context
import android.media.AudioManager

object ServiceProvider {
    fun audioManager(context: Context): AudioManager {
        return context.getSystemService(Context.AUDIO_SERVICE) as AudioManager
    }
}