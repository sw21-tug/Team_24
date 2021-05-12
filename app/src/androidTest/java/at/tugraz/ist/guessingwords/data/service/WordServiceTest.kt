package at.tugraz.ist.guessingwords.data.service

import android.os.ConditionVariable
import androidx.room.Room
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import at.tugraz.ist.guessingwords.data.database.GWDatabase
import at.tugraz.ist.guessingwords.data.entity.Word
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class WordServiceTest {

    private lateinit var service : WordService

    private fun getContext() = InstrumentationRegistry.getInstrumentation().targetContext

    @Before
    fun setup(){
        GWDatabase._instance = Room.inMemoryDatabaseBuilder(
            getContext(),
            GWDatabase::class.java
        ).build()
        service = WordService(getContext())
    }

    @Test(timeout = 3000)
    fun testCallbackIsCalledBackOnGetAllWords(){
        val cb = object:Callback<List<Word>>{
            val finished = ConditionVariable()
            override fun whenReady(data: List<Word>?) {
                finished.open()
            }
        }
        service.getAllWords(cb)
        cb.finished.block()
    }

    @Test(timeout = 3000)
    fun callbackOnInsertWordIsCalled(){
        val word = Word("don't try this at home, kids")
        val cb = object:Callback<Long>{
            val finished = ConditionVariable()
            override fun whenReady(data: Long?) {
                finished.open()
            }
        }
        service.insertOrUpdateExistingWord(word, cb)
        cb.finished.block()
    }

    @Test(timeout = 3000)
    fun callbackOnUpdateWordIsCalled(){
        var word = Word("new Word")
        val cb = object:Callback<Long>{
            val finished = ConditionVariable()
            var uid: Long = 0
            override fun whenReady(data: Long?) {
                uid = data!!
                finished.open()
            }
        }
        service.insertOrUpdateExistingWord(word, cb)
        cb.finished.block()
        assert(cb.uid != 0L)
        word = Word(cb.uid, "pencil")
        cb.finished.close()
        service.insertOrUpdateExistingWord(word, cb)
        cb.finished.block()
    }

    @Test(timeout = 3000)
    fun callbackOnDeleteWordIsCalled(){
        val wordText = "new Word"
        var word = Word(wordText)
        val cb = object:Callback<Long>{
            val finished = ConditionVariable()
            var uid: Long = 0
            override fun whenReady(data: Long?) {
                uid = data!!
                finished.open()
            }
        }
        service.insertOrUpdateExistingWord(word, cb)
        cb.finished.block()
        assert(cb.uid != 0L)
        word = Word(cb.uid, wordText)

        val cbDelete = object:Callback<Boolean>{
            val finished = ConditionVariable()
            override fun whenReady(data: Boolean?) {
                finished.open()
            }
        }
        service.deleteWord(word, cbDelete)
        cbDelete.finished.block()
    }

    @Test(timeout = 3000)
    fun callbackOnGetWordByIdReturnsCorrectWord(){
        val wordText = "new Word"
        var word = Word(wordText)
        val cb = object:Callback<Long>{
            val finished = ConditionVariable()
            var uid: Long = 0
            override fun whenReady(data: Long?) {
                uid = data!!
                finished.open()
            }
        }
        service.insertOrUpdateExistingWord(word, cb)
        cb.finished.block()
        assert(cb.uid != 0L)

        val cbGet = object:Callback<Word>{
            val finished = ConditionVariable()
            var retWord: Word? = null
            override fun whenReady(data: Word?) {
                retWord = data
                finished.open()
            }
        }
        service.getWordById(cb.uid, cbGet)
        cbGet.finished.block()
        assert(cbGet.retWord != null)
        assert(cbGet.retWord!!.text == word.text)
        assert(cbGet.retWord!!.uid == cb.uid)
    }
}