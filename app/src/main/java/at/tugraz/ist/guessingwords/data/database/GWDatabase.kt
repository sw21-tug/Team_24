package at.tugraz.ist.guessingwords.data.database

import androidx.room.RoomDatabase
import at.tugraz.ist.guessingwords.data.dao.WordDao

abstract class GWDatabase  : RoomDatabase() {

    abstract fun wordDao(): WordDao
}