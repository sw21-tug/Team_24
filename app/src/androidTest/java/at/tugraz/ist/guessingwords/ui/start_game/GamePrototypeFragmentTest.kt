package at.tugraz.ist.guessingwords.ui.start_game

import androidx.core.os.bundleOf
import androidx.fragment.app.testing.FragmentScenario
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.test.espresso.Espresso
import androidx.test.espresso.ViewAssertion
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.ext.junit.runners.AndroidJUnit4
import at.tugraz.ist.guessingwords.R
import at.tugraz.ist.guessingwords.data.entity.Word
import at.tugraz.ist.guessingwords.data.service.WordService
import at.tugraz.ist.guessingwords.logic.Game
import at.tugraz.ist.guessingwords.ui.custom_words.CustomWordsFragment
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.kotlin.any
import org.mockito.kotlin.mock
import org.mockito.kotlin.times
import org.mockito.kotlin.verify

@RunWith(AndroidJUnit4::class)
class GamePrototypeFragmentTest {

    @Test
    fun expectToSeeLoadingMessageOnScreenBeforeGameStart() {
        val wordServiceMock = mock<WordService>()
        var text = ""

        val fragmentArgs = bundleOf()
        val scenario = launchFragmentInContainer<GamePrototypeFragment>(fragmentArgs)
        scenario.onFragment { fragment ->
            text = fragment.getString(R.string.loading)
            fragment.wordService = wordServiceMock
            fragment.initFields()
        }

        Espresso.onView(ViewMatchers.withId(R.id.txt_fieldWord)).check(ViewAssertions.matches(ViewMatchers.withText(text)))
    }

    @Test
    fun expectCallToWordServiceToGetAllWords()
    {
        val wordServiceMock = mock<WordService>()

        val fragmentArgs = bundleOf()
        val scenario = launchFragmentInContainer<GamePrototypeFragment>(fragmentArgs)
        scenario.onFragment { fragment ->
            fragment.wordService = wordServiceMock
            fragment.initGame()
        }
        verify(wordServiceMock, times(1)).getAllWords(any())
    }
}