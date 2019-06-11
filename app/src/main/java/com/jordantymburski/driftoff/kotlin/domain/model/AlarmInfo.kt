package com.jordantymburski.driftoff.kotlin.domain.model

import java.util.Calendar

/**
 * Current alarm information including active requested alarms and set visible time points
 * @param alarm unix epoch value of the last requested alarm
 * @param timeHour 24 hour clock value of the set display time (0-23)
 * @param timeMinute minute clock value of the set display time (0-59)
 */
data class AlarmInfo(
    val alarm: Long = 0L,
    val timeHour: Int = 21,
    val timeMinute: Int = 30
) {
    /**
     * Calculates the hours till the alarm will trigger. This is rounded up:
     * 1 to 60 minutes = 1 hour, 61 to 120 minutes  = 2 hours, etc
     * @return in hours
     */
    fun getHoursTillAlarm(): Long {
        TODO("Alarm info get hours till alarm")
    }

    /**
     * Calculates the minutes till the alarm will trigger. This is rounded up:
     * 1 to 60 seconds = 1 minute, 61 to 120 seconds = 2 minutes, etc
     * @return in minutes
     */
    fun getMinutesTillAlarm(): Long {
        TODO("Alarm info get minutes till alarm")
    }

    /**
     * Assembles the time setpoint
     * @return a calendar object
     */
    fun getTime(): Calendar {
        val c = Calendar.getInstance()
        c.set(Calendar.HOUR_OF_DAY, timeHour)
        c.set(Calendar.MINUTE, timeMinute)
        c.set(Calendar.SECOND, 0)
        c.set(Calendar.MILLISECOND, 0)
        if (c.timeInMillis <= System.currentTimeMillis()) {
            c.add(Calendar.DAY_OF_MONTH, 1)
        }
        return c
    }

    /**
     * Calculates the unix epoch time that matches the time setpoint
     * @return in milliseconds
     */
    fun getTimeInMillis(): Long {
        return getTime().timeInMillis
    }

    /**
     * Is the alarm active and waiting to trigger to stop any playing music?
     * @return TRUE if alarm is active. FALSE if off
     */
    fun isActive(): Boolean {
        TODO("Alarm info is active")
    }
}
