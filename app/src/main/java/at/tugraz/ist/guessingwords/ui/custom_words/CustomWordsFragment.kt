package at.tugraz.ist.guessingwords.ui.custom_words

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.fragment.app.Fragment
import at.tugraz.ist.guessingwords.R
import at.tugraz.ist.guessingwords.data.entity.Word
import at.tugraz.ist.guessingwords.data.service.Callback
import at.tugraz.ist.guessingwords.data.service.WordService
import at.tugraz.ist.guessingwords.ui.custom_words.adapters.CustomWordsAdapter

class CustomWordsFragment : Fragment() {

    private lateinit var root: View

    lateinit var customWordService: WordService
    private var customWords: MutableList<Word> = mutableListOf()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        root = inflater.inflate(R.layout.fragment_custom_words, container, false)

        customWordService = WordService(requireActivity())
        initSetUp()
        initSaveCustomWordButton()
        initEditOrDeleteWord()

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

    private fun initEditOrDeleteWord()  {
        val lv_custom_words = root.findViewById<ListView>(R.id.lst_custom_words)


        lv_custom_words.setOnItemLongClickListener {parent, view, position, id ->
            val btn_edit_CW = lv_custom_words.getChildAt(position - lv_custom_words.firstVisiblePosition).findViewById<Button>(R.id.btn_edit_CW)
            val btn_delete_CW = lv_custom_words.getChildAt(position - lv_custom_words.firstVisiblePosition).findViewById<Button>(R.id.btn_delete_CW)

            btn_edit_CW.visibility = View.VISIBLE
            btn_delete_CW.visibility = View.VISIBLE

            btn_edit_CW.setOnClickListener(){
                val word = lv_custom_words.adapter.getItem(position) as Word
                root.findViewById<EditText>(R.id.editText_customWords).setText(word.text)

                val saveBtnUpdate = root.findViewById<Button>(R.id.btn_save_word)
                val cancelBtnUpdate = root.findViewById<Button>(R.id.btn_cancel_word)
                cancelBtnUpdate.visibility = View.VISIBLE

                btn_edit_CW.visibility = View.GONE
                btn_delete_CW.visibility = View.GONE

                saveBtnUpdate.setOnClickListener(){

                    val updatedInput = root.findViewById<EditText>(R.id.editText_customWords).text.toString()
                    if (checkIfUserInputIsValid(updatedInput)) {
                        val updatedWord = Word(word.uid, updatedInput)
                        customWordService.insertOrUpdateExistingWord(updatedWord, object: Callback<Long>{
                            override fun whenReady(data: Long?) {
                                val index = customWords.indexOf(word)
                                customWords[index] = updatedWord
                                updateView(customWords)
                            }
                        })
                    }

                    closeKeyBoard()
                    cancelBtnUpdate.visibility = View.GONE
                    initSaveCustomWordButton()
                }

                cancelBtnUpdate.setOnClickListener(){
                    root.findViewById<EditText>(R.id.editText_customWords).setText("")
                    closeKeyBoard()
                    cancelBtnUpdate.visibility = View.GONE
                    initSaveCustomWordButton()
                }
            }

            btn_delete_CW.setOnClickListener() {
                val word = lv_custom_words.adapter.getItem(position) as Word

                customWordService.deleteWord(word, object : Callback<Boolean> {
                    override fun whenReady(data: Boolean?) {
                        customWords.remove(word)
                        updateView(customWords)

                    }
                })
                (lv_custom_words.adapter as CustomWordsAdapter).notifyDataSetChanged()
            }

            true
        }
    }

    fun updateView (customWords: MutableList<Word>) {
        requireActivity().runOnUiThread {
            displayCustomWordsList(customWords)
        }
    }

    private fun displayCustomWordsList(customWords: MutableList<Word>) {
        val lv_custom_words = root.findViewById<ListView>(R.id.lst_custom_words)
        if (customWords.isNotEmpty()) {
            lv_custom_words.visibility = View.VISIBLE
            lv_custom_words.adapter = CustomWordsAdapter(requireContext(), customWords)
        } else {
            lv_custom_words.visibility = View.GONE
        }

        val countWords = customWords.size.toString() + getString(R.string.words_counter_list)
        root.findViewById<TextView>(R.id.tv_count_words).setText(countWords)
    }

    private fun checkIfUserInputIsValid(string: String) : Boolean {
        var valid = true

        if (string.isBlank()) {
            valid = false
            Toast.makeText(activity, R.string.addWordErrorMessage, Toast.LENGTH_SHORT).show()
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
        val view = requireActivity().currentFocus
        if (view != null) {
            val iMm = context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            iMm.hideSoftInputFromWindow(view.windowToken, 0)
            view.clearFocus()
        }
    }
}