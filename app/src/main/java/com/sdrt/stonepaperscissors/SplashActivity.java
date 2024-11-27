package com.sdrt.stonepaperscissors;


import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import androidx.appcompat.app.AppCompatActivity;

public class SplashActivity extends AppCompatActivity {

    private static final int SPLASH_TIME_OUT = 3000; // 3 seconds

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        // Delay and launch MainActivity
        new Handler().postDelayed(() -> {
            Intent intent = new Intent(SplashActivity.this, GameMenuActivity.class);
            startActivity(intent);
            finish();
        }, SPLASH_TIME_OUT);
    }
}
