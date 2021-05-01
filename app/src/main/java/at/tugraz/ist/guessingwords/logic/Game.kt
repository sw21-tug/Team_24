package at.tugraz.ist.guessingwords.logic

import at.tugraz.ist.guessingwords.data.entity.Word
import java.lang.IllegalArgumentException

class Game(val wordPool: List<Word>)
{

    private lateinit var current_word:Word
    init
    {
        if(wordPool.isEmpty())
        {
            throw IllegalArgumentException("wordpool can not be empty")
        }
        current_word = wordPool.random()
    }

    fun getWord(): Word
    {
        return current_word
    }
}
