package com.example.playlistmaker.ui.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.playlistmaker.R

class SearchActivity : AppCompatActivity() {
    private var savedEditTextValue: String? = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        val btnBack = findViewById<TextView>(R.id.btn_back_search)
        btnBack.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
        }
        val searchEditText = findViewById<EditText>(R.id.search_edit_text)
        searchEditText.setText(savedEditTextValue ?: "")
        val clearTextBtn = findViewById<TextView>(R.id.btn_clear_edit_text)

        fun clearEditTextFocus() {
            searchEditText.clearFocus()
            val inputMethodManager =
                getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
            inputMethodManager?.hideSoftInputFromWindow(searchEditText.windowToken, 0)
        }

        clearTextBtn.setOnClickListener {
            searchEditText.setText("")
            clearEditTextFocus()
        }
        searchEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val input = s.toString()
                savedEditTextValue = input
                if (input.isEmpty()) clearTextBtn.visibility = GONE
                else clearTextBtn.visibility = VISIBLE
            }

            override fun afterTextChanged(s: Editable?) {}
        })

        searchEditText.setOnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                clearEditTextFocus()
                true
            } else false
        }

    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(EDIT_TEXT_VALUE_KEY, savedEditTextValue)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        savedEditTextValue = savedInstanceState.getString(EDIT_TEXT_VALUE_KEY)
    }

    companion object {
        const val EDIT_TEXT_VALUE_KEY = "EDIT_TEXT_VALUE_KEY"
    }
}