package at.tugraz.ist.guessingwords.ui.start_game

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import at.tugraz.ist.guessingwords.R
import at.tugraz.ist.guessingwords.StartGameActivity

class NextScreenFragment : Fragment() {

    private lateinit var nextScreenViewModel: NextScreenViewModel
    private lateinit var root: View
    private lateinit var fieldCorrectGuesses: TextView
    private lateinit var fieldSkippedWords: TextView

    private lateinit var btn_nextRound : Button

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val nextScreenFactory: ViewModelProvider.Factory = ViewModelProvider.AndroidViewModelFactory.getInstance(activity!!.application)
        nextScreenViewModel = ViewModelProvider(this, nextScreenFactory).get(NextScreenViewModel::class.java)
        root = inflater.inflate(R.layout.fragment_next_round_screen, container, false)

        fieldCorrectGuesses = root.findViewById(R.id.text_correctGuesses)
        fieldSkippedWords = root.findViewById(R.id.text_skippedWords)
        btn_nextRound = root.findViewById(R.id.btn_nextRound)

        //fieldCorrectGuesses.text = requireActivity().getString(R.string.correct_guesses, score)
        //fieldSkippedWords.text = requireActivity().getString(R.string.skipped_words, skipped)
        /*btn_nextRound.setOnClickListener {

        }*/

        return root
    }
}