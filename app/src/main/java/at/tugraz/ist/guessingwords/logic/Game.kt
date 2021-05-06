package at.tugraz.ist.guessingwords.logic

import at.tugraz.ist.guessingwords.data.entity.Word
import java.lang.IllegalArgumentException

class Game(val wordPool: List<Word>)
{
    private val used_indices = HashSet<Int>()
    private var current_word: Word

    init
    {
        if(wordPool.isEmpty())
        {
            throw IllegalArgumentException("wordpool can not be empty")
        }
        val index = wordPool.indices.random()
        used_indices.add(index)
        current_word = wordPool[index]
    }

    fun getWord(): Word
    {
        return current_word
    }

    fun next()
    {
        if (wordPool.size == used_indices.size) {
            // all words were used
            used_indices.clear()
        }
        val usable = wordPool.indices.subtract(used_indices)
        val index = usable.random()
        used_indices.add(index)
        current_word = wordPool[index]
    }
}
