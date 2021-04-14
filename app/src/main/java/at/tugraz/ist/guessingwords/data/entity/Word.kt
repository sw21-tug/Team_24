package at.tugraz.ist.guessingwords.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "words")
data class Word(
    @PrimaryKey(autoGenerate = true)
    val uid : Long,
    val text : String,
)