package com.jordantymburski.driftoff.kotlin.service

import com.jordantymburski.driftoff.kotlin.common.ContextProvider
import com.jordantymburski.driftoff.kotlin.common.FakeAudioFocus
import com.jordantymburski.driftoff.kotlin.common.ServiceProvider
import org.junit.Assert.*
import org.junit.Test

class AndroidAudioControllerTest {
    @Test
    fun requestFocus() {
        val audioManager = ServiceProvider.audioManager(ContextProvider.get())
        val fakeFocus = FakeAudioFocus(audioManager)
        fakeFocus.request()

        // Now run the controller service implementation and make sure focus is lost on the fake stream
        TODO("Make request to audio controller service")
        fakeFocus.waitForChange()

        // Check the focus change
        assertTrue(fakeFocus.isLost())

        // Release any focus resources
        fakeFocus.abandon()
    }
}