package com.bikefit.wedgecalculator.startup;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

import com.bikefit.wedgecalculator.R;
import com.bikefit.wedgecalculator.welcome.WelcomeActivity;

/**
 * Activity to show the splash screen
 */
public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_activity);

        Handler handler = new Handler();

        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(SplashActivity.this, WelcomeActivity.class));
                finish();
            }
        };

        handler.postDelayed(runnable, getResources().getInteger(R.integer.splash_screen_pause));
    }
}
