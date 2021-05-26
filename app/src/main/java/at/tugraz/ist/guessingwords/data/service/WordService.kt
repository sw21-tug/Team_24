package at.tugraz.ist.guessingwords.data.service

import android.content.Context
import android.util.Log
import at.tugraz.ist.guessingwords.data.database.GWDatabase
import at.tugraz.ist.guessingwords.data.entity.Word
import kotlin.concurrent.thread

open class WordService(private val context : Context) {

    open fun getAllWords(callback: Callback<List<Word>>) {
        thread {
            val db = GWDatabase.getInstance(context)
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

    open fun createNewMultiplayerWordPool(callback: Callback<List<Long>>) {
        thread {
            val localWords = GWDatabase.getInstance(context).wordDao().getAll()
            val dbTemp = GWDatabase.getInMemoryInstance(context)

            val mergedIds = dbTemp.wordDao().mergeWordsIntoDB(localWords)
            callback.whenReady(mergedIds)
        }
    }

    open fun mergeIntoDatabase(merging: List<Word>, callback: Callback<List<Long>>) {
        thread {
            val db = GWDatabase.getInMemoryInstance(context)
            val mergedIds = db.wordDao().mergeWordsIntoDB(merging)
            callback.whenReady(mergedIds)
        }
    }

    open fun removeMultiplayerWordPool() {
        GWDatabase._in_memory_instance = null;
    }
}