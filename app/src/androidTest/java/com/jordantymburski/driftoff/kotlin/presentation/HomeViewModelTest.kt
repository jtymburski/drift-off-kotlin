package com.jordantymburski.driftoff.kotlin.presentation

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import com.jordantymburski.driftoff.kotlin.domain.model.AlarmInfo
import com.jordantymburski.driftoff.kotlin.domain.usecase.GetInfo
import com.jordantymburski.driftoff.kotlin.domain.usecase.SetInfo
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import java.util.*

class HomeViewModelTest {
    /**
     * Get info use case mock class
     */
    @Mock private lateinit var getInfo: GetInfo

    /**
     * Starting alarm info
     */
    private lateinit var initInfo: AlarmInfo

    /**
     * Internal observable to simulate data connection
     */
    private val observable = MutableLiveData<AlarmInfo>()

    /**
     * Random number generator instance
     */
    private val random = Random()

    /**
     * Set info use case mock class
     */
    @Mock private lateinit var setInfo: SetInfo

    /**
     * The presentation view model
     */
    private lateinit var viewModel: HomeViewModel

    @Rule @JvmField
    val syncRule = InstantTaskExecutorRule()

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)

        // Make the get info mock return an active observable
        initInfo = AlarmInfo(alarm = Date().time,
            timeHour = random.nextInt(24), timeMinute = random.nextInt(60))
        observable.value = initInfo
        Mockito.`when`(getInfo.observable).thenReturn(observable)

        // Create the model
        viewModel = HomeViewModelFactory(getInfo, setInfo).create(HomeViewModel::class.java)

        // Clear out any invocations
        Mockito.clearInvocations(getInfo, setInfo)
    }

    @Test
    fun observable() {
        assertNotNull(viewModel.observable())
        assertEquals(observable, viewModel.observable())
        assertEquals(initInfo, viewModel.observable().value)
    }

    @Test
    fun resetAlarm() {
        // Execute
        viewModel.resetAlarm()

        // Check
        Mockito.verifyZeroInteractions(getInfo)
        Mockito.verify(setInfo).resetAlarm()
        Mockito.verifyNoMoreInteractions(setInfo)
    }

    @Test
    fun setAlarm() {
        // Execute
        viewModel.setAlarm()

        // Check
        Mockito.verifyZeroInteractions(getInfo)
        Mockito.verify(setInfo).setAlarm()
        Mockito.verifyNoMoreInteractions(setInfo)
    }

    @Test
    fun setTime() {
        // Execute
        val hour = random.nextInt(24)
        val minute = random.nextInt(60)
        viewModel.setTime(hour, minute)

        // Check
        Mockito.verifyZeroInteractions(getInfo)
        Mockito.verify(setInfo).setTime(hour, minute)
        Mockito.verifyNoMoreInteractions(setInfo)
    }
}