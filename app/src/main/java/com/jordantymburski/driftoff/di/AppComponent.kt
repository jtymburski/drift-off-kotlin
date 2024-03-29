package com.jordantymburski.driftoff.di

import com.jordantymburski.driftoff.App
import dagger.Component
import dagger.android.AndroidInjectionModule
import dagger.android.AndroidInjector
import javax.inject.Singleton

@Singleton
@Component(modules = [
    AndroidInjectionModule::class,
    AppModule::class,
    DataModule::class,
    HomeActivityModule::class,
    ReceiverModule::class,
    ServiceModule::class
])
interface AppComponent: AndroidInjector<App>