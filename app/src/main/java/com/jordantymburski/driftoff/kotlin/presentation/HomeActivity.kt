package com.jordantymburski.driftoff.kotlin.presentation

import android.os.Bundle
import android.text.format.DateFormat
import android.view.WindowManager
import androidx.fragment.app.FragmentActivity
import com.jordantymburski.driftoff.kotlin.R
import com.jordantymburski.driftoff.kotlin.domain.model.AlarmInfo
import dagger.android.AndroidInjection
import kotlinx.android.synthetic.main.activity_home.*
import java.util.Calendar
import javax.inject.Inject

class HomeActivity : FragmentActivity() {
    companion object {
        private const val DAY_START_HOUR = 8L // 8:00am inclusive
        private const val DAY_END_HOUR = 18L // 6:00pm inclusive
    }

    /**
     * Current theme resource being displayed
     */
    private var currentTheme = 0

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

    override fun onResume() {
        super.onResume()

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
        // TODO
    }

    /**
     * Notified on click trigger when the time is tapped
     */
    private fun timeClick() {
        // TODO
    }

    /**
     * Updates all views
     */
    private fun updateView() {
        val alarmTime = modelInfo.getTime()
        if (DateFormat.is24HourFormat(applicationContext)) {
            time_text.text = DateFormat.getTimeFormat(applicationContext).format(alarmTime.time)
            time_period.text = null
        } else {
            time_text.text = DateFormat.format("h:mm", alarmTime)
            time_period.text = DateFormat.format("a", alarmTime).toString()
                .replace(".", "")
        }
    }
}
