package at.tugraz.ist.guessingwords

import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso
import androidx.test.espresso.Espresso.onData
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.ViewAssertion
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.hamcrest.Matchers.anything
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

    @Test
    fun checkUsernameCorrectlyPassedToHostActivity() {
        val activityScenario = ActivityScenario.launch(MultiplayerActivity::class.java)
        val input = "ABC"

        onView(withId(R.id.editText_multiplayer))
            .perform(typeText(input))
        onView(withId(R.id.btn_host)).perform(click())
        onView(withId(R.id.lst_joined_user)).check(matches(ViewMatchers.isDisplayed()))
        onData(anything())
            .inAdapterView(withId(R.id.lst_joined_user))
            .atPosition(0)
            .onChildView(withId(R.id.li_joinedUser_text))
            .check(matches(withText(input)))
    }
}