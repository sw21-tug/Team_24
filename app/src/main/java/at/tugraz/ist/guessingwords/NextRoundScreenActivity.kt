package at.tugraz.ist.guessingwords

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat.startActivity
import androidx.lifecycle.ViewModelProvider
import at.tugraz.ist.guessingwords.ui.start_game.NextScreenViewModel


class NextRoundScreenActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_next_round)

        val btn_Quit = findViewById<Button>(R.id.btn_Quit)
        btn_Quit.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
        customizeActionBar()
    }

    private fun customizeActionBar() {
        supportActionBar?.title = getString(R.string.next_round_screen)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

}