package at.tugraz.ist.guessingwords.ui.start_game

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import at.tugraz.ist.guessingwords.R

class AboutPageFragment : Fragment() {

    private lateinit var startGameViewModel: StartGameViewModel
    private lateinit var root: View

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val startGameFactory: ViewModelProvider.Factory = ViewModelProvider.AndroidViewModelFactory.getInstance(activity!!.application)
        startGameViewModel = ViewModelProvider(this, startGameFactory).get(StartGameViewModel::class.java)
        root = inflater.inflate(R.layout.fragment_about_page, container, false)
        return root
    }
}