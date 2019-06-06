package com.jordantymburski.driftoff.kotlin.di

import com.jordantymburski.driftoff.kotlin.App
import dagger.Component
import dagger.android.AndroidInjectionModule
import dagger.android.AndroidInjector
import javax.inject.Singleton

@Singleton
@Component(modules = [
    AndroidInjectionModule::class,
    HomeActivityModule::class
])
interface AppComponent: AndroidInjector<App> {
}