package com.jordantymburski.driftoff.kotlin.presentation

import androidx.lifecycle.*
import com.jordantymburski.driftoff.kotlin.domain.model.AlarmInfo
import com.jordantymburski.driftoff.kotlin.domain.usecase.GetInfo
import com.jordantymburski.driftoff.kotlin.domain.usecase.SetInfo

/**
 * View model manager of the home activity
 * @param getInfo use case to get the current alarm information
 */
class HomeViewModel(
    private val getInfo: GetInfo,
    private val setInfo: SetInfo
) : ViewModel() {
    /**
     * Fetches the info observable for usage by the holding activity
     * @return life-cycle aware observable
     */
    internal fun observable(): LiveData<AlarmInfo> {
        return getInfo.observable
    }

    /**
     * Reset the alarm (unset). Called when the alarm either goes off or is cancelled
     */
    internal fun resetAlarm() {
        setInfo.resetAlarm()
    }

    /**
     * Set the alarm
     */
    internal fun setAlarm() {
        setInfo.setAlarm()
    }

    /**
     * Set the time setpoint
     * @param hour 0-23 hour setpoint
     * @param minute minute setpoint
     */
    internal fun setTime(hour: Int, minute: Int) {
        setInfo.setTime(hour, minute)
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