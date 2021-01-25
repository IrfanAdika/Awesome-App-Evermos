package com.irfan.awesomeapp

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.rule.ActivityTestRule
import com.irfan.awesomeapp.module.home.HomeActivity
import com.irfan.awesomeapp.R.id.*
import org.junit.Rule
import org.junit.Test

class HomeActivityTest {

    @Rule
    @JvmField
    var mActivityTestRule = ActivityTestRule(HomeActivity::class.java)

    @Test
    fun HomeActivityTest() {
        onView(withId(recyclerview_photos))
            .check(matches(isDisplayed()))
        Thread.sleep(1000)
    }
}