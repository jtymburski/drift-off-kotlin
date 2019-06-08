package com.jordantymburski.driftoff.kotlin.service

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class AlarmReceiver: BroadcastReceiver() {
    companion object {
        const val ACTION_STOP_AUDIO = "com.jordantymburski.driftoff.STOP_AUDIO"
    }

    override fun onReceive(context: Context?, intent: Intent?) {
    }
}