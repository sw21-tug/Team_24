package at.tugraz.ist.guessingwords

import android.view.View
import android.widget.TextView
import androidx.core.os.bundleOf
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.ext.junit.runners.AndroidJUnit4
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
class GamePlayActivityTest {

    lateinit var textSaver: ViewAction

    @Before
    fun setup() {
        // this matcher simply saves the text of a matched TextView and returns it with toString()
        // (why does this not already exist?! it is obviously missing)
        textSaver = object : ViewAction {
            var content: String = ""

            override fun getConstraints(): Matcher<View> {
                return object : BaseMatcher<View>() {
                    override fun describeTo(description: Description?) {

                    }

                    override fun matches(item: Any?): Boolean {
                        return item != null && item is TextView
                    }
                }
            }

            override fun getDescription(): String {
                return "custom TextView matcher"
            }

            override fun perform(uiController: UiController?, view: View?) {
                val tv: TextView = view as TextView
                content = tv.text.toString()
            }

            override fun toString(): String {
                return content
            }
        }
    }

    @Test
    fun expectToSeeLoadingMessageOnScreenBeforeGameStart() {
        val wordServiceMock = mock<WordService>()
        var text = ""
        val fragmentArgs = bundleOf()
        val scenario = launchFragmentInContainer<GamePlayFragment>(fragmentArgs)

        scenario.onFragment { fragment ->
            text = fragment.getString(R.string.loading)
            fragment.wordService = wordServiceMock
            // run initFields again (first time was on launchFragment)
            // this time with the mocked service so whenReady is never called (so we see loading)
            fragment.initFields()
        }

        onView(ViewMatchers.withId(R.id.txt_fieldWord))
                .check(ViewAssertions.matches(ViewMatchers.withText(text)))
    }

    @Test
    fun expectCallToWordServiceToGetAllWords()
    {
        val wordServiceMock = mock<WordService>()
        val fragmentArgs = bundleOf()
        val scenario = launchFragmentInContainer<GamePlayFragment>(fragmentArgs)

        scenario.onFragment { fragment ->
            fragment.wordService = wordServiceMock
            // run initGame again (first time was on launchFragment)
            // this time with the mocked service so we can check if getAllWords was actually called
            fragment.initGame()
        }

        verify(wordServiceMock, times(1)).getAllWords(any())
    }

    @Test
    fun expectToSeeWordFromWordServiceAfterInitGameWasCalled() {
        val wordServiceMock = mock<WordService>()
        val argCapt: KArgumentCaptor<Callback<List<Word>>> = argumentCaptor()
        val fragmentArgs = bundleOf()
        val scenario = launchFragmentInContainer<GamePlayFragment>(fragmentArgs)

        scenario.onFragment { fragment ->
            fragment.wordService = wordServiceMock
            // run initGame again (first time was on launchFragment)
            // this time with the mocked service so we can control the callback values
            fragment.initGame()
        }

        val word = Word("testit")

        // capture the callback object and manually call whenReady with our own data
        verify(wordServiceMock).getAllWords(argCapt.capture())
        argCapt.firstValue.whenReady(listOf(word))

        onView(ViewMatchers.withId(R.id.txt_fieldWord))
                .check(ViewAssertions.matches(ViewMatchers.withText(word.text)))
    }

    @Test
    fun expectNextRoundActivityAfterTimerReachesZero()
    {
        val wordServiceMock = mock<WordService>()
        val argCapt: KArgumentCaptor<Callback<List<Word>>> = argumentCaptor()
        val fragmentArgs = bundleOf()
        val scenario = launchFragmentInContainer<GamePlayFragment>(fragmentArgs)
        var text = ""

        scenario.onFragment { fragment ->
            text = fragment.getString(R.string.time_finish)
            fragment.wordService = wordServiceMock
            fragment.maxTimeMillis = 0
            // run initGame again (first time was on launchFragment)
            // this time with the mocked service so whenReady can be called to start the timer
            fragment.initGame()
        }

        verify(wordServiceMock).getAllWords(argCapt.capture())
        argCapt.firstValue.whenReady(null)

        onView(ViewMatchers.withId(R.id.btn_nextRound))
                .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }

    @Test
    fun expectToReturnToGameAfterNextRound()
    {
        val wordServiceMock = mock<WordService>()
        val argCapt: KArgumentCaptor<Callback<List<Word>>> = argumentCaptor()
        val fragmentArgs = bundleOf()
        val scenario = launchFragmentInContainer<GamePlayFragment>(fragmentArgs)
        var text = ""

        scenario.onFragment { fragment ->
            text = fragment.getString(R.string.time_finish)
            fragment.wordService = wordServiceMock
            fragment.maxTimeMillis = 0
            // run initGame again (first time was on launchFragment)
            // this time with the mocked service so whenReady can be called to start the timer
            fragment.initGame()
        }

        verify(wordServiceMock).getAllWords(argCapt.capture())
        argCapt.firstValue.whenReady(null)

        onView(ViewMatchers.withId(R.id.btn_nextRound))
            .perform(click())

        onView(ViewMatchers.withId(R.id.btn_skipWord)).check(
            ViewAssertions.matches(
                ViewMatchers.isDisplayed()
            )
        )
        onView(ViewMatchers.withId(R.id.btn_correctWord)).check(
            ViewAssertions.matches(
                ViewMatchers.isDisplayed()
            )
        )
    }

