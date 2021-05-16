package at.tugraz.ist.guessingwords.ui.start_game

import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers
import at.tugraz.ist.guessingwords.MainActivity
import at.tugraz.ist.guessingwords.R
import org.junit.Test

class HostActivityTest {

    @Test
    fun readyButtontest(){
        val activityScenario = ActivityScenario.launch(MainActivity::class.java)

        Espresso.onView(ViewMatchers.withId(R.id.btn_multiplayer)).check(ViewAssertions.matches(ViewMatchers.isClickable()))
        Espresso.onView(ViewMatchers.withId(R.id.btn_multiplayer)).perform(ViewActions.click())
        Espresso.onView(ViewMatchers.withId(R.id.btn_host)).check(ViewAssertions.matches(ViewMatchers.isClickable()))
        Espresso.onView(ViewMatchers.withId(R.id.btn_host)).perform(ViewActions.click())
        Espresso.onView(ViewMatchers.withId(R.id.btn_ready)).check(ViewAssertions.matches(ViewMatchers.isClickable()))
    }
}