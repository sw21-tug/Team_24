package at.tugraz.ist.guessingwords.ui.multiplayer

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import at.tugraz.ist.guessingwords.HostActivity
import at.tugraz.ist.guessingwords.JoinActivity
import at.tugraz.ist.guessingwords.R
import at.tugraz.ist.guessingwords.data.service.WordService

class MultiplayerFragment : Fragment() {

    private lateinit var root: View
    private lateinit var user_name: TextView

    lateinit var mpWordService: WordService

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        root = inflater.inflate(R.layout.fragment_multiplayer, container, false)

        mpWordService = WordService(requireContext())
        mpWordService.removeMultiplayerWordPool()

        val btn_host = root.findViewById<Button>(R.id.btn_host)
        val btn_join = root.findViewById<Button>(R.id.btn_join)

        user_name = root.findViewById<TextView>(R.id.editText_multiplayer)

        btn_host.setOnClickListener {
            val intent = Intent(context, HostActivity::class.java)
            intent.putExtra("UserNameHost", user_name.text.toString())
            startActivity(intent)
        }
        btn_join.setOnClickListener {
            val intent = Intent(context, JoinActivity::class.java)
            intent.putExtra("UserNameJoin", user_name.text.toString())
            startActivity(intent)
        }

        return root
    }

    override fun onResume() {
        mpWordService.removeMultiplayerWordPool()
        super.onResume()
    }


}