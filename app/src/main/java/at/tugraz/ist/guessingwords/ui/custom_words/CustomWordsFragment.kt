package at.tugraz.ist.guessingwords.ui.custom_words

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import at.tugraz.ist.guessingwords.R
import at.tugraz.ist.guessingwords.data.entity.Word
import at.tugraz.ist.guessingwords.data.service.Callback
import at.tugraz.ist.guessingwords.data.service.WordService

class CustomWordsFragment : Fragment() {

    private lateinit var customWordsViewModel: CustomWordsViewModel
    private lateinit var root: View

    lateinit var customWordService: WordService

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val customWordsFactory: ViewModelProvider.Factory = ViewModelProvider.AndroidViewModelFactory.getInstance(activity!!.application)
        customWordsViewModel = ViewModelProvider(this, customWordsFactory).get(CustomWordsViewModel::class.java)
        root = inflater.inflate(R.layout.fragment_custom_words, container, false)

        initSaveCustomWord()
        return root
    }

    fun initSaveCustomWord(){
        customWordService = WordService(activity!!)
        val btn_save = root.findViewById<Button>(R.id.btn_save_word)
        val text_field = root.findViewById<EditText>(R.id.editText_customWords)

        btn_save.setOnClickListener{
            Toast.makeText(activity, "You clicked save", Toast.LENGTH_SHORT).show()
            var addText = text_field.text.toString()
            var newWord = Word(addText)

            customWordService.insertOrUpdateExistingWord(newWord, object: Callback<Long>{
                override fun whenReady(data: Long?) {
                    newWord = Word(data!!, addText)
                    // TODO add word to view
                }
            })
            closeKeyBoard()
        }
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