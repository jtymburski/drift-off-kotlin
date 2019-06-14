package com.jordantymburski.driftoff.common

import android.app.AlarmManager
import android.content.Context
import android.media.AudioManager

object ServiceProvider {
    fun alarmManager(context: Context): AlarmManager {
        return context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
    }

    fun audioManager(context: Context): AudioManager {
        return context.getSystemService(Context.AUDIO_SERVICE) as AudioManager
    }
}