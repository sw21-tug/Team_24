package at.tugraz.ist.guessingwords

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.Toast

class CustomWordsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_custom_words)

        val btn_back = findViewById<Button>(R.id.btn_back_CW)
        val btn_save = findViewById<Button>(R.id.btn_save_word)
        val text_field = findViewById<EditText>(R.id.editText_customWords)

        btn_back.setOnClickListener {
            finish()
        }


        val custom_words = mutableListOf<String>()

        btn_save.setOnClickListener {
            Toast.makeText(this@CustomWordsActivity, "You Clicked Save!", Toast.LENGTH_SHORT).show()
            var editTextString = text_field.text.toString()
            custom_words.add(editTextString) // saves the custom words into list for now, until database is ready
            closeKeyBoard()
        }
    }
    private fun closeKeyBoard() {
        val view = this.currentFocus
        if (view != null) {
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }
}
