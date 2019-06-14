package com.jordantymburski.driftoff.service

import com.jordantymburski.driftoff.common.ContextProvider
import com.jordantymburski.driftoff.common.FakeAudioFocus
import com.jordantymburski.driftoff.common.ServiceProvider
import org.junit.Assert.*
import org.junit.Test

class AndroidAudioControllerTest {
    @Test
    fun requestFocus() {
        val audioManager = ServiceProvider.audioManager(ContextProvider.get())
        val fakeFocus = FakeAudioFocus(audioManager)
        fakeFocus.request()

        // Now run the controller service implementation and make sure focus is lost on the fake stream
        AndroidAudioController(audioManager).requestFocus()
        fakeFocus.waitForChange()

        // Check the focus change
        assertTrue(fakeFocus.isLost())

        // Release any focus resources
        fakeFocus.abandon()
    }
}