package com.jordantymburski.driftoff.kotlin.domain.model

import org.junit.Assert.*
import org.junit.Test
import java.util.*
import java.util.concurrent.TimeUnit

class AlarmInfoTest {
    @Test
    fun constructorSimple() {
        val alarmTime = Date().time
        val timeHour = 14
        val timeMinute = 45
        val info = AlarmInfo(alarmTime, timeHour, timeMinute)
        assertEquals(alarmTime, info.alarm)
        assertEquals(timeHour, info.timeHour)
        assertEquals(timeMinute, info.timeMinute)

        val alarmTime2 = 0L
        val timeHour2 = 0
        val timeMinute2 = 0
        val info2 = AlarmInfo(alarmTime2, timeHour2, timeMinute2)
        assertEquals(alarmTime2, info2.alarm)
        assertEquals(timeHour2, info2.timeHour)
        assertEquals(timeMinute2, info2.timeMinute)
    }

    @Test
    fun constructorModifyAlarm() {
        val baseInfo = AlarmInfo(Date().time, 8, 21)

        val modifiedAlarm = 40000L
        val modifiedInfo = baseInfo.copy(alarm = modifiedAlarm)
        assertEquals(modifiedAlarm, modifiedInfo.alarm)
        assertEquals(baseInfo.timeHour, modifiedInfo.timeHour)
        assertEquals(baseInfo.timeMinute, modifiedInfo.timeMinute)

        val modifiedAlarm2 = 0L
        val modifiedInfo2 = baseInfo.copy(alarm = modifiedAlarm2)
        assertEquals(modifiedAlarm2, modifiedInfo2.alarm)
        assertEquals(baseInfo.timeHour, modifiedInfo2.timeHour)
        assertEquals(baseInfo.timeMinute, modifiedInfo2.timeMinute)
    }

    @Test
    fun constructorModifyTime() {
        val baseInfo = AlarmInfo(Date().time, 21, 8)

        val modifiedTimeHour = 4
        val modifiedTimeMinute = 59
        val modifiedInfo = baseInfo.copy(timeHour = modifiedTimeHour, timeMinute = modifiedTimeMinute)
        assertEquals(baseInfo.alarm, modifiedInfo.alarm)
        assertEquals(modifiedTimeHour, modifiedInfo.timeHour)
        assertEquals(modifiedTimeMinute, modifiedInfo.timeMinute)

        val modifiedTimeHour2 = baseInfo.timeHour - 2
        val modifiedTimeMinute2 = baseInfo.timeMinute + 4
        val modifiedInfo2 = baseInfo.copy(timeHour = modifiedTimeHour2, timeMinute = modifiedTimeMinute2)
        assertEquals(baseInfo.alarm, modifiedInfo2.alarm)
        assertEquals(modifiedTimeHour2, modifiedInfo2.timeHour)
        assertEquals(modifiedTimeMinute2, modifiedInfo2.timeMinute)
    }

    @Test
    fun objectCompare() {
        val alarmTime = Date().time
        val timeHour = 7
        val timeMinute = 34

        val info1 = AlarmInfo(alarmTime, timeHour, timeMinute)
        val info2 = AlarmInfo(alarmTime, timeHour, timeMinute)
        assertEquals(info1, info2)

        // Mod just the alarm value
        val infoModAlarm = AlarmInfo(alarmTime + 1, timeHour, timeMinute)
        assertNotEquals(info1, infoModAlarm)

        // Mod just the time hour value
        val infoModTimeHour = AlarmInfo(alarmTime, timeHour + 1, timeMinute)
        assertNotEquals(info1, infoModTimeHour)

        // Mod just the time minute value
        val infoModTimeMinute = AlarmInfo(alarmTime, timeHour, timeMinute + 1)
        assertNotEquals(info1, infoModTimeMinute)
    }

