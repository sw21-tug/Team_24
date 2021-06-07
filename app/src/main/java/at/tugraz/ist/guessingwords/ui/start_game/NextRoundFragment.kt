package at.tugraz.ist.guessingwords.ui.start_game

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import at.tugraz.ist.guessingwords.R
import at.tugraz.ist.guessingwords.GamePlayActivity

class NextRoundFragment : Fragment() {

    private lateinit var root: View
    private lateinit var fieldCorrectGuesses: TextView
    private lateinit var fieldSkippedWords: TextView

    private lateinit var btn_nextRound : Button

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        root = inflater.inflate(R.layout.fragment_next_round_screen, container, false)

        fieldCorrectGuesses = root.findViewById(R.id.text_correctGuesses)
        fieldSkippedWords = root.findViewById(R.id.text_skippedWords)
        btn_nextRound = root.findViewById<Button>(R.id.btn_nextRound)


        val score = activity!!.intent.getIntExtra("Score", 0)
        val skipped = activity!!.intent.getIntExtra("Skipped", 0)


        fieldCorrectGuesses.text = requireActivity().getString(R.string.correct_guesses, score)
        fieldSkippedWords.text = requireActivity().getString(R.string.skipped_words, skipped)

        btn_nextRound.setOnClickListener {
            val intent = Intent(context, GamePlayActivity::class.java)
            startActivity(intent)
        }

        return root
    }

}