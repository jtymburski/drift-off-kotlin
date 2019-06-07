package com.jordantymburski.driftoff.kotlin.di

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
}