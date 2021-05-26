package at.tugraz.ist.guessingwords.data.dao

import androidx.room.*
import at.tugraz.ist.guessingwords.data.entity.Word

@Dao
interface WordDao {

    @Query("SELECT * FROM words")
    fun getAll(): List<Word>

    @Query("SELECT * FROM words WHERE uid = :id ")
    fun getWordById(id: Long): Word

    @Insert
    fun insertWord(word: Word): Long

    @Update
    fun updateWord(word: Word)

    @Delete
    fun deleteWord(word: Word)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun mergeWordsIntoDB(wordList: List<Word>): List<Long>
}