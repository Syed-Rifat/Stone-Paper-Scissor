package com.sdrt.stonepaperscissors;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class OnlinePlayActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_online_play);

        Button createGameButton = findViewById(R.id.createGameButton);
        Button joinGameButton = findViewById(R.id.joinGameButton);

        createGameButton.setOnClickListener(v -> {
            Intent intent = new Intent(OnlinePlayActivity.this, CreateGameActivity.class);
            startActivity(intent);
        });

        joinGameButton.setOnClickListener(v -> {
            Intent intent = new Intent(OnlinePlayActivity.this, JoinGameActivity.class);
            startActivity(intent);
        });
    }
}
