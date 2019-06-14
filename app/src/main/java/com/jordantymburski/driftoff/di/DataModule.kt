package com.jordantymburski.driftoff.di

import android.content.Context
import com.jordantymburski.driftoff.data.PreferenceStorage
import com.jordantymburski.driftoff.domain.adapter.Storage
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