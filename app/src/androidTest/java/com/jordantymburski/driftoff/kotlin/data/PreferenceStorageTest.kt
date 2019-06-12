package com.jordantymburski.driftoff.kotlin.data

import com.jordantymburski.driftoff.kotlin.common.ContextProvider
import com.jordantymburski.driftoff.kotlin.domain.model.AlarmInfo
import org.junit.Assert.assertEquals
import org.junit.Test
import java.util.*
import java.util.concurrent.TimeUnit

class PreferenceStorageTest {
    /**
     * Creates a storage instance
     * @param deleteAll TRUE to delete all existing data. FALSE to leave as is
     * @return the preference storage
     */
    private fun createStorage(deleteAll: Boolean): PreferenceStorage {
        val storage = PreferenceStorage(ContextProvider.get())
        if (deleteAll) storage.deleteAll()
        return storage
    }

    @Test
    fun loadDefault() {
        val storage = createStorage(true)
        val info = storage.load()
        assertEquals(AlarmInfo(), info)
    }

    @Test
    fun setValue() {
        val storage = createStorage(true)
        val info = AlarmInfo(alarm = Date().time, timeHour = 14, timeMinute = 22)
        storage.save(info)
        assertEquals(info, storage.load())

        val info2 = AlarmInfo(alarm = 41523456L, timeHour = 8, timeMinute = 14)
        storage.save(info2)
        assertEquals(info2, storage.load())
    }

    @Test
    fun loadOldValue() {
        val storage = createStorage(true)
        val info = AlarmInfo(alarm = Date().time - TimeUnit.DAYS.toMillis(4), timeHour = 14, timeMinute = 22)
        storage.save(info)

        val storageNew = createStorage(false)
        assertEquals(info, storageNew.load())
    }
}