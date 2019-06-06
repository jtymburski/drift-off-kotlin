package com.jordantymburski.driftoff.kotlin

import android.app.Activity
import android.app.Application
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasActivityInjector
import javax.inject.Inject

class App: Application(), HasActivityInjector {
    @Inject lateinit var dispatchingActivityInjector: DispatchingAndroidInjector<Activity>

    override fun onCreate() {
        super.onCreate()

        // TODO: Component injection
    }

    override fun activityInjector(): DispatchingAndroidInjector<Activity> {
        return dispatchingActivityInjector
    }

    fun component(newComponent: AndroidInjector<App>) {
        newComponent.inject(this)
    }
}
