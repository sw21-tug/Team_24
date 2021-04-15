package at.tugraz.ist.guessingwords.data.service

import android.content.Context
import at.tugraz.ist.guessingwords.data.database.GWDatabase
import at.tugraz.ist.guessingwords.data.entity.Word
import java.lang.Exception
import kotlin.concurrent.thread

class WordService(private val context : Context) {

    fun getAllWords(callback: Callback<List<Word>>) {
        thread {
            val db = GWDatabase.getInstance(context)
            val allWords = db.wordDao().getAll()
            callback.whenReady(allWords)
        }
    }

    fun insertOrUpdateExistingWord(word: Word, callback: Callback<Long>) {
        if(word.uid == 0L){
            thread {
                val db = GWDatabase.getInstance(context)
                val returnID = db.wordDao().insertWord(word)
                callback.whenReady(returnID)
            }
        }
        else{
            throw NotImplementedError("Updating not implemented yet")
        }
    }
}