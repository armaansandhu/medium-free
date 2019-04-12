package com.armaan.mediumfree

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.webkit.CookieManager

import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.ProgressBar
import android.widget.TextView

import com.google.firebase.analytics.FirebaseAnalytics

import java.util.ArrayList
import java.util.regex.Matcher
import java.util.regex.Pattern

class Read : AppCompatActivity() {

    internal var url = ""
    internal lateinit var textView: TextView
    internal lateinit var view: WebView
    internal lateinit var mProgressBar: ProgressBar

    private var mFirebaseAnalytics: FirebaseAnalytics? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_read)


        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this)
        textView = findViewById(R.id.textView) as TextView
        mProgressBar = findViewById(R.id.progressBar) as ProgressBar

        view = findViewById(R.id.web) as WebView
        view.visibility = View.GONE
        view.webViewClient = object : WebViewClient() {
            override fun onPageFinished(view: WebView, url: String) {
                super.onPageFinished(view, url)

                view.loadUrl(
                    "javascript:(function() { " +
                            "document.getElementsByClassName('postMeterBar')[0].style.display='none';" +
                            "})()"
                )
                mProgressBar.visibility = ProgressBar.GONE
                view.visibility = View.VISIBLE
            }
        }
        val settings = view.settings
        settings.javaScriptEnabled = true
        val intent = intent
        val action = intent.action
        val type = intent.type

        if (Intent.ACTION_SEND == action && type != null) {
            if ("text/plain" == type) {
                handleSendText(intent)
            }
        } else {
            removeCookies()
            url = intent.getStringExtra("url")
            view.loadUrl(url)
        }
    }

    internal fun handleSendText(intent: Intent) {
        val sharedText = intent.getStringExtra(Intent.EXTRA_TEXT)
        removeCookies()
        if (sharedText != null) {
            url = extractUrls(sharedText)[0]
            textView.text = url
            view.loadUrl(url)
        }
    }

    internal fun removeCookies() {
        val cookieManager = CookieManager.getInstance()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            cookieManager.removeAllCookies { aBoolean ->
                // a callback which is executed when the cookies have been removed
                Log.d("COOKIE_REMOVED", "Cookie removed: " + aBoolean!!)
            }
        } else
            cookieManager.removeAllCookie()
    }

    companion object {

        fun extractUrls(text: String): List<String> {
            val containedUrls = ArrayList<String>()
            val urlRegex = "((https?|ftp|gopher|telnet|file):((//)|(\\\\))+[\\w\\d:#@%/;$()~_?\\+-=\\\\\\.&]*)"
            val pattern = Pattern.compile(urlRegex, Pattern.CASE_INSENSITIVE)
            val urlMatcher = pattern.matcher(text)

            while (urlMatcher.find()) {
                containedUrls.add(
                    text.substring(
                        urlMatcher.start(0),
                        urlMatcher.end(0)
                    )
                )
            }

            return containedUrls
        }
    }
}