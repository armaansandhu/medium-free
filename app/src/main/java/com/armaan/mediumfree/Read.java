package com.armaan.mediumfree;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.ValueCallback;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Read extends AppCompatActivity {

    String url = "";
    TextView textView;
    WebView view;
    ProgressBar mProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read);

        textView = (TextView) findViewById(R.id.textView);
        mProgressBar = (ProgressBar) findViewById(R.id.progressBar);

        view = (WebView) findViewById(R.id.web);
        view.setVisibility(View.GONE);
        view.setWebViewClient(new WebViewClient(){
            @Override public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);

                view.loadUrl("javascript:(function() { " +
                        "document.getElementsByClassName('postMeterBar')[0].style.display='none';" +
                   "})()");
                mProgressBar.setVisibility(ProgressBar.GONE);
                view.setVisibility(View.VISIBLE);
            }
        });
        WebSettings settings = view.getSettings();
        settings.setJavaScriptEnabled(true);
        Intent intent = getIntent();
        String action = intent.getAction();
        String type = intent.getType();

        if (Intent.ACTION_SEND.equals(action) && type != null) {
            if ("text/plain".equals(type)) {
                handleSendText(intent);
            }
        } else {
            removeCookies();
            url = intent.getStringExtra("url");
            view.loadUrl(url);
        }
    }

    void handleSendText(Intent intent) {
        String sharedText = intent.getStringExtra(Intent.EXTRA_TEXT);
        removeCookies();
        if (sharedText != null) {
            url = extractUrls(sharedText).get(0);
            textView.setText(url);
            view.loadUrl(url);
        }
    }

    void removeCookies(){
        android.webkit.CookieManager cookieManager = CookieManager.getInstance();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            cookieManager.removeAllCookies(new ValueCallback<Boolean>() {
                // a callback which is executed when the cookies have been removed
                @Override
                public void onReceiveValue(Boolean aBoolean) {
                    Log.d("COOKIE_REMOVED", "Cookie removed: " + aBoolean);
                }
            });
        }
        else cookieManager.removeAllCookie();
    }

    public static List<String> extractUrls(String text) {
        List<String> containedUrls = new ArrayList<String>();
        String urlRegex = "((https?|ftp|gopher|telnet|file):((//)|(\\\\))+[\\w\\d:#@%/;$()~_?\\+-=\\\\\\.&]*)";
        Pattern pattern = Pattern.compile(urlRegex, Pattern.CASE_INSENSITIVE);
        Matcher urlMatcher = pattern.matcher(text);

        while (urlMatcher.find())
        {
            containedUrls.add(text.substring(urlMatcher.start(0),
                    urlMatcher.end(0)));
        }

        return containedUrls;
    }
}