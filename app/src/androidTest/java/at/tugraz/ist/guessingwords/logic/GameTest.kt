package at.tugraz.ist.guessingwords.logic

import androidx.test.ext.junit.runners.AndroidJUnit4
import at.tugraz.ist.guessingwords.data.entity.Word
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class GameTest {

    @Test
    fun testConstructingNewGameAndReturningAWordFromInputList() {
        val words = listOf<Word>(Word("hallo"), Word("welt"))
        val game = Game(words)

        val word = game.getWord()

        assert(words.contains(word))
    }

    @Test
    fun testChangeToDifferentWordonNextCall()
    {
        val words = listOf<Word>(Word("hallo"), Word("welt"))
        val game = Game(words)

        val word_1 = game.getWord()
        game.next()

        val word_2 = game.getWord()

        assert(word_1 != word_2)

    }

}