    @Test
    fun expectTimerToTakeOnMaxTimeValue()
    {
        val fragmentArgs = bundleOf()
        val scenario = launchFragmentInContainer<GamePlayFragment>(fragmentArgs)
        var text = ""
        val time: Long = 36000

        scenario.onFragment { fragment ->
            text = fragment.getString(R.string.time_display, time / 1000)
            fragment.maxTimeMillis = time
            fragment.timer?.cancel()
            fragment.timer = null
            // run initFields again (first time was on launchFragment)
            // this time with the timer canceled so we get the initial timer value
            fragment.initFields()
        }

        onView(ViewMatchers.withId(R.id.txt_fieldTimer))
                .check(ViewAssertions.matches(ViewMatchers.withText(text)))
    }

    @Test
    fun expectTimerTextToChangeAfterAtMostOnceSecond()
    {
        val fragmentArgs = bundleOf()
        val scenario = launchFragmentInContainer<GamePlayFragment>(fragmentArgs)
        var textInit = ""
        var textFin = ""
        val time: Long = 36000

        scenario.onFragment { fragment ->
            textInit = fragment.getString(R.string.time_display, time / 1000)
            textFin = fragment.getString(R.string.time_finish)
            fragment.maxTimeMillis = time
            fragment.timer?.cancel()
            fragment.timer = null
            fragment.initFields()
            fragment.initGame()
        }

        Thread.sleep(1000)
        onView(ViewMatchers.withId(R.id.txt_fieldTimer))
                .perform(textSaver)
        val timerValue = textSaver.toString()
        assert(timerValue != textInit)
        assert(timerValue != textFin)
    }

    @Test
    fun expectToSeeZeroCorrectGuessingAtStart()
    {
        val fragmentArgs = bundleOf()
        val scenario = launchFragmentInContainer<GamePlayFragment>(fragmentArgs)
        var text = ""

        scenario.onFragment { fragment ->
            text = fragment.getString(R.string.correct_words, 0)
        }

        onView(ViewMatchers.withId(R.id.txt_fieldWordCounter))
                .check(ViewAssertions.matches(ViewMatchers.withText(text)))
    }

    @Test
    fun expectToSeeNumberOfCorrectGuessesIncreasingAfterClickingTheCorrectButton()
    {
        val fragmentArgs = bundleOf()
        val scenario = launchFragmentInContainer<GamePlayFragment>(fragmentArgs)
        var text = ""

        scenario.onFragment { fragment ->
            text = fragment.getString(R.string.correct_words, 1)
        }

        onView(ViewMatchers.withId(R.id.btn_correctWord))
                .check(ViewAssertions.matches(ViewMatchers.isClickable()))
                .perform(ViewActions.click())
        onView(ViewMatchers.withId(R.id.txt_fieldWordCounter))
                .check(ViewAssertions.matches(ViewMatchers.withText(text)))
    }

    @Test
    fun expectToNotSeeNumberOfCorrectGuessesIncreasingAfterClickingTheSkipButton()
    {
        val fragmentArgs = bundleOf()
        launchFragmentInContainer<GamePlayFragment>(fragmentArgs)

        // save current text in wordCounter
        onView(ViewMatchers.withId(R.id.txt_fieldWordCounter))
                .perform(textSaver)
        // skip once
        onView(ViewMatchers.withId(R.id.btn_skipWord))
                .check(ViewAssertions.matches(ViewMatchers.isClickable()))
                .perform(ViewActions.click())
        // compare old wordCounter value with this one (they should be identical)
        onView(ViewMatchers.withId(R.id.txt_fieldWordCounter))
                .check(ViewAssertions.matches(ViewMatchers.withText(textSaver.toString())))
    }

    @Test
    fun expectWordToChangeOnClickingTheCorrectOrSkipButton()
    {
        val wordServiceMock = mock<WordService>()
        val argCapt: KArgumentCaptor<Callback<List<Word>>> = argumentCaptor()
        val fragmentArgs = bundleOf()
        val scenario = launchFragmentInContainer<GamePlayFragment>(fragmentArgs)

        scenario.onFragment { fragment ->
            fragment.wordService = wordServiceMock
            // run initGame again (first time was on launchFragment)
            // this time with the mocked service so a new game with our words is created on initGame
            fragment.initGame()
        }

        verify(wordServiceMock).getAllWords(argCapt.capture())
        argCapt.firstValue.whenReady(listOf(Word("one"), Word("two"), Word("three")))

        // save current word
        onView(ViewMatchers.withId(R.id.txt_fieldWord))
                .perform(textSaver)
        val word1 = textSaver.toString()
        // correct once
        onView(ViewMatchers.withId(R.id.btn_correctWord))
                .check(ViewAssertions.matches(ViewMatchers.isClickable()))
                .perform(ViewActions.click())
        // save new word (should be different from word1)
        onView(ViewMatchers.withId(R.id.txt_fieldWord))
                .perform(textSaver)
        val word2 = textSaver.toString()
        // skip once
        onView(ViewMatchers.withId(R.id.btn_skipWord))
                .check(ViewAssertions.matches(ViewMatchers.isClickable()))
                .perform(ViewActions.click())
        // save new word (should be different from other two)
        onView(ViewMatchers.withId(R.id.txt_fieldWord))
                .perform(textSaver)
        val word3 = textSaver.toString()
        assert(word1 != word2)
        assert(word1 != word3)
        assert(word2 != word3)
    }
}