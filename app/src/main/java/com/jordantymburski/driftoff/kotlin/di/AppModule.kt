package com.jordantymburski.driftoff.kotlin.di

import android.app.AlarmManager
import android.app.Application
import android.content.Context
import android.media.AudioManager
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class AppModule(
    private val application: Application
) {
    @Provides
    @Singleton
    internal fun provideApplicationContext(): Context {
        return application
    }

    @Provides
    @Singleton
    internal fun provideAlarmManager(): AlarmManager {
        return application.getSystemService(Context.ALARM_SERVICE) as AlarmManager
    }

    @Provides
    @Singleton
    internal fun provideAudioManager(): AudioManager {
        return application.getSystemService(Context.AUDIO_SERVICE) as AudioManager
    }
}