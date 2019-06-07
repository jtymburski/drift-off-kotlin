package com.jordantymburski.driftoff.kotlin.di

import android.content.Context
import com.jordantymburski.driftoff.kotlin.data.PreferenceStorage
import com.jordantymburski.driftoff.kotlin.domain.adapter.Storage
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class DataModule {
    @Provides
    @Singleton
    internal fun provideStorage(context: Context): Storage {
        return PreferenceStorage(context)
    }
}