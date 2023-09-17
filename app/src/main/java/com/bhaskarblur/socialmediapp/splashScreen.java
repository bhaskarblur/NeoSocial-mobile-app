package com.bhaskarblur.socialmediapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.window.SplashScreen;

import java.util.Objects;

public class splashScreen extends AppCompatActivity {

    SharedPreferences prefs;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
//      getActionBar().hide();


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                prefs = PreferenceManager.getDefaultSharedPreferences(splashScreen.this);
                Log.d("logged", String.valueOf(prefs.getBoolean("loggedStatus", false)));
                if(prefs.getBoolean("loggedStatus", false)) {
                    startActivity(new Intent(splashScreen.this, MainActivity.class));
                    finish();
                }
                else {
                    startActivity(new Intent(splashScreen.this, loginscreen.class));
                    finish();
                }
            }
        },1500);


    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);
    }
}
