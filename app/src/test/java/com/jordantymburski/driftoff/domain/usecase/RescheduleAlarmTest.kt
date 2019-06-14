package com.jordantymburski.driftoff.domain.usecase

import com.jordantymburski.driftoff.domain.adapter.AlarmScheduler
import com.jordantymburski.driftoff.domain.model.AlarmInfo
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import java.util.*

class RescheduleAlarmTest {
    /**
     * Alarm scheduling port
     */
    @Mock private lateinit var alarmScheduler: AlarmScheduler

    /**
     * Get alarm info use case to fetch existing cached value
     */
    @Mock private lateinit var getInfo: GetInfo

    /**
     * Random number generator instance
     */
    private val random = Random()

    /**
     * Reschedule alarm use case. Test is built around this implementation
     */
    private lateinit var rescheduleAlarm: RescheduleAlarm

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)
        rescheduleAlarm = RescheduleAlarm(alarmScheduler, getInfo)
    }

    @Test
    fun active() {
        // Set up get info with an active alarm object
        val info = AlarmInfo(alarm = Date().time,
            timeHour = random.nextInt(24), timeMinute = random.nextInt(60))
        Mockito.`when`(getInfo.current()).thenReturn(info)

        // Trigger
        rescheduleAlarm.execute()

        // Check on the execution
        Mockito.verify(getInfo, Mockito.atLeastOnce()).current()
        Mockito.verifyNoMoreInteractions(getInfo)
        Mockito.verify(alarmScheduler).cancel()
        Mockito.verify(alarmScheduler).schedule(info.alarm)
        Mockito.verifyNoMoreInteractions(alarmScheduler)
    }

    @Test
    fun inactive() {
        // Set up get info with an inactive alarm
        val info = AlarmInfo(alarm = 0L,
            timeHour = random.nextInt(24), timeMinute = random.nextInt(60))
        Mockito.`when`(getInfo.current()).thenReturn(info)

        // Trigger
        rescheduleAlarm.execute()

        // Check on the execution
        Mockito.verify(getInfo, Mockito.atLeastOnce()).current()
        Mockito.verifyNoMoreInteractions(getInfo)
        Mockito.verifyZeroInteractions(alarmScheduler)
    }
}