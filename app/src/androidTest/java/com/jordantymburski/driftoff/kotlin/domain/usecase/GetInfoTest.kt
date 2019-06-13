package com.jordantymburski.driftoff.kotlin.domain.usecase

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import androidx.test.annotation.UiThreadTest
import com.jordantymburski.driftoff.kotlin.common.ResettableCountDownLatch
import com.jordantymburski.driftoff.kotlin.domain.adapter.Storage
import com.jordantymburski.driftoff.kotlin.domain.model.AlarmInfo
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import java.util.*
import java.util.concurrent.TimeUnit

class GetInfoTest: Observer<AlarmInfo> {
    /**
     * Get info use case
     */
    private lateinit var getInfo: GetInfo

    /**
     * Starting info
     */
    private lateinit var initInfo: AlarmInfo

    /**
     * Reset capable count down latch
     */
    private val lock = ResettableCountDownLatch(1)

    /**
     * The last observed info from the live data subscription
     */
    private var observedInfo: AlarmInfo? = null

    /**
     * Random number generator instance
     */
    private val random = Random()

    /**
     * Storage mock class
     */
    @Mock private lateinit var storage: Storage

    @Rule @JvmField
    val syncRule = InstantTaskExecutorRule()

    override fun onChanged(info: AlarmInfo?) {
        observedInfo = info
        lock.countDown()
    }

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)
        lock.reset()

        // Create the starting alarm object
        initInfo = AlarmInfo(alarm = 0L, timeHour = random.nextInt(24), timeMinute = random.nextInt(60))

        // Storage mock config
        Mockito.`when`(storage.load()).thenReturn(initInfo)

        // Get info use case set up and observe
        getInfo = GetInfo(storage)
        getInfo.observable.observeForever(this)
    }

    @After
    fun cleanUp() {
        getInfo.observable.removeObserver(this)
    }

    @Test
    @UiThreadTest
    fun current() {
        // Wait for the observable to be active
        waitForResponse()
        Mockito.clearInvocations(storage)

        // Fetch the current after it was already observed. Should fetch from live data internally
        assertEquals(initInfo, getInfo.current())
        Mockito.verifyZeroInteractions(storage)

        // Create a new use case but do not observe. It should still properly fetch
        val fetchedFreshInfo = GetInfo(storage).current()
        assertNotNull(fetchedFreshInfo)
        assertEquals(initInfo, fetchedFreshInfo)
    }

    @Test
    @UiThreadTest
    fun observe() {
        // First update (initial)
        waitForResponse()
        lock.reset()
        assertNotNull(observedInfo)
        assertEquals(initInfo, observedInfo)

        // No more updates should happen since no data was changed
        assertFalse(lock.await(2000, TimeUnit.MILLISECONDS))

        // Make sure it only called load on the storage adapter
        Mockito.verify(storage, Mockito.atLeastOnce()).load()
        Mockito.verifyNoMoreInteractions(storage)
    }

    @Test
    @UiThreadTest
    fun post() {
        // First update (initial)
        waitForResponse()

        // New alarm info. Post it and wait for the observe update
        val newInfo = AlarmInfo(alarm = Date().time,
            timeHour = random.nextInt(24), timeMinute = random.nextInt(60))
        lock.reset()
        getInfo.post(newInfo)
        waitForResponse()

        // Check result
        assertNotNull(observedInfo)
        assertEquals(newInfo, observedInfo)

        // Try and post on a fresh object. This should do nothing but NOT crash
        GetInfo(storage).post(newInfo)
    }

    /**
     * Waits for a response from the observer for a fixed period of time
     * @throws InterruptedException exception thrown if thread requested to shut down
     */
    private fun waitForResponse() {
        assertTrue(lock.await(5000, TimeUnit.MILLISECONDS))
    }
}