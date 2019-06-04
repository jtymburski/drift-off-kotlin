package com.jordantymburski.driftoff.kotlin.presentation

import android.os.Bundle
import android.view.WindowManager
import androidx.fragment.app.FragmentActivity
import com.jordantymburski.driftoff.kotlin.R

class HomeActivity : FragmentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Content
        setContentView(R.layout.activity_home)

        // Full screen under the status bar at the top and nav bar at the bottom
        window.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
    }
}
