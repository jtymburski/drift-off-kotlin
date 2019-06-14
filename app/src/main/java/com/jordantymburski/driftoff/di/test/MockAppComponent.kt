package com.jordantymburski.driftoff.di.test

import com.jordantymburski.driftoff.App
import com.jordantymburski.driftoff.di.HomeActivityModule
import com.jordantymburski.driftoff.di.ReceiverModule
import dagger.Component
import dagger.android.AndroidInjectionModule
import dagger.android.AndroidInjector
import javax.inject.Singleton

@Singleton
@Component(modules = [
    AndroidInjectionModule::class,
    MockDomainModule::class,
    HomeActivityModule::class,
    ReceiverModule::class
])
interface MockAppComponent: AndroidInjector<App>