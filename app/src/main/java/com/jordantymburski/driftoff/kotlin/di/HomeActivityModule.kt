package com.jordantymburski.driftoff.kotlin.di

import com.jordantymburski.driftoff.kotlin.presentation.HomeActivity
import dagger.android.ContributesAndroidInjector

abstract class HomeActivityModule {
    @ContributesAndroidInjector
    abstract fun contributeHomeActivity(): HomeActivity
}