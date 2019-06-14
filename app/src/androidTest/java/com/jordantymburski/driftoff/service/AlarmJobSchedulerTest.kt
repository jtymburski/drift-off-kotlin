package com.jordantymburski.driftoff.service

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import com.jordantymburski.driftoff.common.ContextProvider
import com.jordantymburski.driftoff.common.ServiceProvider
import org.junit.After
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit
import java.util.Date

class AlarmJobSchedulerTest {
    companion object {
        private const val ACTION_SIMULATED = "com.jordantymburski.driftoff.test.SIMULATED"
    }

    /**
     * The alarm scheduler instance to test with
     */
    private lateinit var alarmScheduler: AlarmJobScheduler

    /**
     * Count down latch for synchronization
     */
    private lateinit var lock: CountDownLatch

    /**
     * Internal custom receiver
     */
    private val customReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            if (intent.action == ACTION_SIMULATED) {
                lock.countDown()
            }
        }
    }

    @Before
    fun setup() {
        val context = ContextProvider.get()
        val intentFilter = IntentFilter()
        intentFilter.addAction(ACTION_SIMULATED)
        context.registerReceiver(customReceiver, intentFilter)

        alarmScheduler = AlarmJobScheduler(context = context, alarmManager = ServiceProvider.alarmManager(context),
            broadcastIntent = Intent(ACTION_SIMULATED))

        lock = CountDownLatch(1)
    }

    @After
    fun cleanup() {
        ContextProvider.get().unregisterReceiver(customReceiver)
    }

    @Test
    fun cancel() {
        // Schedule it in a few seconds
        alarmScheduler.schedule(Date().time + TimeUnit.SECONDS.toMillis(3))

        // Sleep for a bit (less than the alarm above)
        Thread.sleep(1500)

        // Cancel it
        alarmScheduler.cancel()

        // Wait for the broadcast (should never happen)
        assertFalse(lock.await(8, TimeUnit.SECONDS))
    }

    @Test
    fun schedule() {
        // Schedule
        alarmScheduler.schedule(Date().time)

        // Wait for the broadcast
        assertTrue(lock.await(8, TimeUnit.SECONDS))
    }

    @Test
    fun reschedule() {
        // Schedule far away
        alarmScheduler.schedule(Date().time + TimeUnit.HOURS.toMillis(1))

        // Wait for a bit
        assertFalse(lock.await(5, TimeUnit.SECONDS))

        // Re-schedule it for now
        schedule()
    }
}