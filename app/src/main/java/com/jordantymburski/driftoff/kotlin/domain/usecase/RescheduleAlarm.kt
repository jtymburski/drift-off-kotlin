package com.jordantymburski.driftoff.kotlin.domain.usecase

import com.jordantymburski.driftoff.kotlin.domain.OpenForTest
import com.jordantymburski.driftoff.kotlin.domain.adapter.AlarmScheduler
import javax.inject.Inject
import javax.inject.Singleton

@Singleton @OpenForTest
class RescheduleAlarm @Inject constructor(
    private val alarmScheduler: AlarmScheduler,
    private val getInfo: GetInfo
) {
    /**
     * Execute a reschedule if there is an existing alarm. If the time has past, it will trigger
     * the job immediately
     */
    fun execute() {
        val info = getInfo.current()
        if (info.alarm > 0) {
            alarmScheduler.cancel()
            alarmScheduler.schedule(info.alarm)
        }
    }
}