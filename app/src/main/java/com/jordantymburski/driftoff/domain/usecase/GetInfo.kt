package com.jordantymburski.driftoff.domain.usecase

import androidx.lifecycle.MutableLiveData
import com.jordantymburski.driftoff.domain.OpenForTest
import com.jordantymburski.driftoff.domain.adapter.Storage
import com.jordantymburski.driftoff.domain.model.AlarmInfo
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Use case to get the current alarm information and observe for changes
 * @param storage connection to the storage layer. Used to fetch current persisted state
 */
@Singleton @OpenForTest
class GetInfo @Inject constructor(
    private val storage: Storage
) {
    /**
     * Observable wrapper that interfaces can monitor for changes
     */
    val observable = MutableLiveData<AlarmInfo>()

    /**
     * Initialization. Loads the current info asynchronously for any observers
     */
    init {
        GlobalScope.launch { observable.postValue(storage.load()) }
    }

    /**
     * Fetches the current info object
     * @return alarm details
     */
    internal fun current() : AlarmInfo {
        return observable.value ?: storage.load()
    }

    /**
     * Posts to any active observer the newly provided info
     * @param info new info object
     */
    internal fun post(info: AlarmInfo) {
        observable.postValue(info)
    }
}