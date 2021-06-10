package at.tugraz.ist.guessingwords.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable


@Entity(tableName = "words")
@Serializable
data class Word(
    @PrimaryKey(autoGenerate = true)
    val uid : Long,
    val text : String,
) {
    constructor(text: String) : this(0, text)
}