package com.jordantymburski.driftoff.kotlin.di

import android.app.AlarmManager
import android.content.Context
import android.content.Intent
import android.media.AudioManager
import com.jordantymburski.driftoff.kotlin.domain.adapter.AlarmScheduler
import com.jordantymburski.driftoff.kotlin.domain.adapter.AudioController
import com.jordantymburski.driftoff.kotlin.service.AlarmJobScheduler
import com.jordantymburski.driftoff.kotlin.service.AlarmReceiver
import com.jordantymburski.driftoff.kotlin.service.AndroidAudioController
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class ServiceModule {
    @Provides
    @Singleton
    internal fun provideAlarmScheduler(alarmManager: AlarmManager, context: Context): AlarmScheduler {
        val intent = Intent(context, AlarmReceiver::class.java)
        intent.action = AlarmReceiver.ACTION_STOP_AUDIO
        return AlarmJobScheduler(
            alarmManager = alarmManager,
            broadcastIntent = intent,
            context = context)
    }

    @Provides
    @Singleton
    internal fun provideAudioController(audioManager: AudioManager): AudioController {
        return AndroidAudioController(audioManager)
    }
}