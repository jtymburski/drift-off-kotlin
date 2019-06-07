package com.jordantymburski.driftoff.kotlin.presentation

import androidx.lifecycle.*
import com.jordantymburski.driftoff.kotlin.domain.model.AlarmInfo
import com.jordantymburski.driftoff.kotlin.domain.usecase.GetInfo

/**
 * View model manager of the home activity
 * @param getInfo use case to get the current alarm information
 */
class HomeViewModel(private val getInfo: GetInfo) : ViewModel() {
    /**
     * Fetches the info observable for usage by the holding activity
     * @return life-cycle aware observable
     */
    fun observable(): LiveData<AlarmInfo> {
        return getInfo.observable
    }

    companion object {
        /**
         * Static fetch to create an instance of this class using the factory
         * @param owner the activity owner that will hold the view model
         * @param factory the creator that can generate an instance of this home view model
         */
        fun getInstance(owner: ViewModelStoreOwner, factory: ViewModelProvider.Factory) : HomeViewModel {
            return ViewModelProvider(owner, factory).get(HomeViewModel::class.java)
        }
    }
}