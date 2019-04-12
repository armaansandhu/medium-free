package com.armaan.mediumfree

import android.content.Intent
import android.graphics.Typeface
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.KeyEvent
import android.view.View

import android.os.Build
import android.support.annotation.RequiresApi
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.TextView

import com.google.firebase.analytics.FirebaseAnalytics


class MainActivity : AppCompatActivity(), TextView.OnEditorActionListener {


    internal lateinit var urlInput: EditText
    private var mFirebaseAnalytics: FirebaseAnalytics? = null

    private val editorActionListener = TextView.OnEditorActionListener { v, actionId, event ->
        if (actionId == EditorInfo.IME_ACTION_SEARCH) {
            val intent = Intent(applicationContext, Read::class.java)
            intent.putExtra("url", urlInput.text.toString())
            startActivity(intent)
        }
        false
    }

    fun linkSummarize(view: View) {
        val intent = Intent(this, Read::class.java)
        intent.putExtra("url", urlInput.text.toString())
        startActivity(intent)
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this)
        urlInput = findViewById(R.id.urlInput) as EditText
        urlInput.setOnEditorActionListener(editorActionListener)
    }

    override fun onEditorAction(v: TextView, actionId: Int, event: KeyEvent): Boolean {
        return false
    }
}
