package com.jordantymburski.driftoff.kotlin.data

import com.jordantymburski.driftoff.kotlin.domain.adapter.Storage
import com.jordantymburski.driftoff.kotlin.domain.model.AlarmInfo

class PreferenceStorage: Storage {
    override fun load(): AlarmInfo {
        return AlarmInfo()
    }

    override fun save(info: AlarmInfo) {
    }
}