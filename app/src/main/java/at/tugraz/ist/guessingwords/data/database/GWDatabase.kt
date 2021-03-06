package at.tugraz.ist.guessingwords.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
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

    companion object {
        var _instance: GWDatabase? = null
        var _in_memory_instance: GWDatabase? = null

        fun getInstance(context: Context): GWDatabase {
            if (_instance == null) {
                synchronized(GWDatabase::class) {
                    _instance = Room.databaseBuilder(
                        context.applicationContext,
                        GWDatabase::class.java,
                        "GWDB"
                    )
                        .fallbackToDestructiveMigration()
                        .build()
                }
            }
            return _instance!!
        }
        fun getInMemoryInstance(context: Context): GWDatabase {
            if (_in_memory_instance == null){
                synchronized(GWDatabase::class) {
                    _in_memory_instance = Room.inMemoryDatabaseBuilder(
                        context,
                        GWDatabase::class.java
                    ).build()
                }
            }
            return _in_memory_instance!!
        }

    }
}