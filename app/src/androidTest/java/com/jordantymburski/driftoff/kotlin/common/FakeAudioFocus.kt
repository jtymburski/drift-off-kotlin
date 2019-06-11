package com.jordantymburski.driftoff.kotlin.common

import android.annotation.TargetApi
import android.media.AudioAttributes
import android.media.AudioFocusRequest
import android.media.AudioManager
import android.os.Build
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit

/**
 * Simulates a fake audio focus request to act as a music player
 * @param audioManager Audio manager service interface
 */
class FakeAudioFocus(
    private val audioManager: AudioManager
): AudioManager.OnAudioFocusChangeListener {
    /**
     * The focus request for the fake stream
     */
    private var focusRequest: AudioFocusRequest? = null;

    /**
     * Last focus change
     */
    private var lastFocusChange = AudioManager.AUDIOFOCUS_NONE

    /**
     * Lock for synchronization to wait for a response
     */
    private val lock = CountDownLatch(1)

    /* ----------------------------------------------
     * PRIVATE FUNCTIONS
     * ---------------------------------------------- */

    @TargetApi(Build.VERSION_CODES.O)
    private fun abandonNew() {
        if (focusRequest != null) {
            audioManager.abandonAudioFocusRequest(focusRequest!!)
        }
    }

    private fun requestLegacy() {
        audioManager.requestAudioFocus(this, AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN)
    }

    @TargetApi(Build.VERSION_CODES.O)
    private fun requestNew() {
        if (focusRequest == null) {
            val attributes = AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_MEDIA)
                .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                .build()

            focusRequest = AudioFocusRequest.Builder(AudioManager.AUDIOFOCUS_GAIN)
                .setAudioAttributes(attributes)
                .setOnAudioFocusChangeListener(this)
                .build()
        }
        audioManager.requestAudioFocus(focusRequest!!)
    }

    /* ----------------------------------------------
     * OnAudioFocusChangeListener OVERRIDES
     * ---------------------------------------------- */

    override fun onAudioFocusChange(focusChange: Int) {
        lastFocusChange = focusChange
        lock.countDown()
    }

    /* ----------------------------------------------
     * PUBLIC FUNCTIONS
     * ---------------------------------------------- */

    fun abandon() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            abandonNew()
        }
        lastFocusChange = AudioManager.AUDIOFOCUS_NONE
    }

    fun isLost(): Boolean {
        return lastFocusChange == AudioManager.AUDIOFOCUS_LOSS
    }

    fun request() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
            requestNew()
        else
            requestLegacy()
    }

    fun waitForChange() {
        try {
            lock.await(5000, TimeUnit.MILLISECONDS)
        } catch (ie: InterruptedException) {}
    }
}