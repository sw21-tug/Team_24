package at.tugraz.ist.guessingwords

import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isClickable
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class CustomWordsActivityTest {

    @Test
    fun addNewCustomWords() {
        val activityScenario = ActivityScenario.launch(MainActivity::class.java)

        onView(withId(R.id.btn_customWords)).check(matches(isClickable()))
        onView(withId(R.id.btn_customWords)).perform(click())
        onView(withId(R.id.editText_customWords)).perform(typeText("Testing Custom Words!"))
        onView(withId(R.id.btn_save_word)).check(matches(isClickable()))
        onView(withId(R.id.btn_save_word)).perform(click())
        onView(withId(R.id.btn_back_CW)).check(matches(isClickable()))
        onView(withId(R.id.btn_back_CW)).perform(click())
    }
}