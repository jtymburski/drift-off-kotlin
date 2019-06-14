package com.jordantymburski.driftoff.presentation

import android.content.Context
import android.content.DialogInterface
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.VectorDrawable
import android.text.format.DateFormat
import android.view.View
import android.widget.ImageButton
import androidx.lifecycle.MutableLiveData
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.ViewInteraction
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.BoundedMatcher
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.rule.ActivityTestRule
import com.jordantymburski.driftoff.R
import com.jordantymburski.driftoff.common.ContextProvider
import com.jordantymburski.driftoff.domain.MockDomain
import com.jordantymburski.driftoff.domain.model.AlarmInfo
import org.hamcrest.CoreMatchers
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.junit.*
import org.junit.Assert.*
import org.junit.runners.MethodSorters
import org.mockito.Mockito
import java.util.*

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
class HomeActivityTest {
    companion object {
        /**
         * The domain interception object
         */
        private lateinit var domain: MockDomain

        /**
         * Primary data observable
         */
        private val observable = MutableLiveData<AlarmInfo>()

        /**
         * Random number generator instance
         */
        private val random = Random()

        /**
         * Randomized starting alarm info
         */
        private var startInfo = AlarmInfo(alarm = 0L,
            timeHour = random.nextInt(24), timeMinute = random.nextInt(60))

        @BeforeClass @JvmStatic
        fun preSetup() {
            domain = MockDomain()

            // Observable
            observable.postValue(startInfo)
            Mockito.`when`(domain.getInfo.observable).thenReturn(observable)
        }
    }

    @Rule @JvmField
    val activityRule = ActivityTestRule<HomeActivity>(HomeActivity::class.java)

    @Before
    fun setup() {
        domain.clearInvocations()
    }

    /* ----------------------------------------------
     * TEST CASES
     * ---------------------------------------------- */

    @Test
    fun t01_initialLoad() {
        waitForUpdate()

        // Check the time
        checkTime(ContextProvider.get(), startInfo)

        // Check the remaining status
        checkText(view = getViewTextRemaining())

        // Make sure the button is in play mode
        getViewBtnRun().check(matches(withImageDrawable(R.drawable.ic_play)))
    }

    @Test
    fun t02_timeDialogCancel() {
        waitForUpdate()
        val activity = activityRule.activity

        // Check the time
        checkTime(activity, startInfo)

        // Click on the time to open the calendar widget
        getViewTextTime().perform(click())
        waitForUpdate()

        // Check that the time picker is opened
        val timePicker = activity.timePicker
        assertNotNull(timePicker)
        assertTrue(timePicker!!.isShowing)

        // Modify the time
        activity.runOnUiThread {
            timePicker.cancel()
        }
        waitForUpdate()
        assertFalse(timePicker.isShowing)

        // Check the time that no request to change it has been made
        domain.verifyZeroInteractions()
    }

    @Test
    fun t03_timeDialogOk() {
        waitForUpdate()
        val activity = activityRule.activity

        // Check the time
        checkTime(activity, startInfo)

        // Click on the time to open the calendar widget
        getViewTextTime().perform(click())
        waitForUpdate()

        // Check that the time picker is opened
        val timePicker = activity.timePicker
        assertNotNull(timePicker)
        assertTrue(timePicker!!.isShowing)

        // Modify the time
        activity.runOnUiThread {
            timePicker.getButton(DialogInterface.BUTTON_POSITIVE).performClick()
        }
        waitForUpdate()
        assertFalse(timePicker.isShowing)

        // Check the time that no request to change it has been made
        Mockito.verify(domain.setInfo).setTime(startInfo.timeHour, startInfo.timeMinute)
        domain.verifyNoMoreInteractions()
    }

    @Test
    fun t04_timeDialogChange() {
        waitForUpdate()
        val activity = activityRule.activity

        // Check the time
        checkTime(activity, startInfo)

        // Click on the time to open the calendar widget
        getViewTextTime().perform(click())
        waitForUpdate()

        // Check that the time picker is opened
        val timePicker = activity.timePicker
        assertNotNull(timePicker)
        assertTrue(timePicker!!.isShowing)

        // Modify the time
        val newInfo = startInfo.copy(timeHour = random.nextInt(24), timeMinute = random.nextInt(60))
        activity.runOnUiThread {
            timePicker.updateTime(newInfo.timeHour, newInfo.timeMinute)
            timePicker.getButton(DialogInterface.BUTTON_POSITIVE).performClick()
        }
        waitForUpdate()
        assertFalse(timePicker.isShowing)

        // Check the time that no request to change it has been made
        Mockito.verify(domain.setInfo).setTime(newInfo.timeHour, newInfo.timeMinute)
        domain.verifyNoMoreInteractions()

        // Pass it through to the observer and witness the update
        startInfo = newInfo
        observable.postValue(newInfo)
        waitForUpdate()
        checkTime(activity, newInfo)
    }

