package at.tugraz.ist.guessingwords

import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class StartActivityTest {

    @Test
    fun backButton(){
        val activityScenario = ActivityScenario.launch(MainActivity::class.java)

        onView(withId(R.id.btn_startGame)).check(matches(isClickable()))
        onView(withId(R.id.btn_startGame)).perform(click())
        onView(withId(R.id.btn_back_GP)).check(matches(isClickable()))
        onView(withId(R.id.btn_back_GP)).perform(click())
    }

    @Test
    fun skipButton(){
        val activityScenario = ActivityScenario.launch(MainActivity::class.java)

        onView(withId(R.id.btn_startGame)).check(matches(isClickable()))
        onView(withId(R.id.btn_startGame)).perform(click())
        onView(withId(R.id.btn_skipWord)).check(matches(isClickable()))
        onView(withId(R.id.btn_skipWord)).perform(click())
    }

    @Test
    fun correctButton(){
        val activityScenario = ActivityScenario.launch(MainActivity::class.java)

        onView(withId(R.id.btn_startGame)).check(matches(isClickable()))
        onView(withId(R.id.btn_startGame)).perform(click())
        onView(withId(R.id.btn_correctWord)).check(matches(isClickable()))
        onView(withId(R.id.btn_correctWord)).perform(click())
    }

    @Test
    fun wordFieldEmpty() {
        val activityScenario = ActivityScenario.launch(MainActivity::class.java)

        onView(withId(R.id.btn_startGame)).check(matches(isClickable()))
        onView(withId(R.id.btn_startGame)).perform(click())
        onView(withId(R.id.txt_fieldWord)).check(matches(isDisplayed()))
    }

    @Test
    fun timerFieldDisplayed() {
        val activityScenario = ActivityScenario.launch(MainActivity::class.java)

        onView(withId(R.id.btn_startGame)).check(matches(isClickable()))
        onView(withId(R.id.btn_startGame)).perform(click())
        onView(withId(R.id.txt_fieldTimer)).check(matches(isDisplayed()))
    }
}