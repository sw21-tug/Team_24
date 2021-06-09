package at.tugraz.ist.guessingwords

import android.view.View
import android.widget.TextView
import androidx.core.os.bundleOf
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.withContentDescription
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import at.tugraz.ist.guessingwords.data.entity.Word
import at.tugraz.ist.guessingwords.data.service.Callback
import at.tugraz.ist.guessingwords.data.service.WordService
import at.tugraz.ist.guessingwords.ui.start_game.GamePlayFragment
import org.hamcrest.BaseMatcher
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.kotlin.*

@RunWith(AndroidJUnit4::class)
class UserInterfaceActivityTest {

    lateinit var textSaver: ViewAction

    @Test
    fun fromMainActivityToGamePlayActivityAndBack(){
        val activityScenario = ActivityScenario.launch(MainActivity::class.java)

        onView(ViewMatchers.withId(R.id.btn_startGame)).check(ViewAssertions.matches(ViewMatchers.isClickable()))
        onView(ViewMatchers.withId(R.id.btn_startGame)).perform(click())
        onView(withContentDescription("Navigate up")).perform(click())
        onView(ViewMatchers.withId(R.id.btn_startGame)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }

    @Test
    fun fromMainActivityToMultiplayerActivityAndBack(){
        val activityScenario = ActivityScenario.launch(MainActivity::class.java)

        onView(ViewMatchers.withId(R.id.btn_multiplayer)).check(ViewAssertions.matches(ViewMatchers.isClickable()))
        onView(ViewMatchers.withId(R.id.btn_multiplayer)).perform(click())
        onView(withContentDescription("Navigate up")).perform(click())
        onView(ViewMatchers.withId(R.id.btn_multiplayer)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }
}