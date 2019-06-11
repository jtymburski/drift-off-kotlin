package com.jordantymburski.driftoff.kotlin.service

import com.jordantymburski.driftoff.kotlin.domain.adapter.AudioController

/**
 * Custom audio control and management functionality
 */
class AndroidAudioController: AudioController {

    /**
     * Request focus on the audio layer. All listeners will stop playing
     */
    override fun requestFocus() {
        TODO("Android audio request focus")
    }
}