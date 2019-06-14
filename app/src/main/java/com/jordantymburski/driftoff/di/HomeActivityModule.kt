package com.jordantymburski.driftoff.di

import com.jordantymburski.driftoff.presentation.HomeActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class HomeActivityModule {
    @ContributesAndroidInjector
    internal abstract fun contributeHomeActivity(): HomeActivity
}