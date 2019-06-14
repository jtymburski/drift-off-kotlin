package com.jordantymburski.driftoff.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.jordantymburski.driftoff.domain.usecase.GetInfo
import com.jordantymburski.driftoff.domain.usecase.SetInfo
import javax.inject.Inject

class HomeViewModelFactory @Inject constructor(
    private val getInfo: GetInfo,
    private val setInfo: SetInfo
): ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return HomeViewModel(getInfo, setInfo) as T
    }
}
