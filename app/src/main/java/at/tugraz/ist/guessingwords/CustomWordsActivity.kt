package at.tugraz.ist.guessingwords

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText

class CustomWordsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_custom_words)

        val btn_back = findViewById(R.id.btn_back_CW) as Button
        val btn_save = findViewById(R.id.btn_save_word) as Button
        val text_field = findViewById(R.id.editText_customWords) as EditText

        btn_back.setOnClickListener {
            finish()
        }


        val custom_words = mutableListOf<String>()

        btn_save.setOnClickListener {
            var editTextString = text_field.text.toString()
            custom_words.add(editTextString) // saves the custom words into list for now, until database is ready
        }
    }
}