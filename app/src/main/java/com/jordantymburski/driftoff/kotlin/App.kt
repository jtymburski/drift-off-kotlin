package com.jordantymburski.driftoff.kotlin

import android.app.Activity
import android.app.Application
import android.content.BroadcastReceiver
import com.jordantymburski.driftoff.kotlin.di.AppModule
import com.jordantymburski.driftoff.kotlin.di.DaggerAppComponent
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasActivityInjector
import dagger.android.HasBroadcastReceiverInjector
import javax.inject.Inject

class App: Application(), HasActivityInjector, HasBroadcastReceiverInjector {
    @Inject lateinit var dispatchingActivityInjector: DispatchingAndroidInjector<Activity>
    @Inject lateinit var dispatchingReceiverInjector: DispatchingAndroidInjector<BroadcastReceiver>

    override fun onCreate() {
        super.onCreate()

        component(DaggerAppComponent.builder()
            .appModule(AppModule(this))
            .build())
    }

    override fun activityInjector(): DispatchingAndroidInjector<Activity> {
        return dispatchingActivityInjector
    }

    override fun broadcastReceiverInjector(): AndroidInjector<BroadcastReceiver> {
        return dispatchingReceiverInjector
    }

    fun component(newComponent: AndroidInjector<App>) {
        newComponent.inject(this)
    }
}
