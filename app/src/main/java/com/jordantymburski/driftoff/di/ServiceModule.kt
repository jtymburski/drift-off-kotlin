package com.jordantymburski.driftoff.di

import android.app.AlarmManager
import android.content.Context
import android.content.Intent
import android.media.AudioManager
import com.jordantymburski.driftoff.domain.adapter.AlarmScheduler
import com.jordantymburski.driftoff.domain.adapter.AudioController
import com.jordantymburski.driftoff.service.AlarmJobScheduler
import com.jordantymburski.driftoff.service.AlarmReceiver
import com.jordantymburski.driftoff.service.AndroidAudioController
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