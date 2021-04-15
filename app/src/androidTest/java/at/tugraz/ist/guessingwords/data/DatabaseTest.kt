package at.tugraz.ist.guessingwords.data

import androidx.room.Room
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import at.tugraz.ist.guessingwords.data.database.GWDatabase
import at.tugraz.ist.guessingwords.data.entity.Word
import junit.framework.Assert.assertTrue
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class DatabaseTest {

    private lateinit var db: GWDatabase

    private fun getContext() = InstrumentationRegistry.getInstrumentation().targetContext

    @Before
    fun setup() {
        db = Room.inMemoryDatabaseBuilder(
                getContext(),
                GWDatabase::class.java
            ).build()
    }

    @After
    fun teardown() {
        db.close()
    }

    @Test
    fun gettingAllWordsFromAnEmptyDatabaseReturnsEmptyList() {
        val allWords = db.wordDao().getAll()

        assertTrue(allWords.isEmpty())
    }

    @Test
    fun elementInsertedIntoDatabaseIsReturned() {
        val word = Word("anything")

        db.wordDao().insertWord(word)

        val allWords = db.wordDao().getAll()
        assertTrue(allWords.contains(word))
    }

    @Test
    fun databaseInsertReturnsCreatedPrimaryKey(){
        val word = Word("a word")

        val rv1 = db.wordDao().insertWord(word)
        val rv2 = db.wordDao().insertWord(word)

        assertTrue(rv1 != rv2)

        val allUids = db.wordDao().getAll().map { word.uid }
        assertTrue(allUids.contains(rv1))
        assertTrue(allUids.contains(rv2))
    }
}