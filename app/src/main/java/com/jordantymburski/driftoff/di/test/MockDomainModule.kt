package com.jordantymburski.driftoff.di.test

import com.jordantymburski.driftoff.domain.usecase.GetInfo
import com.jordantymburski.driftoff.domain.usecase.RescheduleAlarm
import com.jordantymburski.driftoff.domain.usecase.SetInfo
import com.jordantymburski.driftoff.domain.usecase.StopAudio
import dagger.Module
import dagger.Provides

@Module
class MockDomainModule(
    private val getInfo: GetInfo,
    private val rescheduleAlarm: RescheduleAlarm,
    private val setInfo: SetInfo,
    private val stopAudio: StopAudio
) {
    val provideGetInfo: GetInfo
        @Provides get() = this.getInfo

    val provideRescheduleAlarm: RescheduleAlarm
        @Provides get() = this.rescheduleAlarm

    val provideSetInfo: SetInfo
        @Provides get() = this.setInfo

    val provideStopAudio: StopAudio
        @Provides get() = this.stopAudio
}