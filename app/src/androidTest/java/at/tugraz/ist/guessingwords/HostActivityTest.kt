package at.tugraz.ist.guessingwords

import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
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

    @Test
    fun hostBackButtontest(){
        val activityScenario = ActivityScenario.launch(MainActivity::class.java)

        Espresso.onView(ViewMatchers.withId(R.id.btn_multiplayer)).check(ViewAssertions.matches(ViewMatchers.isClickable()))
        Espresso.onView(ViewMatchers.withId(R.id.btn_multiplayer)).perform(ViewActions.click())
        Espresso.onView(ViewMatchers.withId(R.id.btn_host)).check(ViewAssertions.matches(ViewMatchers.isClickable()))
        Espresso.onView(ViewMatchers.withId(R.id.btn_host)).perform(ViewActions.click())
        Espresso.onView(ViewMatchers.withContentDescription(R.string.abc_action_bar_up_description)).perform(ViewActions.click())
    }
}