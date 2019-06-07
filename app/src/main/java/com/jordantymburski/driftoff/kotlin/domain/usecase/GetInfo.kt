package com.jordantymburski.driftoff.kotlin.domain.usecase

import androidx.lifecycle.MutableLiveData
import com.jordantymburski.driftoff.kotlin.domain.model.AlarmInfo
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Use case to get the current alarm information and observe for changes
 */
@Singleton
class GetInfo @Inject constructor() {
    /**
     * Observable wrapper that interfaces can monitor for changes
     */
    val observable = MutableLiveData<AlarmInfo>()

    init {
        observable.postValue(AlarmInfo())
    }
}