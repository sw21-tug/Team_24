package at.tugraz.ist.guessingwords.ui.start_game

import android.content.Context
import android.content.Intent
import android.media.CamcorderProfile.get
import android.media.MediaPlayer
import android.os.*
import android.text.TextUtils.replace
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import at.tugraz.ist.guessingwords.AboutPageActivity
import at.tugraz.ist.guessingwords.NextRoundScreenActivity
import at.tugraz.ist.guessingwords.R
import at.tugraz.ist.guessingwords.StartGameActivity
import at.tugraz.ist.guessingwords.data.entity.Word
import at.tugraz.ist.guessingwords.data.service.Callback
import at.tugraz.ist.guessingwords.data.service.WordService
import at.tugraz.ist.guessingwords.logic.Game
import org.w3c.dom.Text

class GamePrototypeFragment : Fragment() {

    private lateinit var startGameViewModel: StartGameViewModel
    private lateinit var root: View

    var maxTimeMillis: Long = 90999 // 91 seconds

    lateinit var wordService: WordService

    var game: Game? = null
    var timer: CountDownTimer? = null
    var score: Int = 0
    var skipped: Int = 0
    var beep_flag: Boolean = false

    private lateinit var fieldTimer: TextView
    private lateinit var fieldWord: TextView
    private lateinit var fieldWordCounter: TextView
    private lateinit var btn_skip: Button
    private lateinit var btn_correct: Button
    lateinit var timeIsUpSound: MediaPlayer
    lateinit var tenSecondBeep: MediaPlayer
    lateinit var vibrator: Vibrator

    private val staticWordList = listOf(
        Word("mobile phone"),
        Word("bicycle"),
        Word("toaster"),
        Word("movie"),
        Word("book store"),
        Word("alien"),
        Word("soccer"),
        Word("sorcerer"),
        Word("television"),
    )

    override fun onDestroyView() {
        timeIsUpSound.stop()
        tenSecondBeep.stop()
        timer?.cancel()
        super.onDestroyView()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val startGameFactory: ViewModelProvider.Factory =
            ViewModelProvider.AndroidViewModelFactory.getInstance(requireActivity().application)
        startGameViewModel =
            ViewModelProvider(this, startGameFactory).get(StartGameViewModel::class.java)
        root = inflater.inflate(R.layout.fragment_game_prototype, container, false)

        wordService = WordService(requireContext())

        timeIsUpSound = MediaPlayer.create(context, R.raw.time_up)

        tenSecondBeep = MediaPlayer.create(context, R.raw.beep)

        initFields()
        initGame()

        return root
    }

    fun initFields() {
        fieldTimer = root.findViewById(R.id.txt_fieldTimer)
        fieldWord = root.findViewById(R.id.txt_fieldWord)
        fieldWordCounter = root.findViewById(R.id.txt_fieldWordCounter)
        btn_correct = root.findViewById(R.id.btn_correctWord)
        btn_skip = root.findViewById(R.id.btn_skipWord)

        displayTimer(maxTimeMillis / 1000)
        fieldWord.text = requireActivity().getString(R.string.loading)
        displayWordCounter()

        btn_correct.setOnClickListener {
            score += 1
            displayWordCounter()
            nextWord()
        }
        btn_skip.setOnClickListener {
            skipped += 1
            nextWord()
        }
    }

    fun initGame() {
        timer = object : CountDownTimer(maxTimeMillis, 500) {
            override fun onTick(millisUntilFinished: Long) {
                val seconds: Long = millisUntilFinished / 1000
                displayTimer(seconds)
                timerSound(seconds)
                timeUpSound(seconds)
            }

            override fun onFinish() {
                displayTimer(0)
                beep_flag = false
                tenSecondBeep.stop()
            }
        }
        wordService.getAllWords(object : Callback<List<Word>> {
            override fun whenReady(data: List<Word>?) {
                if (data != null && data.isNotEmpty()) {
                    game = Game(data)
                } else {
                    game = Game(staticWordList)
                }
                displayWord()
                timer?.start()
            }
        })
    }

    fun nextWord() {
        game?.next()
        displayWord()
    }

    fun displayWord() {
        activity?.runOnUiThread {
            fieldWord.text = game?.getWord()?.text
        }
    }

    fun displayTimer(seconds: Long) {
        activity?.runOnUiThread {
            if (seconds > 0) {
                fieldTimer.text = requireActivity().getString(R.string.time_display, seconds)
            } else {
                fieldTimer.text = requireActivity().getString(R.string.time_finish)
                nextRoundScreen()
            }
        }
    }

    fun displayWordCounter() {
        activity?.runOnUiThread {
            fieldWordCounter.text = requireActivity().getString(R.string.correct_words, score)
        }
    }

    fun nextRoundScreen() {

        val intent = Intent(context, NextRoundScreenActivity::class.java)
        intent.putExtra("Score", score)
        intent.putExtra("Skipped", skipped)
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
        startActivity(intent)

    }

    fun timeUpSound(seconds: Long) {
        if (seconds <= 0.2) {
            timeIsUpSound.start()
            vibratePhone()
        }
    }


    fun timerSound(seconds: Long) {
        if (seconds <= 10 && !beep_flag) {
            tenSecondBeep.start()
            beep_flag = true
        }
    }


    fun Fragment.vibratePhone() {
        vibrator = context?.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        vibrator.cancel()
        if (Build.VERSION.SDK_INT >= 26) {
            vibrator.vibrate(VibrationEffect.createOneShot(1200, VibrationEffect.DEFAULT_AMPLITUDE))
            vibrator.hasAmplitudeControl()
        } else {
            vibrator.vibrate(1200)
        }
    }
}
