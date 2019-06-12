package com.jordantymburski.driftoff.kotlin.di

import com.jordantymburski.driftoff.kotlin.service.AlarmReceiver
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ReceiverModule {
    @ContributesAndroidInjector
    abstract fun contributeAlarmReceiver(): AlarmReceiver
}