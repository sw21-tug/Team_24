package at.tugraz.ist.guessingwords

import androidx.core.os.bundleOf
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isClickable
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import at.tugraz.ist.guessingwords.data.service.WordService
import at.tugraz.ist.guessingwords.ui.custom_words.CustomWordsFragment
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.kotlin.any
import org.mockito.kotlin.mock
import org.mockito.kotlin.times
import org.mockito.kotlin.verify

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

        //Todo Test if stuff is actually displayed and if input is deleted from input field and so on

        onView(withId(R.id.btn_back_CW)).check(matches(isClickable()))
        onView(withId(R.id.btn_back_CW)).perform(click())
    }


    @Test
    fun customWordSaveFragmentTest() {
        val wordServiceMock = mock<WordService>()

        val fragmentArgs = bundleOf()
        val scenario = launchFragmentInContainer<CustomWordsFragment>(fragmentArgs)
        scenario.onFragment { fragment -> fragment.customWordService = wordServiceMock }

        val input = "Testing Custom Words!"

        onView(withId(R.id.editText_customWords)).perform(typeText(input))
        onView(withId(R.id.btn_save_word)).check(matches(isClickable()))
        onView(withId(R.id.btn_save_word)).perform(click())

        verify(wordServiceMock, times(1)).insertOrUpdateExistingWord(any(), any())
    }
}