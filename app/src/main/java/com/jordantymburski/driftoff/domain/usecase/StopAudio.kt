package com.jordantymburski.driftoff.domain.usecase

import com.jordantymburski.driftoff.domain.OpenForTest
import com.jordantymburski.driftoff.domain.adapter.AudioController
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Use case to stop playing audio
 * @param audioController Audio managing controller port
 * @param setInfo Set alarm info use case to modify existing persisted values
 */
@Singleton @OpenForTest
class StopAudio @Inject constructor(
    private val audioController: AudioController,
    private val setInfo: SetInfo
) {
    fun execute() {
        audioController.requestFocus()
        setInfo.resetAlarm()
    }
}