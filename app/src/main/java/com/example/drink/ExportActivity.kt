package com.example.drink

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.drink.ui.SharedViewModel
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_export.*


class ExportActivity : AppCompatActivity() {
    private lateinit var sharedViewModel: SharedViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_export)
        text_result.movementMethod = ScrollingMovementMethod()

        sharedViewModel =
            ViewModelProviders.of(this@ExportActivity).get(SharedViewModel::class.java)
        sharedViewModel.drinks.observe(this, Observer {
            this.text_result.text = Gson().toJson(it)
        })

        this.clipboard_button.setOnClickListener {
            val clipboard: ClipboardManager =
                getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            val clip: ClipData = ClipData.newPlainText("", this.text_result.text)
            clipboard.setPrimaryClip(clip)
            Toast.makeText(applicationContext, "Saved to clipboard!", Toast.LENGTH_SHORT).show()
        }
    }
}

