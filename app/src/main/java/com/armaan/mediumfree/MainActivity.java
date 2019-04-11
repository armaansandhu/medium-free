package com.armaan.mediumfree;

import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;

import android.os.Build;
import android.support.annotation.RequiresApi;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;


public class MainActivity extends AppCompatActivity implements TextView.OnEditorActionListener {


    EditText urlInput;

    void linkSummarize(View view){
        Intent intent = new Intent(this,Read.class);
        intent.putExtra("url",urlInput.getText().toString());
        startActivity(intent);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        urlInput = (EditText) findViewById(R.id.urlInput);
        urlInput.setOnEditorActionListener(editorActionListener);
    }

    private TextView.OnEditorActionListener editorActionListener = new TextView.OnEditorActionListener() {
        @Override
        public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
            if(actionId == EditorInfo.IME_ACTION_SEARCH){
                Intent intent = new Intent(getApplicationContext(),Read.class);
                intent.putExtra("url",urlInput.getText().toString());
                startActivity(intent);
            }
            return false;
        }
    };

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        return false;
    }
}
