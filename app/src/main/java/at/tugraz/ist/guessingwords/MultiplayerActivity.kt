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
        val btn_join = findViewById<Button>(R.id.btn_join)

        btn_host.setOnClickListener {
            // Toast.makeText(this@MultiplayerActivity, "You Clicked: host!", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, HostActivity::class.java)
            startActivity(intent)
        }
        btn_join.setOnClickListener {
            // Toast.makeText(this@MultiplayerActivity, "You Clicked: join!", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, JoinActivity::class.java)
            startActivity(intent)
        }

    }
}