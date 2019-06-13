package com.jordantymburski.driftoff.kotlin.common

import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit

class ResettableCountDownLatch(
    private val initialCount: Int
) {
    @Volatile private lateinit var latch: CountDownLatch

    init {
        reset()
    }

    fun await(timeout: Long, unit: TimeUnit): Boolean {
        return latch.await(timeout, unit)
    }

    fun countDown() {
        latch.countDown()
    }

    fun reset() {
        latch = CountDownLatch(initialCount)
    }
}