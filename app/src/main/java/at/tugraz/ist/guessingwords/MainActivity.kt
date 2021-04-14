package at.tugraz.ist.guessingwords

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val btn_startGame = findViewById<Button>(R.id.btn_startGame)
        val btn_customWords = findViewById(R.id.btn_customWords) as Button

       btn_startGame.setOnClickListener {
            Toast.makeText(this@MainActivity, "You Clicked: Start Game!", Toast.LENGTH_SHORT).show()
       }

       btn_customWords.setOnClickListener {
           Toast.makeText(this@MainActivity, "You Clicked: Custom Words!", Toast.LENGTH_SHORT).show()
       }

    }
}