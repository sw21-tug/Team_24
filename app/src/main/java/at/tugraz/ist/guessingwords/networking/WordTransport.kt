package at.tugraz.ist.guessingwords.networking

import at.tugraz.ist.guessingwords.data.entity.Word
import kotlinx.serialization.Serializable

@Serializable
data class WordTransport(
    val name: String,
    val words: List<Word>
)