package at.tugraz.ist.guessingwords.data.service

import android.content.Context
import android.util.Log
import androidx.room.Room
import at.tugraz.ist.guessingwords.data.database.GWDatabase
import at.tugraz.ist.guessingwords.data.entity.Word
import java.lang.Exception
import kotlin.concurrent.thread

open class WordService(private val context : Context) {

    open fun getAllWords(callback: Callback<List<Word>>) {
        thread {
            var db = GWDatabase.getInstance(context)
            if(GWDatabase._in_memory_instance != null){
                Log.d("DBTEst entered if", "entered if")
                db = GWDatabase._in_memory_instance!!
            }
            val allWords = db.wordDao().getAll()
            callback.whenReady(allWords)
        }
    }

    open fun insertOrUpdateExistingWord(word: Word, callback: Callback<Long>) {
        if(word.uid == 0L){
            thread {
                val db = GWDatabase.getInstance(context)
                val returnID = db.wordDao().insertWord(word)
                callback.whenReady(returnID)
            }
        }
        else {
            thread {
                val db = GWDatabase.getInstance(context)
                db.wordDao().updateWord(word)
                callback.whenReady(word.uid)
            }
        }
    }

    open fun deleteWord(word: Word, callback: Callback<Boolean>) {
        thread {
            val db = GWDatabase.getInstance(context)
            db.wordDao().deleteWord(word)
            callback.whenReady(true)
        }
    }

    open fun getWordById(id: Long, callback: Callback<Word>) {
        thread {
            val db = GWDatabase.getInstance(context)
            val word = db.wordDao().getWordById(id)
            callback.whenReady(word)
        }
    }

    open fun createNewMultiplayerWordPool(merging: List<Word>, callback: Callback<Long>) {
        GWDatabase._in_memory_instance = Room.inMemoryDatabaseBuilder(
            context,
            GWDatabase::class.java
        ).build()
        // TODO: merge own words into this db
        // val wordList = listOf<Word>(Word("Test"), Word("Test2"), Word("Hosam"))
        //mergeIntoDatabase(merging, GWDatabase._in_memory_instance, callback)
        for(word in merging){
            insertOrUpdateExistingWord(word, callback)
        }
    }

    // TODO: might need a callback
    open fun mergeIntoDatabase(merging: List<Word>, db: GWDatabase?, callback: Callback<List<Word>>) {
        thread {
            // TODO
            if (db != null) {
                db.wordDao().mergeWordsIntoDB(merging)
                callback.whenReady(merging)
            }
        }
    }

    open fun removeMultiplayerWordPool() {
        GWDatabase._in_memory_instance = null;
    }
}