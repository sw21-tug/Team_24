package at.tugraz.ist.guessingwords.data.dao

import androidx.room.Dao
import androidx.room.Query
import at.tugraz.ist.guessingwords.data.entity.Word
@Dao
interface WordDao {
    @Query("SELECT * FROM words")
    fun getAll(): List<Word>
}