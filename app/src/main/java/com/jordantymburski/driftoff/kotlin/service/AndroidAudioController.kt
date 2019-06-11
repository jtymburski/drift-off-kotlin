package com.jordantymburski.driftoff.kotlin.service

import android.annotation.TargetApi
import android.media.AudioAttributes
import android.media.AudioFocusRequest
import android.media.AudioManager
import android.os.Build
import com.jordantymburski.driftoff.kotlin.domain.adapter.AudioController

/**
 * Custom audio control and management functionality
 * @param audioManager audio manager system service interface
 */
class AndroidAudioController(
    private val audioManager: AudioManager
): AudioController {
    /**
     * Request focus on the audio layer
     * This functionality is now legacy as of Android O
     */
    private fun requestFocusLegacy() {
        audioManager.requestAudioFocus(null, AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN)
    }

    /**
     * Request focus on the audio layer
     * This functionality is only valid on Android O+
     */
    @TargetApi(Build.VERSION_CODES.O)
    private fun requestFocusNew() {
        val attributes = AudioAttributes.Builder()
            .setUsage(AudioAttributes.USAGE_MEDIA)
            .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
            .build()

        val focusRequest = AudioFocusRequest.Builder(AudioManager.AUDIOFOCUS_GAIN)
            .setAudioAttributes(attributes)
            .build()

        audioManager.requestAudioFocus(focusRequest)
    }

    /**
     * Request focus on the audio layer. All listeners will stop playing
     */
    override fun requestFocus() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
            requestFocusNew()
        else
            requestFocusLegacy()
    }
}