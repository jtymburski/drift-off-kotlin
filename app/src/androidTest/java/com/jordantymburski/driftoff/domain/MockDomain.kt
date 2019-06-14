package com.jordantymburski.driftoff.domain

import com.jordantymburski.driftoff.App
import com.jordantymburski.driftoff.common.ContextProvider
import com.jordantymburski.driftoff.di.test.DaggerMockAppComponent
import com.jordantymburski.driftoff.di.test.MockDomainModule
import com.jordantymburski.driftoff.domain.usecase.GetInfo
import com.jordantymburski.driftoff.domain.usecase.RescheduleAlarm
import com.jordantymburski.driftoff.domain.usecase.SetInfo
import com.jordantymburski.driftoff.domain.usecase.StopAudio
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations

class MockDomain {
    /**
     * Get info domain use case
     */
    @Mock lateinit var getInfo: GetInfo
        private set

    /**
     * Reschedule alarm domain use case
     */
    @Mock lateinit var rescheduleAlarm: RescheduleAlarm
        private set

    /**
     * Set info domain use case
     */
    @Mock lateinit var setInfo: SetInfo
        private set

    /**
     * Stop audio domain use case
     */
    @Mock lateinit var stopAudio: StopAudio
        private set

    /**
     * Initialization constructor
     */
    init {
        MockitoAnnotations.initMocks(this)

        (ContextProvider.get().applicationContext as App)
            .component(DaggerMockAppComponent.builder()
                .mockDomainModule(MockDomainModule(getInfo, rescheduleAlarm, setInfo, stopAudio))
                .build())
    }

    /**
     * Fetches a primitive array of all use cases for usage in (Object... mocks) function calls
     * @return the array
     */
    private fun getAll(): Array<Any> {
        return arrayOf(getInfo, rescheduleAlarm, setInfo, stopAudio)
    }

    /**
     * Clears all existing history of function calls for all domain use cases
     */
    fun clearInvocations() {
        Mockito.clearInvocations(*getAll())
    }

    /**
     * Verifies that there are no more interactions on all domain use cases
     */
    fun verifyNoMoreInteractions() {
        Mockito.verifyNoMoreInteractions(*getAll())
    }

    /**
     * Verifies that there are zero interactions on all domain use cases
     */
    fun verifyZeroInteractions() {
        Mockito.verifyZeroInteractions(*getAll())
    }
}