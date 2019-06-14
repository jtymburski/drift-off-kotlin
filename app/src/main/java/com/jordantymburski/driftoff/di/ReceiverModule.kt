package com.jordantymburski.driftoff.di

import com.jordantymburski.driftoff.service.AlarmReceiver
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ReceiverModule {
    @ContributesAndroidInjector
    abstract fun contributeAlarmReceiver(): AlarmReceiver
}