    @Test
    fun t05_startAndStopAlarm() {
        waitForUpdate()
        val context = ContextProvider.get()

        // Check that the alarm is not active and the starting parameters are still maintained
        checkAlarmInactive(context, startInfo)

        // Start the alarm
        getViewBtnRun().perform(click())
        waitForUpdate()

        // Check that the alarm was attempted to be set on the domain
        Mockito.verify(domain.setInfo).setAlarm()
        domain.verifyNoMoreInteractions()

        // Set the alarm in the observer and witness the result
        val setInfo = startInfo.copy(alarm = startInfo.getTimeInMillis())
        observable.postValue(setInfo)
        waitForUpdate()
        checkAlarmActive(context, setInfo)

        // Stop the alarm
        getViewBtnRun().perform(click())
        waitForUpdate()

        // Check that the activity requested the alarm to be reset to the domain
        Mockito.verify(domain.setInfo).resetAlarm()
        domain.verifyNoMoreInteractions()

        // Check that the alarm is no longer displayed and back to the start
        observable.postValue(startInfo)
        waitForUpdate()
        checkAlarmInactive(context, startInfo)
    }

    @Test
    fun t06_alarmOverHour() {
        val activity = activityRule.activity

        // Fetch the time for over 1 hour from now
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.HOUR_OF_DAY, 2)
        val info = AlarmInfo(alarm = 0L,
            timeHour = calendar.get(Calendar.HOUR_OF_DAY), timeMinute = calendar.get(Calendar.MINUTE))

        // Start the alarm
        val infoActive = info.copy(alarm = info.getTimeInMillis())
        observable.postValue(infoActive)
        waitForUpdate()

        // Check the time string
        checkAlarmActive(activity, infoActive)
        checkText(getViewTextRemaining(), activity.resources.getQuantityString(R.plurals.alarm_notice_hours,
            infoActive.getHoursTillAlarm().toInt(), infoActive.getHoursTillAlarm()))