    @Test
    fun getHoursTillAlarm() {
        // Now
        val nowInfo = AlarmInfo(Date().time, 15, 14)
        assertEquals(0L, nowInfo.getHoursTillAlarm())

        // Just past
        val pastInfo = nowInfo.copy(alarm = Date().time - TimeUnit.MINUTES.toMillis(1))
        assertEquals(0L, pastInfo.getHoursTillAlarm())

        // Almost here
        val almostInfo = nowInfo.copy(alarm = Date().time + TimeUnit.MINUTES.toMillis(1))
        assertEquals(1L, almostInfo.getHoursTillAlarm())

        // Exactly 1 hour
        val exactlyHourInfo = nowInfo.copy(alarm = Date().time + TimeUnit.HOURS.toMillis(1))
        assertEquals(1L, exactlyHourInfo.getHoursTillAlarm())

        // Just over 1 hour
        val overHourInfo = nowInfo.copy(alarm = Date().time + TimeUnit.HOURS.toMillis(1) + 1)
        assertEquals(2L, overHourInfo.getHoursTillAlarm())

        // Much larger value
        val farAwayInfo = nowInfo.copy(alarm = Date().time + TimeUnit.HOURS.toMillis(24))
        assertEquals(24L, farAwayInfo.getHoursTillAlarm())
    }

    @Test
    fun getMinutesTillAlarm() {
        // Now
        val nowInfo = AlarmInfo(Date().time, 15, 14)
        assertEquals(0L, nowInfo.getMinutesTillAlarm())

        // Just past
        val pastInfo = nowInfo.copy(alarm = Date().time - TimeUnit.SECONDS.toMillis(1))
        assertEquals(0L, pastInfo.getMinutesTillAlarm())

        // More than just past
        val pastInfoMore = nowInfo.copy(alarm = Date().time - TimeUnit.MINUTES.toMillis(1))
        assertEquals(0L, pastInfoMore.getMinutesTillAlarm())

        // Almost here
        val almostInfo = nowInfo.copy(alarm = Date().time + TimeUnit.SECONDS.toMillis(1))
        assertEquals(1L, almostInfo.getMinutesTillAlarm())

        // Exactly 1 minute
        val exactlyMinInfo = nowInfo.copy(alarm = Date().time + TimeUnit.MINUTES.toMillis(1))
        assertEquals(1L, exactlyMinInfo.getMinutesTillAlarm())

        // Just over 1 minute
        val overMinInfo = nowInfo.copy(alarm = Date().time + TimeUnit.MINUTES.toMillis(1) + 1)
        assertEquals(2L, overMinInfo.getMinutesTillAlarm())

        // Much larger value
        val farAwayInfo = nowInfo.copy(alarm = Date().time + TimeUnit.MINUTES.toMillis(60))
        assertEquals(60L, farAwayInfo.getMinutesTillAlarm())
    }

    @Test
    fun getTime() {
        // Just a random value
        val randomHour = 13
        val randomMinute = 49
        val randomInfo = AlarmInfo(0L, randomHour, randomMinute)
        val randomTime = randomInfo.getTime()
        assertEquals(randomHour, randomTime.get(Calendar.HOUR_OF_DAY))
        assertEquals(randomMinute, randomTime.get(Calendar.MINUTE))
        assertEquals(0, randomTime.get(Calendar.SECOND))
        assertEquals(0, randomTime.get(Calendar.MILLISECOND))

        // 1 minute ahead of now
        val plusOneTime = Calendar.getInstance()
        plusOneTime.add(Calendar.MINUTE, 1)
        val plusOneInfo = AlarmInfo(0L, plusOneTime.get(Calendar.HOUR_OF_DAY), plusOneTime.get(Calendar.MINUTE))
        val plusOneTimeGen = plusOneInfo.getTime()
        assertTrue(plusOneTimeGen.timeInMillis > Calendar.getInstance().timeInMillis)
        assertEquals(plusOneTime.get(Calendar.HOUR_OF_DAY), plusOneTimeGen.get(Calendar.HOUR_OF_DAY))
        assertEquals(plusOneTime.get(Calendar.MINUTE), plusOneTimeGen.get(Calendar.MINUTE))
        assertEquals(0, plusOneTimeGen.get(Calendar.SECOND))
        assertEquals(0, plusOneTimeGen.get(Calendar.MILLISECOND))

        // 1 minute behind now
        val minusOneTime = Calendar.getInstance()
        minusOneTime.add(Calendar.MINUTE, -1)
        val minusOneInfo = AlarmInfo(0L,
            minusOneTime.get(Calendar.HOUR_OF_DAY), minusOneTime.get(Calendar.MINUTE))
        val minusOneTimeGen = minusOneInfo.getTime()
        assertTrue(minusOneTimeGen.timeInMillis > Calendar.getInstance().timeInMillis)
        assertEquals(minusOneTime.get(Calendar.HOUR_OF_DAY), minusOneTimeGen.get(Calendar.HOUR_OF_DAY))
        assertEquals(minusOneTime.get(Calendar.MINUTE), minusOneTimeGen.get(Calendar.MINUTE))
        assertEquals(0, minusOneTimeGen.get(Calendar.SECOND))
        assertEquals(0, minusOneTimeGen.get(Calendar.MILLISECOND))
    }

