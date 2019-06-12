package com.jordantymburski.driftoff.kotlin.domain.usecase

import com.jordantymburski.driftoff.kotlin.domain.OpenForTest
import com.jordantymburski.driftoff.kotlin.domain.adapter.AlarmScheduler
import com.jordantymburski.driftoff.kotlin.domain.adapter.Storage
import com.jordantymburski.driftoff.kotlin.domain.model.AlarmInfo
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Use case to set and update the persisted alarm information
 * @param alarmScheduler manages alarm job scheduling
 * @param getInfo use case to get the current alarm information
 * @param storage persisted storage implementation
 */
@Singleton @OpenForTest
class SetInfo @Inject constructor(
    private val alarmScheduler: AlarmScheduler,
    private val getInfo: GetInfo,
    private val storage: Storage
) {
    /**
     * Update the alarm object in the persisted storage and for any active observers
     * @param info the new alarm info object
     */
    private fun update(info: AlarmInfo) {
        if (info != getInfo.current()) {
            getInfo.post(info)
            storage.save(info)
        }
    }

    /**
     * Reset the alarm (unset). Called when the alarm either goes off or is cancelled
     * This runs on a separate thread
     */
    fun resetAlarm() {
        GlobalScope.launch {
            alarmScheduler.cancel()
            update(getInfo.current().copy(alarm = 0L))
        }
    }

    /**
     * Set the alarm to the current time setpoint value
     * This runs on a separate thread
     */
    fun setAlarm() {
        GlobalScope.launch {
            val currentInfo = getInfo.current()
            val alarmTime = currentInfo.getTimeInMillis()

            alarmScheduler.schedule(alarmTime)
            update(currentInfo.copy(alarm = alarmTime))
        }
    }

    /**
     * Set the time setpoint
     * This runs on a separate thread
     * @param hour 0-23 hour setpoint
     * @param minute minute setpoint
     */
    fun setTime(hour: Int, minute: Int) {
        GlobalScope.launch {
            update(getInfo.current().copy(timeHour = hour, timeMinute = minute))
        }
    }
}