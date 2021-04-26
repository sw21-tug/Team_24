package at.tugraz.ist.guessingwords.ui.custom_words

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import at.tugraz.ist.guessingwords.R
import at.tugraz.ist.guessingwords.data.entity.Word
import at.tugraz.ist.guessingwords.data.service.Callback
import at.tugraz.ist.guessingwords.data.service.WordService
import at.tugraz.ist.guessingwords.ui.custom_words.adapters.CustomWordsAdapter

class CustomWordsFragment : Fragment() {

    private lateinit var customWordsViewModel: CustomWordsViewModel
    private lateinit var root: View

    lateinit var customWordService: WordService
    private var customWords: MutableList<Word> = mutableListOf()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val customWordsFactory: ViewModelProvider.Factory = ViewModelProvider.AndroidViewModelFactory.getInstance(activity!!.application)
        customWordsViewModel = ViewModelProvider(this, customWordsFactory).get(CustomWordsViewModel::class.java)
        root = inflater.inflate(R.layout.fragment_custom_words, container, false)

        customWordService = WordService(activity!!)
        initSetUp()
        initSaveCustomWordButton()

        return root
    }

    private fun initSaveCustomWordButton(){
        val btn_save = root.findViewById<Button>(R.id.btn_save_word)
        val text_field = root.findViewById<EditText>(R.id.editText_customWords)

        btn_save.setOnClickListener{
            var addText = text_field.text.toString()

            if (checkIfUserInputIsValid(addText)) {
                addText = prepareUserInputToSaveInDB(addText)
                var newWord = Word(addText)

                customWordService.insertOrUpdateExistingWord(newWord, object: Callback<Long>{
                    override fun whenReady(data: Long?) {
                        newWord = Word(data!!, addText)
                        customWords.add(newWord)
                        updateView(customWords)
                    }
                })
            }
            closeKeyBoard()
        }
    }

    private fun initSetUp() {
        customWordService.getAllWords(object: Callback<List<Word>> {
            override fun whenReady(data: List<Word>?) {
                if (data != null){
                    customWords.addAll(data)
                }
                updateView(customWords)
            }
        })
    }

    fun updateView (customWords: MutableList<Word>) {
        activity!!.runOnUiThread {
            displayCustomWordsList(customWords)
        }
    }

    private fun displayCustomWordsList(customWords: MutableList<Word>) {
        val lv_custom_words = root.findViewById<ListView>(R.id.lst_custom_words)

        lv_custom_words.adapter = CustomWordsAdapter(context!!, customWords)

        val countWords = customWords.size.toString() + " Words"
        root.findViewById<TextView>(R.id.tv_count_words).setText(countWords)
    }

    private fun checkIfUserInputIsValid(string: String) : Boolean {
        var valid = true

        if (string.isBlank()) {
            valid = false
            Toast.makeText(activity, "Please enter a word you would like to save!", Toast.LENGTH_SHORT).show()
        }
        root.findViewById<EditText>(R.id.editText_customWords).setText("")
        return valid
    }

    private fun prepareUserInputToSaveInDB(string: String) : String {
        var stringTmp = string

        stringTmp = stringTmp.trimStart().trimEnd()

        return stringTmp
    }

    private fun closeKeyBoard() {
        val view = activity!!.currentFocus
        if (view != null) {
            val iMm = context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            iMm.hideSoftInputFromWindow(view.windowToken, 0)
            view.clearFocus()
        }
    }
}