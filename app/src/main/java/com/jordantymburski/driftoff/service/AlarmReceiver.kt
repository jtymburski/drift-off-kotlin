package com.jordantymburski.driftoff.service

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.jordantymburski.driftoff.domain.usecase.RescheduleAlarm
import com.jordantymburski.driftoff.domain.usecase.StopAudio
import dagger.android.AndroidInjection
import javax.inject.Inject

class AlarmReceiver: BroadcastReceiver() {
    companion object {
        const val ACTION_STOP_AUDIO = "com.jordantymburski.driftoff.STOP_AUDIO"
    }

    /**
     * Reschedule alarm domain use case
     */
    @Inject lateinit var rescheduleAlarm: RescheduleAlarm

    /**
     * Stop audio domain use case
     */
    @Inject lateinit var stopAudio: StopAudio

    override fun onReceive(context: Context?, intent: Intent?) {
        AndroidInjection.inject(this, context)

        when (intent?.action) {
            Intent.ACTION_BOOT_COMPLETED, Intent.ACTION_TIME_CHANGED
                -> rescheduleAlarm.execute()
            ACTION_STOP_AUDIO
                -> stopAudio.execute()
        }
    }
}