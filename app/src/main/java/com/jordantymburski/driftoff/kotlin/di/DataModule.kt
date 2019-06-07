package com.jordantymburski.driftoff.kotlin.di

import com.jordantymburski.driftoff.kotlin.data.PreferenceStorage
import com.jordantymburski.driftoff.kotlin.domain.adapter.Storage
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class DataModule {
    @Provides
    @Singleton
    fun provideStorage(): Storage {
        return PreferenceStorage()
    }
}