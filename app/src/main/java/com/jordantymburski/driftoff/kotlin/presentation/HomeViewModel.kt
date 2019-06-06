package com.jordantymburski.driftoff.kotlin.presentation

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import com.jordantymburski.driftoff.kotlin.domain.model.AlarmInfo

class HomeViewModel : ViewModel() {
    /**
     * Observable wrapper that the connected activity can monitor for changes
     */
    val infoObservable = MutableLiveData<AlarmInfo>()

    init {
        infoObservable.postValue(AlarmInfo())
    }

    companion object {
        fun getInstance(owner: ViewModelStoreOwner, factory: ViewModelProvider.Factory) : HomeViewModel {
            return ViewModelProvider(owner, factory).get(HomeViewModel::class.java)
        }
    }
}