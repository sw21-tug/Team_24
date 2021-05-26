package at.tugraz.ist.guessingwords

import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class NextRoundActivityTest {
    @Test
    fun nextRoundScreenDisplayed() {
        val activityScenario = ActivityScenario.launch(NextRoundScreenActivity::class.java)

        onView(withId(R.id.text_skippedWords)).check(matches(isDisplayed()))
        onView(withId(R.id.text_correctGuesses)).check(matches(isDisplayed()))
    }

    @Test
    fun nextRoundButtonDisplayed() {
        val activityScenario = ActivityScenario.launch(NextRoundScreenActivity::class.java)

        onView(withId(R.id.btn_nextRound)).check(matches(isClickable()))
        onView(withId(R.id.btn_nextRound)).check(matches(isDisplayed()))
    }
}