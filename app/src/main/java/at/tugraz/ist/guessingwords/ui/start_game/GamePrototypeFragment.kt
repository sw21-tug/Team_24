package at.tugraz.ist.guessingwords.ui.start_game

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import at.tugraz.ist.guessingwords.R
import at.tugraz.ist.guessingwords.data.service.WordService
import at.tugraz.ist.guessingwords.logic.Game

class GamePrototypeFragment : Fragment() {

    private lateinit var startGameViewModel: StartGameViewModel
    private lateinit var root: View

    lateinit var wordService: WordService

    private var game: Game? = null
    private lateinit var fieldTimer: TextView
    private lateinit var fieldWord: TextView
    private lateinit var fieldWordCounter: TextView
    private lateinit var btn_skip: Button
    private lateinit var btn_correct: Button

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val startGameFactory: ViewModelProvider.Factory = ViewModelProvider.AndroidViewModelFactory.getInstance(activity!!.application)
        startGameViewModel = ViewModelProvider(this, startGameFactory).get(StartGameViewModel::class.java)
        root = inflater.inflate(R.layout.fragment_game_prototype, container, false)

        initFields()

        return root
    }

    private fun initFields()
    {
        fieldTimer = root.findViewById(R.id.txt_fieldTimer)
        fieldWord = root.findViewById(R.id.txt_fieldWord)
        fieldWordCounter = root.findViewById(R.id.txt_fieldWordCounter)
        btn_correct = root.findViewById(R.id.btn_correctWord)
        btn_skip = root.findViewById(R.id.btn_skipWord)

        fieldTimer.text = ""
        fieldWord.text = getString(R.string.loading)
        fieldWordCounter.text = ""
    }
}