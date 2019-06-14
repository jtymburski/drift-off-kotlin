package com.jordantymburski.driftoff.service

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import com.jordantymburski.driftoff.domain.adapter.AlarmScheduler

/**
 * Manage alarm job scheduling
 * @param alarmManager system alarm manager service interface
 * @param broadcastIntent the intent to locally broadcast towards the AlarmReceiver
 * @param context android application context
 */
class AlarmJobScheduler(
    private val alarmManager: AlarmManager,
    broadcastIntent: Intent,
    context: Context
): AlarmScheduler {
    /**
     * The pending alarm intent when the alarm fires
     */
    private val alarmIntent = PendingIntent.getBroadcast(context, 0, broadcastIntent, 0)

    /**
     * Cancel any active alarm job
     */
    override fun cancel() {
        alarmManager.cancel(alarmIntent)
    }

    /**
     * Schedule a job at the given time
     * @param time epoch unix time
     */
    override fun schedule(time: Long) {
        alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, time, alarmIntent)
    }
}