    @Test
    fun getTimeInMillis() {
        val randomHour = 13
        val randomMinute = 49
        val randomInfo = AlarmInfo(0L, randomHour, randomMinute)
        val randomTimeCreated = Calendar.getInstance()
        randomTimeCreated.timeInMillis = randomInfo.getTimeInMillis()
        assertEquals(randomHour, randomTimeCreated.get(Calendar.HOUR_OF_DAY))
        assertEquals(randomMinute, randomTimeCreated.get(Calendar.MINUTE))

        // 1 minute ahead of now
        val plusOneTime = Calendar.getInstance()
        plusOneTime.add(Calendar.MINUTE, 1)
        val plusOneInfo = AlarmInfo(0L, plusOneTime.get(Calendar.HOUR_OF_DAY), plusOneTime.get(Calendar.MINUTE))
        val plusOneTimeCreated = Calendar.getInstance()
        plusOneTimeCreated.timeInMillis = plusOneInfo.getTimeInMillis()
        assertEquals(plusOneTime.get(Calendar.HOUR_OF_DAY), plusOneTimeCreated.get(Calendar.HOUR_OF_DAY))
        assertEquals(plusOneTime.get(Calendar.MINUTE), plusOneTimeCreated.get(Calendar.MINUTE))

        // 1 minute behind now
        val minusOneTime = Calendar.getInstance()
        minusOneTime.add(Calendar.MINUTE, -1)
        val minusOneInfo = AlarmInfo(0L,
            minusOneTime.get(Calendar.HOUR_OF_DAY), minusOneTime.get(Calendar.MINUTE))
        val minusOneTimeCreated = Calendar.getInstance()
        minusOneTimeCreated.timeInMillis = minusOneInfo.getTimeInMillis()
        assertEquals(minusOneTime.get(Calendar.HOUR_OF_DAY), minusOneTimeCreated.get(Calendar.HOUR_OF_DAY))
        assertEquals(minusOneTime.get(Calendar.MINUTE), minusOneTimeCreated.get(Calendar.MINUTE))
    }

    @Test
    fun isActive() {
        // Reset
        val resetInfo = AlarmInfo(0L, 14, 44)
        assertFalse(resetInfo.isActive())

        // 1 minute ahead of now
        val plusOneTime = Calendar.getInstance()
        plusOneTime.add(Calendar.MINUTE, 1)
        val plusOneInfo = AlarmInfo(plusOneTime.timeInMillis,
            plusOneTime.get(Calendar.HOUR_OF_DAY), plusOneTime.get(Calendar.MINUTE))
        assertTrue(plusOneInfo.isActive())

        // 1 minute behind now
        val minusOneTime = Calendar.getInstance()
        minusOneTime.add(Calendar.MINUTE, -1)
        val minusOneInfo = AlarmInfo(minusOneTime.timeInMillis,
            minusOneTime.get(Calendar.HOUR_OF_DAY), minusOneTime.get(Calendar.MINUTE))
        assertFalse(minusOneInfo.isActive())
    }
}