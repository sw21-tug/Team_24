package at.tugraz.ist.guessingwords.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "words")
data class Word(
    @PrimaryKey(autoGenerate = true)
    val uid : Long,
    var text : String,
) {
    constructor(text: String) : this(0, text)

    override fun equals(other: Any?): Boolean
        = (other is Word) && text == other.text
}