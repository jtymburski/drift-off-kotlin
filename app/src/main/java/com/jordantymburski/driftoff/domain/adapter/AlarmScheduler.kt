package com.jordantymburski.driftoff.domain.adapter

interface AlarmScheduler {
    fun cancel()
    fun schedule(time: Long)
}