package at.tugraz.ist.guessingwords.data.dao

import at.tugraz.ist.guessingwords.data.entity.Word

interface WordDao {

    fun getAll(): List<Word>
}