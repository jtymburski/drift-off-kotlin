package com.jordantymburski.driftoff.data

import android.content.Context
import androidx.annotation.VisibleForTesting
import com.jordantymburski.driftoff.domain.adapter.Storage
import com.jordantymburski.driftoff.domain.model.AlarmInfo

/**
 * Implementation of storage using the SharedPreferences android interface
 * @param context android application context
 */
class PreferenceStorage(context: Context): Storage {
    companion object {
        private const val KEY_ALARM = "alarm"
        private const val KEY_TIME_HOUR = "timeHour"
        private const val KEY_TIME_MINUTE = "timeMinute"
        private const val STORE_NAME = "DataStorage"
    }

    /**
     * Shared preference database connection
     */
    private val database = context.getSharedPreferences(STORE_NAME, Context.MODE_PRIVATE)

    /**
     * Delete all keys in the preference database
     */
    @SuppressWarnings("ApplySharedPref")
    @VisibleForTesting
    internal fun deleteAll() {
        database.edit().clear().commit()
    }

    /**
     * Load all data from the shared preference storage
     * @return stored alarm info
     */
    override fun load(): AlarmInfo {
        val defaultInfo = AlarmInfo()
        return AlarmInfo(
            alarm = database.getLong(KEY_ALARM, defaultInfo.alarm),
            timeHour = database.getInt(KEY_TIME_HOUR, defaultInfo.timeHour),
            timeMinute = database.getInt(KEY_TIME_MINUTE, defaultInfo.timeMinute))
    }

    /**
     * Save all changes to the shared preference storage
     * @param info new alarm info
     */
    override fun save(info: AlarmInfo) {
        database.edit()
            .putLong(KEY_ALARM, info.alarm)
            .putInt(KEY_TIME_HOUR, info.timeHour)
            .putInt(KEY_TIME_MINUTE, info.timeMinute)
            .apply()
    }
}