package com.jordantymburski.driftoff.kotlin.domain.usecase

import com.jordantymburski.driftoff.kotlin.domain.adapter.AudioController
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations

class StopAudioTest {
    /**
     * Audio controller mock class
     */
    @Mock private lateinit var audioController: AudioController

    /**
     * Set info mock class
     */
    @Mock private lateinit var setInfo: SetInfo

    /**
     * Stop audio use case for the core of the test
     */
    private lateinit var stopAudio: StopAudio

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)
        stopAudio = StopAudio(audioController, setInfo)
    }

    @Test
    fun execute() {
        stopAudio.execute()

        Mockito.verify(audioController).requestFocus()
        Mockito.verifyNoMoreInteractions(audioController)
        Mockito.verify(setInfo).resetAlarm()
        Mockito.verifyNoMoreInteractions(setInfo)
    }
}