        // Reset the observable state
        observable.postValue(startInfo)
    }

    @Test
    fun t07_alarmUnderHour() {
        val activity = activityRule.activity

        // Fetch the time for over 1 hour from now
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.MINUTE, 2)
        val info = AlarmInfo(alarm = 0L,
            timeHour = calendar.get(Calendar.HOUR_OF_DAY), timeMinute = calendar.get(Calendar.MINUTE))

        // Start the alarm
        startInfo = info.copy(alarm = info.getTimeInMillis())
        observable.postValue(startInfo)
        waitForUpdate()

        // Check the time string
        checkAlarmActive(activity, startInfo)
        checkText(getViewTextRemaining(), activity.resources.getQuantityString(R.plurals.alarm_notice_minutes,
            startInfo.getMinutesTillAlarm().toInt(), startInfo.getMinutesTillAlarm()))
    }

    @Test
    fun t08_noTimeDialogIfAlarm() {
        waitForUpdate()
        val activity = activityRule.activity

        // Check that an alarm is active
        assertTrue(startInfo.isActive())
        checkAlarmActive(activity, startInfo)

        // Try to open the dialog
        getViewTextTime().perform(click())
        waitForUpdate()
        assertNull(activity.timePicker)
    }

    @Test
    fun t09_alarmFired() {
        waitForUpdate()
        val context = ContextProvider.get()

        // Check that an alarm is active
        assertTrue(startInfo.isActive())
        checkAlarmActive(context, startInfo)

        // Change to a non-active alarm and update
        startInfo = startInfo.copy(alarm = 0L)
        assertFalse(startInfo.isActive())
        observable.postValue(startInfo)
        waitForUpdate()

        // Check that the alarm is no longer active
        checkAlarmInactive(context, startInfo)
    }

    @Test
    fun t10_setAlarmBackground() {
        waitForUpdate()
        val context = ContextProvider.get()

        // Make sure the alarm is inactive
        assertFalse(startInfo.isActive())
        checkAlarmInactive(context, startInfo)

        // Change to an active alarm
        val activeInfo = startInfo.copy(alarm = startInfo.getTimeInMillis())
        assertTrue(activeInfo.isActive())
        observable.postValue(activeInfo)
        waitForUpdate()

        // Check for an active alarm
        checkAlarmActive(context, activeInfo)
    }

    /* ----------------------------------------------
     * PRIVATE FUNCTIONS
     * ---------------------------------------------- */

    /**
     * Checks if the alarm is active and displayed to the user
     * @param context application reference context
     * @param info the alarm info that should be displayed
     */
    private fun checkAlarmActive(context: Context, info: AlarmInfo) {
        checkTime(context, info)
        getViewTextRemaining().check(matches(CoreMatchers.not(withText(""))))
        getViewBtnRun().check(matches(withImageDrawable(R.drawable.ic_stop)))
    }

    /**
     * Checks if the alarm is inactive and displayed to the user
     * @param context application reference context
     * @param info the alarm info that should be displayed
     */
    private fun checkAlarmInactive(context: Context, info: AlarmInfo) {
        checkTime(context, info)
        checkText(view = getViewTextRemaining())
        getViewBtnRun().check(matches(withImageDrawable(R.drawable.ic_play)))
    }

    /**
     * Run a text comparison
     * @param view the espresso view to check on
     * @param text the text that should be in the field
     */
    private fun checkText(view: ViewInteraction, text: String = "") {
        view.check(matches(withText(text)))
    }

    /**
     * Check the displayed time in the UI
     * @param context application reference context
     * @param info the alarm info that should be displayed
     */
    private fun checkTime(context: Context, info: AlarmInfo) {
        val alarmCalendar = info.getTime()
        if (DateFormat.is24HourFormat(context)) {
            checkText(view = getViewTextTime(), text = DateFormat.getTimeFormat(context).format(alarmCalendar.time))
            checkText(view = getViewTextPeriod())
        } else {
            checkText(view = getViewTextTime(), text = DateFormat.format("h:mm", alarmCalendar).toString())
            checkText(view = getViewTextPeriod(),
                text = DateFormat.format("a", alarmCalendar).toString().replace(".", ""))
        }
    }

    /**
     * Converts a vector drawable into a bitmap. Clean up is responsibility of the caller (.recycle)
     * @param vectorDrawable the valid vector drawable
     * @return the new bitmap
     */
    private fun createBitmap(vectorDrawable: VectorDrawable): Bitmap {
        val bitmap = Bitmap.createBitmap(
            vectorDrawable.intrinsicWidth, vectorDrawable.intrinsicHeight, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        vectorDrawable.setBounds(0, 0, canvas.width, canvas.height)
        vectorDrawable.draw(canvas)
        return bitmap
    }

    /**
     * Fetch the run button
     * @return espresso view for testing
     */
    private fun getViewBtnRun(): ViewInteraction {
        return onView(withId(R.id.run_button))
    }

    /**
     * Fetch the time period text field (am/pm)
     * @return espresso view for testing
     */
    private fun getViewTextPeriod(): ViewInteraction {
        return onView(withId(R.id.time_period))
    }

    /**
     * Fetch the time remaining text (when the alarm is active and running)
     * @return espresso view for testing
     */
    private fun getViewTextRemaining(): ViewInteraction {
        return onView(withId(R.id.time_remaining))
    }

    /**
     * Fetch the time text field
     * @return espresso view for testing
     */
    private fun getViewTextTime(): ViewInteraction {
        return onView(withId(R.id.time_text))
    }

    /**
     * Wait for the UI to update due to the async LiveData response system
     * @throws InterruptedException if the wait ended early
     */
    private fun waitForUpdate() {
        Thread.sleep(350)
    }

    /**
     * Matcher comparison to check that an image drawable matches a noted resource ID
     * @param resourceId the expected resource ID
     * @return the espresso matcher
     */
    private fun withImageDrawable(resourceId: Int): Matcher<View> {
        return object : BoundedMatcher<View, ImageButton>(ImageButton::class.java) {
            override fun describeTo(description: Description) {
                description.appendText("has image drawable resource $resourceId")
            }

            override fun matchesSafely(imageButton: ImageButton): Boolean {
                val expectedDrawable = imageButton.context.getDrawable(resourceId)
                if (imageButton.drawable is VectorDrawable && expectedDrawable is VectorDrawable) {
                    val current = createBitmap(imageButton.drawable as VectorDrawable)
                    val expected = createBitmap(expectedDrawable)
                    val isSame = current.sameAs(expected)

                    current.recycle()
                    expected.recycle()

                    return isSame
                }
                return false
            }
        }
    }
}