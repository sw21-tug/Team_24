package at.tugraz.ist.guessingwords.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import at.tugraz.ist.guessingwords.data.dao.WordDao
import at.tugraz.ist.guessingwords.data.entity.Word

@Database(
    entities = [Word::class],
    version = 1,
    exportSchema = false
)
abstract class GWDatabase  : RoomDatabase() {

    abstract fun wordDao(): WordDao
}