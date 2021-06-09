package at.tugraz.ist.guessingwords

import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class JoinActivityTest {

    @Test
    fun joinScreenMessageCheck(){
        val activityScenario = ActivityScenario.launch(MainActivity::class.java)

        onView(withId(R.id.btn_multiplayer)).check(matches(isClickable()))
        onView(withId(R.id.btn_multiplayer)).perform(click())
        onView(withId(R.id.btn_join)).check(matches(isClickable()))
        onView(withId(R.id.btn_join)).perform(click())
        onView(withId(R.id.text_wordsSentMessage)).check(matches(isDisplayed()))
    }

    @Test
    fun joinBackButtontest(){
        val activityScenario = ActivityScenario.launch(MainActivity::class.java)

        onView(withId(R.id.btn_multiplayer)).check(matches(isClickable()))
        onView(withId(R.id.btn_multiplayer)).perform(click())
        onView(withId(R.id.btn_join)).check(matches(isClickable()))
        onView(withId(R.id.btn_join)).perform(click())
        onView(withContentDescription(R.string.abc_action_bar_up_description)).perform(click())
    }

    @Test
    fun checkUsernameCorrectlyPassedToJoinActivity() {
        val activityScenario = ActivityScenario.launch(MultiplayerActivity::class.java)
        val input = "ABC"

        onView(withId(R.id.editText_multiplayer)).perform(typeText(input))
        onView(withId(R.id.btn_join)).perform(click())
        onView(withId(R.id.text_username_join)).check(matches(withText(input)))
    }
}