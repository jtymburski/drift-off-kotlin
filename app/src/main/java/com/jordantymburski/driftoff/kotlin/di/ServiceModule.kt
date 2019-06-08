package com.jordantymburski.driftoff.kotlin.di

import android.app.AlarmManager
import android.content.Context
import android.content.Intent
import com.jordantymburski.driftoff.kotlin.domain.adapter.AlarmScheduler
import com.jordantymburski.driftoff.kotlin.service.AlarmJobScheduler
import com.jordantymburski.driftoff.kotlin.service.AlarmReceiver
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
}