package com.jordantymburski.driftoff.kotlin.domain.usecase

import com.jordantymburski.driftoff.kotlin.domain.adapter.AlarmScheduler
import com.jordantymburski.driftoff.kotlin.domain.adapter.Storage
import com.jordantymburski.driftoff.kotlin.domain.model.AlarmInfo
import org.junit.Assert.assertNotEquals
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import java.util.Random

class SetInfoTest {
    /**
     * Alarm scheduler mock class
     */
    @Mock private lateinit var alarmScheduler: AlarmScheduler

    /**
     * Get info mock class
     */
    @Mock private lateinit var getInfo: GetInfo

    /**
     * Initialized info
     */
    private lateinit var initInfo: AlarmInfo

    /**
     * Random number generator instance
     */
    private val random = Random()

    /**
     * Set info use case
     */
    private lateinit var setInfo: SetInfo

    /**
     * Storage mock class
     */
    @Mock private lateinit var storage: Storage

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)
        setInitInfo(AlarmInfo(alarm = 0L,
            timeHour = random.nextInt(24), timeMinute = random.nextInt(60)))
        setInfo = SetInfo(alarmScheduler, getInfo, storage)
    }

    @Test
    fun resetAlarm() {
        // Swap the default set-up for one that already has an alarm ready
        val noAlarmInfo = initInfo.copy()
        setInitInfo(initInfo.copy(alarm = initInfo.getTimeInMillis()))

        // Reset it
        setInfo.resetAlarm()
        Thread.sleep(250)

        // Check
        Mockito.verify(alarmScheduler).cancel()
        Mockito.verifyNoMoreInteractions(alarmScheduler)

        Mockito.verify(getInfo, Mockito.atLeastOnce()).current()
        Mockito.verify(getInfo).post(noAlarmInfo)
        Mockito.verifyNoMoreInteractions(getInfo)

        Mockito.verify(storage).save(noAlarmInfo)
        Mockito.verifyNoMoreInteractions(storage)
    }

    @Test
    fun setAlarm() {
        // Set the alarm
        setInfo.setAlarm()

        // Generate the expected info and wait
        val freshAlarmInfo = initInfo.copy(alarm = initInfo.getTimeInMillis())
        Thread.sleep(250)

        // Check
        Mockito.verify(alarmScheduler).schedule(freshAlarmInfo.alarm)
        Mockito.verifyNoMoreInteractions(alarmScheduler)

        Mockito.verify(getInfo, Mockito.atLeastOnce()).current()
        Mockito.verify(getInfo).post(freshAlarmInfo)
        Mockito.verifyNoMoreInteractions(getInfo)

        Mockito.verify(storage).save(freshAlarmInfo)
        Mockito.verifyNoMoreInteractions(storage)
    }

    @Test
    fun setTime() {
        // Set a modified time setpoint
        val newInfo = initInfo.copy(timeHour = random.nextInt(24), timeMinute = random.nextInt(60))
        setInfo.setTime(hour = newInfo.timeHour, minute = newInfo.timeMinute)
        Thread.sleep(250)

        // Check it
        assertNotEquals(initInfo, newInfo)
        Mockito.verifyZeroInteractions(alarmScheduler)
        Mockito.verify(getInfo, Mockito.atLeastOnce()).current()
        Mockito.verify(getInfo).post(newInfo)
        Mockito.verifyNoMoreInteractions(getInfo)
        Mockito.verify(storage).save(newInfo)
        Mockito.verifyNoMoreInteractions(storage)

        // Set another time setpoint
        val newInfo2 = newInfo.copy(timeHour = random.nextInt(24), timeMinute = random.nextInt(60))
        setInfo.setTime(hour = newInfo2.timeHour, minute = newInfo2.timeMinute)
        Thread.sleep(250)

        // Check again
        assertNotEquals(newInfo, newInfo2)
        Mockito.verifyZeroInteractions(alarmScheduler)
        Mockito.verify(getInfo, Mockito.atLeastOnce()).current()
        Mockito.verify(getInfo).post(newInfo2)
        Mockito.verifyNoMoreInteractions(getInfo)
        Mockito.verify(storage).save(newInfo2)
        Mockito.verifyNoMoreInteractions(storage)
    }

    /**
     * Sets the initialized alarm object
     * @param alarmInfo custom alarm info
     */
    private fun setInitInfo(info: AlarmInfo) {
        initInfo = info
        Mockito.`when`(getInfo.current()).thenReturn(initInfo)
        Mockito.`when`(storage.load()).thenReturn(initInfo)
    }
}