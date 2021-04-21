package at.tugraz.ist.guessingwords.data

import android.util.Log
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

        val allWords = db.wordDao().getAll()
        val allUids = allWords.map { w -> w.uid }
        Log.d("DBTest", allWords.toString())
        Log.d("DBTest", allUids.toString())
        Log.d("DBTest", rv1.toString())
        Log.d("DBTest", rv2.toString())
        assertTrue(rv1 != rv2)
        assertTrue(allUids.contains(rv1))
        assertTrue(allUids.contains(rv2))
    }

    @Test
    fun databaseUpdateOverridesExistingWordTextOnSameUid() {
        var wordText = "bicycle"
        var word = Word(wordText)
        val ruid = db.wordDao().insertWord(word)

        wordText = "mobile phone"
        word = Word(ruid, wordText)
        db.wordDao().updateWord(word)

        val allWords = db.wordDao().getAll()
        assertTrue(allWords.count() == 1)
        assertTrue(allWords.contains(word))
        assertTrue(allWords[0].uid == ruid)
    }

    @Test
    fun deleteExistingWordFromDataBaseResultsInEmptyDB() {
        val wordText = "picture"
        var word = Word(wordText)
        val ruid = db.wordDao().insertWord(word)

        word = Word(ruid, wordText)
        db.wordDao().deleteWord(word)

        val allWords = db.wordDao().getAll()
        assertTrue(allWords.isEmpty())
    }

    @Test
    fun returnWordByUidFromDatabaseWorks() {
        val word = Word("hello")
        val ruid = db.wordDao().insertWord(word)

        val newWord = db.wordDao().getWordByUid(ruid)

        assert(word == newWord)
        assert(ruid == newWord.uid)
    }
}