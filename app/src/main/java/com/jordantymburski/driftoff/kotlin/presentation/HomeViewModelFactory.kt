package com.jordantymburski.driftoff.kotlin.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.jordantymburski.driftoff.kotlin.domain.usecase.GetInfo
import com.jordantymburski.driftoff.kotlin.domain.usecase.SetInfo
import javax.inject.Inject

class HomeViewModelFactory @Inject constructor(
    private val getInfo: GetInfo,
    private val setInfo: SetInfo
): ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return HomeViewModel(getInfo, setInfo) as T
    }
}
