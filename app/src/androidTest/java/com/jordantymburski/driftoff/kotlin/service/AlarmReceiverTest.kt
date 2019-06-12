package com.jordantymburski.driftoff.kotlin.service

import android.content.Intent
import android.content.IntentFilter
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.jordantymburski.driftoff.kotlin.common.ContextProvider
import com.jordantymburski.driftoff.kotlin.domain.MockDomain
import com.jordantymburski.driftoff.kotlin.domain.adapter.AudioController
import com.jordantymburski.driftoff.kotlin.domain.usecase.SetInfo
import com.jordantymburski.driftoff.kotlin.domain.usecase.StopAudio
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito

class AlarmReceiverTest {
    /**
     * The domain interception object
     */
    private lateinit var domain: MockDomain

    @Before
    fun setup() {
        domain = MockDomain()
    }

    @Test
    fun bootCompleted() {
        // System service connections
        val context = ContextProvider.get()

        // Register the receiver for local broadcasts
        val receiver = AlarmReceiver()
        val intentFilter = IntentFilter()
        intentFilter.addAction(Intent.ACTION_BOOT_COMPLETED)
        LocalBroadcastManager.getInstance(context).registerReceiver(receiver, intentFilter)

        // Make sure no calls have been made to the use cases
        domain.verifyZeroInteractions()

        // Broadcast to force a create with BOOT_COMPLETED
        LocalBroadcastManager.getInstance(context).sendBroadcast(Intent(Intent.ACTION_BOOT_COMPLETED))
        Thread.sleep(250)

        // Check that just the reschedule was called
        Mockito.verify(domain.rescheduleAlarm).execute()
        domain.verifyNoMoreInteractions()

        // Clean up
        LocalBroadcastManager.getInstance(context).unregisterReceiver(receiver)
    }

    @Test
    fun stopAudio() {
        // System service connections
        val context = ContextProvider.get()

        // Register the receiver for local broadcasts
        val receiver = AlarmReceiver()
        val intentFilter = IntentFilter()
        intentFilter.addAction(AlarmReceiver.ACTION_STOP_AUDIO)
        LocalBroadcastManager.getInstance(context).registerReceiver(receiver, intentFilter)

        // Make sure no calls have been made to the use cases
        domain.verifyZeroInteractions()

        // Broadcast to force a create with BOOT_COMPLETED
        LocalBroadcastManager.getInstance(context).sendBroadcast(Intent(AlarmReceiver.ACTION_STOP_AUDIO))
        Thread.sleep(250)

        // Check that just the reschedule was called
        Mockito.verify(domain.stopAudio).execute()
        domain.verifyNoMoreInteractions()

        // Clean up
        LocalBroadcastManager.getInstance(context).unregisterReceiver(receiver)
    }

    @Test
    fun timeChanged() {
        // System service connections
        val context = ContextProvider.get()

        // Register the receiver for local broadcasts
        val receiver = AlarmReceiver()
        val intentFilter = IntentFilter()
        intentFilter.addAction(Intent.ACTION_TIME_CHANGED)
        LocalBroadcastManager.getInstance(context).registerReceiver(receiver, intentFilter)

        // Make sure no calls have been made to the use cases
        domain.verifyZeroInteractions()

        // Broadcast to force a create with BOOT_COMPLETED
        LocalBroadcastManager.getInstance(context).sendBroadcast(Intent(Intent.ACTION_TIME_CHANGED))
        Thread.sleep(250)

        // Check that just the reschedule was called
        Mockito.verify(domain.rescheduleAlarm).execute()
        domain.verifyNoMoreInteractions()

        // Clean up
        LocalBroadcastManager.getInstance(context).unregisterReceiver(receiver)
    }
}