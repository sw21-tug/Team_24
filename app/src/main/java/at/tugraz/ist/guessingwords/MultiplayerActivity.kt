package at.tugraz.ist.guessingwords

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast

class MultiplayerActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_multiplayer)

        val btn_host = findViewById<Button>(R.id.btn_host)

        btn_host.setOnClickListener {
            Toast.makeText(this@MultiplayerActivity, "You Clicked: Ready!", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, HostActivity::class.java)
            startActivity(intent)
        }
    }
}