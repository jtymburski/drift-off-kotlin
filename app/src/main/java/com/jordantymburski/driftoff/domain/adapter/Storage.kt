package com.jordantymburski.driftoff.domain.adapter

import com.jordantymburski.driftoff.domain.model.AlarmInfo

interface Storage {
    fun load(): AlarmInfo
    fun save(info: AlarmInfo)
}