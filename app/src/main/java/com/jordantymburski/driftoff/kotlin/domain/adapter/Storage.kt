package com.jordantymburski.driftoff.kotlin.domain.adapter

import com.jordantymburski.driftoff.kotlin.domain.model.AlarmInfo

interface Storage {
    fun load(): AlarmInfo
    fun save(info: AlarmInfo)
}