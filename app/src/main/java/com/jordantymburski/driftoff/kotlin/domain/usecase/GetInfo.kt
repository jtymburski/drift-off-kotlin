package com.jordantymburski.driftoff.kotlin.domain.usecase

import androidx.lifecycle.MutableLiveData
import com.jordantymburski.driftoff.kotlin.domain.adapter.Storage
import com.jordantymburski.driftoff.kotlin.domain.model.AlarmInfo
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Use case to get the current alarm information and observe for changes
 * @param storage connection to the storage layer. Used to fetch current persisted state
 */
@Singleton
class GetInfo @Inject constructor(
    private val storage: Storage
) {
    /**
     * Observable wrapper that interfaces can monitor for changes
     */
    val observable = MutableLiveData<AlarmInfo>()

    init {
        GlobalScope.launch { observable.postValue(storage.load()) }
    }
}