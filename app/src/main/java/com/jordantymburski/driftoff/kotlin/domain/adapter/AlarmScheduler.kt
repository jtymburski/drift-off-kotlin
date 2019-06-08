package com.jordantymburski.driftoff.kotlin.domain.adapter

interface AlarmScheduler {
    fun cancel()
    fun schedule(time: Long)
}