package com.jordantymburski.driftoff.kotlin.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.jordantymburski.driftoff.kotlin.domain.usecase.GetInfo
import javax.inject.Inject

class HomeViewModelFactory @Inject constructor(
    private val getInfo: GetInfo
): ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return HomeViewModel(getInfo = getInfo) as T
    }
}
