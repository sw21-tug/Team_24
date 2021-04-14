package at.tugraz.ist.guessingwords.data.service

import android.content.Context
import at.tugraz.ist.guessingwords.data.database.GWDatabase
import at.tugraz.ist.guessingwords.data.entity.Word
import kotlin.concurrent.thread

class WordService(private val context : Context) {
    fun getAllWords(callback: Callback<List<Word>>) {
        thread {
            val db = GWDatabase.getInstance(context)
            val allWords = db.wordDao().getAll()
            callback.whenReady(allWords)
        }
    }
}