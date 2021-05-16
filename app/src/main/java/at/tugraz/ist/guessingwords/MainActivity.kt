package at.tugraz.ist.guessingwords

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AlertDialog
import androidx.core.view.get
import android.os.Bundle
import android.view.*
import android.widget.*
import java.util.*
import kotlin.collections.ArrayList


class MainActivity : AppCompatActivity() {
    lateinit var locale: Locale
    private var currentLanguage = "en"
    private var currentLang: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val btn_startGame = findViewById<Button>(R.id.btn_startGame)
        val btn_multiplayer = findViewById<Button>(R.id.btn_multiplayer)
        val btn_customWords = findViewById<Button>(R.id.btn_customWords)

        btn_startGame.setOnClickListener {
//            Toast.makeText(MainActivity, "You Clicked: Start Game!", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, StartGameActivity::class.java)
            startActivity(intent)
        }

        btn_multiplayer.setOnClickListener {
            Toast.makeText(this@MainActivity, "You Clicked: Multiplayer!", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, StartGameActivity::class.java) // THIS IS TEMPORARY!
            startActivity(intent)
        }

        btn_customWords.setOnClickListener {
//            Toast.makeText(this@MainActivity, "You Clicked: Custom Words!", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, CustomWordsActivity::class.java)
            startActivity(intent)
        }
        currentLanguage = intent.getStringExtra(currentLang).toString()
    }

    override fun onResume() {
        super.onResume()
//        Toast.makeText(this@MainActivity, "Resumed from other Activity", Toast.LENGTH_SHORT).show()
    }

    private fun setLocale(localeName: String) {
        if (localeName != currentLanguage) {
            locale = Locale(localeName)
            val res = resources
            val dm = res.displayMetrics
            val conf = res.configuration
            conf.locale = locale
            res.updateConfiguration(conf, dm)
            val refresh = Intent(
                    this,
                    MainActivity::class.java
            )
            refresh.putExtra(currentLang, localeName)
            refresh.putExtra(currentLanguage, localeName)
            startActivity(refresh)
        } else {
            Toast.makeText(
                    this@MainActivity, "Language is already selected!", Toast.LENGTH_SHORT).show();
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.context_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.link_about_page){
            val intent = Intent(this, AboutPageActivity::class.java)
            startActivity(intent)
            return true
        } else if(item.itemId == R.id.btn_change_language) {
            openLanguageDialog();
        }

        return super.onOptionsItemSelected(item)
    }

    @Override
    fun openLanguageDialog() {
        val mDialogView = LayoutInflater.from(this).inflate(R.layout.fragment_languages_dialog, null)
        val mBuilder = AlertDialog.Builder(this)
                .setView(mDialogView)
                .setTitle(R.string.lang_dialog_title)
        var setSelected:View

        if (currentLanguage != "ru") {
            setSelected = mDialogView.findViewById<RadioButton>(R.id.lang_english)
        } else {
            setSelected = mDialogView.findViewById<RadioButton>(R.id.lang_russian)
        }
        setSelected.setChecked(true);
        val mAlertDialog = mBuilder.show()
        val button = mDialogView.findViewById<Button>(R.id.btn_language_change)
        button.setOnClickListener{
            var selected = (mDialogView.findViewById<RadioGroup>(R.id.lang_radio).checkedRadioButtonId)
            var text = mDialogView.findViewById<RadioButton>(selected).text
            if (text == "English"){
                setLocale("en")
            } else if(text == "Russian") {
                setLocale("ru")
            }
            mAlertDialog.dismiss()
        }

    }

}