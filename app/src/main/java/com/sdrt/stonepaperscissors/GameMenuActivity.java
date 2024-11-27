package com.sdrt.stonepaperscissors;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class GameMenuActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_menu);

        // Offline Play Button
        Button offlinePlayButton = findViewById(R.id.offlinePlayButton);
        offlinePlayButton.setOnClickListener(v -> {
            // Start Offline Play Activity
            Intent intent = new Intent(GameMenuActivity.this, OfflinePlayActivity.class);
            startActivity(intent);
        });

        // Online Play Button
        Button onlinePlayButton = findViewById(R.id.onlinePlayButton);
        onlinePlayButton.setOnClickListener(v -> {
            // Start Online Play Activity
            Intent intent = new Intent(GameMenuActivity.this, OnlinePlayActivity.class); // or CreateGameActivity for host
            startActivity(intent);
        });
    }
}