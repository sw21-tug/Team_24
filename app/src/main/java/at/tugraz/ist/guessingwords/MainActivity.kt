package at.tugraz.ist.guessingwords

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.widget.Button
import android.widget.Toast


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val btn_startGame = findViewById<Button>(R.id.btn_startGame)
        val btn_customWords = findViewById<Button>(R.id.btn_customWords)

        btn_startGame.setOnClickListener {
//            Toast.makeText(MainActivity, "You Clicked: Start Game!", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, StartGameActivity::class.java)
            startActivity(intent)
        }

        btn_customWords.setOnClickListener {
//            Toast.makeText(this@MainActivity, "You Clicked: Custom Words!", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, CustomWordsActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onResume() {
        super.onResume()
//        Toast.makeText(this@MainActivity, "Resumed from other Activity", Toast.LENGTH_SHORT).show()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.context_menu, menu)
        return true
    }


}