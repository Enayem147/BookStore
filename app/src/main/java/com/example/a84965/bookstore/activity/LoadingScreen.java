package com.example.a84965.bookstore.activity;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.a84965.bookstore.R;

public class LoadingScreen extends AppCompatActivity {
    Handler handler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading_screen);
        goHomePage();
    }

    private void goHomePage(){
        handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                HomePage.isFirst = false;
                finish();
            }
        },5000);
    }
}
