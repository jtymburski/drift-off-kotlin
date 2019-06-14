package com.jordantymburski.driftoff.presentation

import android.app.TimePickerDialog
import android.os.Bundle
import android.os.Handler
import android.text.format.DateFormat
import android.view.WindowManager
import androidx.fragment.app.FragmentActivity
import com.jordantymburski.driftoff.R
import com.jordantymburski.driftoff.domain.model.AlarmInfo
import dagger.android.AndroidInjection
import kotlinx.android.synthetic.main.activity_home.*
import java.util.Calendar
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class HomeActivity : FragmentActivity() {
    companion object {
        private const val DAY_START_HOUR = 8L // 8:00am inclusive
        private const val DAY_END_HOUR = 18L // 6:00pm inclusive
        private val UPDATE_TIME_MS = TimeUnit.SECONDS.toMillis(30)
    }

    /**
     * Current theme resource being displayed
     */
    private var currentTheme = 0

    /**
     * Thread handler
     */
    private val handler = Handler()

    /**
     * View model
     */
    private lateinit var model: HomeViewModel

    /**
     * View model factory
     */
    @Inject
    lateinit var modelFactory: HomeViewModelFactory

    /**
     * Current model info
     */
    private var modelInfo = AlarmInfo()

    /**
     * Last time picker dialog that was opened and is still active
     */
    var timePicker: TimePickerDialog? = null

    /**
     * Update state runnable
     */
    private val updateRunnable = Runnable {
        updateState()
    }

    /* ----------------------------------------------
     * FragmentActivity OVERRIDES
     * ---------------------------------------------- */

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)

        // Content and theme
        currentTheme = getThemeResource()
        setTheme(currentTheme)
        setContentView(R.layout.activity_home)

        // Full screen under the status bar at the top and nav bar at the bottom
        window.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)

        // UI on click triggers
        run_button.setOnClickListener { runClick() }
        time_text.setOnClickListener { timeClick() }

        // Create model and start observing the view model for data changes
        model = HomeViewModel.getInstance(this, modelFactory)
        model.observable().observe(this, androidx.lifecycle.Observer {
            if (modelInfo != it) {
                modelInfo = it
                updateView()
            }
        })
    }

    override fun onPause() {
        super.onPause()

        handler.removeCallbacks(updateRunnable)
    }

    override fun onResume() {
        super.onResume()

        // Clean up any previously active dialog reference
        timePicker = null

        // Check that the theme has not changed while away
        if (currentTheme != getThemeResource()) {
            recreate()
        } else {
            updateView()
        }
    }

    /* ----------------------------------------------
     * PRIVATE FUNCTIONS
     * ---------------------------------------------- */

    /**
     * Determines the theme resource that should be used
     * @return style resource ID
     */
    private fun getThemeResource(): Int {
        return if (Calendar.getInstance().get(Calendar.HOUR_OF_DAY) in DAY_START_HOUR..DAY_END_HOUR)
            R.style.AppTheme_Light
        else
            R.style.AppTheme
    }

    /**
     * Notified on click trigger when the run button is tapped
     */
    private fun runClick() {
        if (modelInfo.isActive())
            model.resetAlarm()
        else
            model.setAlarm()
    }

    /**
     * Notified on click trigger when the time is tapped
     */
    private fun timeClick() {
        if (!modelInfo.isActive()) {
            val timeListener = TimePickerDialog.OnTimeSetListener { _, hourOfDay, minute ->
                model.setTime(hour = hourOfDay, minute = minute)
            }

            timePicker = TimePickerDialog(this, timeListener,
                modelInfo.timeHour, modelInfo.timeMinute, DateFormat.is24HourFormat(applicationContext))
            timePicker!!.show()
        }
    }

    /**
     * Assembles the string status for the time that is remaining before the alarm fires
     * @return time remaining text
     */
    private fun timeTextRemaining(): String {
        // Check if it should display in hours
        val hoursToStop = modelInfo.getHoursTillAlarm()
        if (hoursToStop > 1) {
            return resources.getQuantityString(
                R.plurals.alarm_notice_hours, hoursToStop.toInt(), hoursToStop
            )
        }

        // Otherwise, it should display in minutes
        val minutesToStop = modelInfo.getMinutesTillAlarm()
        return resources.getQuantityString(
            R.plurals.alarm_notice_minutes, minutesToStop.toInt(), minutesToStop
        )
    }

    /**
     * Updates the active state
     */
    private fun updateState() {
        handler.removeCallbacks(updateRunnable)

        if (modelInfo.isActive()) {
            run_button.setImageResource(R.drawable.ic_stop)
            time_text.setTextColor(getColor(R.color.textActive))
            time_period.setTextColor(getColor(R.color.textActive))
            time_remaining.text = timeTextRemaining()

            handler.postDelayed(updateRunnable, UPDATE_TIME_MS)
        } else {
            run_button.setImageResource(R.drawable.ic_play)
            time_text.setTextColor(getColor(R.color.textEdit))
            time_period.setTextColor(getColor(R.color.textEdit))
            time_remaining.text = ""
        }
    }

    /**
     * Updates all views
     */
    private fun updateView() {
        // Time setpoint
        val alarmTime = modelInfo.getTime()
        if (DateFormat.is24HourFormat(applicationContext)) {
            time_text.text = DateFormat.getTimeFormat(applicationContext).format(alarmTime.time)
            time_period.text = null
        } else {
            time_text.text = DateFormat.format("h:mm", alarmTime)
            time_period.text = DateFormat.format("a", alarmTime).toString()
                .replace(".", "")
        }

        // Active state and time remaining
        updateState()
    }
}
