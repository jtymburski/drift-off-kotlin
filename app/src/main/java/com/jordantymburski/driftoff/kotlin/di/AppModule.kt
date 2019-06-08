package com.jordantymburski.driftoff.kotlin.di

import android.app.AlarmManager
import android.app.Application
import android.content.Context
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
}