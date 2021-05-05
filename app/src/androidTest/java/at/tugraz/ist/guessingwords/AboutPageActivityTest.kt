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
class AboutPageActivityTest {

    @Test
    fun backButton(){
        val activityScenario = ActivityScenario.launch(MainActivity::class.java)

        Espresso.openActionBarOverflowOrOptionsMenu(InstrumentationRegistry.getInstrumentation().getTargetContext())
        onView(withText("About")).perform(click())
        onView(withContentDescription(R.string.abc_action_bar_up_description)).perform(click())
    }

//    @Test
//    fun wordFieldEmpty() {
//        val activityScenario = ActivityScenario.launch(MainActivity::class.java)
//
//        Espresso.openActionBarOverflowOrOptionsMenu(InstrumentationRegistry.getInstrumentation().getTargetContext())
//        onView(withText("About")).perform(click())
//        onView(withId(R.id.text_aboutPageText)).check(matches(isDisplayed()))
//    }
}