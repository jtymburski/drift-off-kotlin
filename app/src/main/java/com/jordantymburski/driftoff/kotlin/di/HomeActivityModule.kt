package com.jordantymburski.driftoff.kotlin.di

import com.jordantymburski.driftoff.kotlin.presentation.HomeActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class HomeActivityModule {
    @ContributesAndroidInjector
    internal abstract fun contributeHomeActivity(): HomeActivity
}