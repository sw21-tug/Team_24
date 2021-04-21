package at.tugraz.ist.guessingwords.data.dao

import androidx.room.*
import at.tugraz.ist.guessingwords.data.entity.Word

@Dao
interface WordDao {

    @Query("SELECT * FROM words")
    fun getAll(): List<Word>

    @Insert
    fun insertWord(word: Word): Long

    @Update
    fun updateWord(word: Word)

    @Delete
    fun deleteWord(word: Word)
}