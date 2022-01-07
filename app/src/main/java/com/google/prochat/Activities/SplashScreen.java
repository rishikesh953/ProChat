package com.google.prochat.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;

import com.google.prochat.Activities.PhoneAuthentication;
import com.google.prochat.R;

@SuppressLint("CustomSplashScreen")
public class SplashScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_splash_screen);


        new Handler().postDelayed(() -> {
            Intent i = new Intent(getApplicationContext(), PhoneAuthentication.class);
            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK| Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(i);
            finish();
        }, 4000);

    }
}