package at.tugraz.ist.guessingwords.ui.custom_words

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import at.tugraz.ist.guessingwords.R
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
        return root
    }
}