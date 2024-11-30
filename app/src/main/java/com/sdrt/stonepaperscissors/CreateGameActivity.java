package com.sdrt.stonepaperscissors;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class CreateGameActivity extends AppCompatActivity {

    private DatabaseReference gameRef;
    private String gameCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_game);

        TextView gameCodeTextView = findViewById(R.id.gameCodeTextView);
        TextView statusTextView = findViewById(R.id.statusTextView);
        Button copyGameCodeButton = findViewById(R.id.copyGameCodeButton);

        // Generate a unique game code
        gameCode = UUID.randomUUID().toString().substring(0, 6).toUpperCase();
        gameCodeTextView.setText("Game Code: " + gameCode);

        // Copy game code functionality
        copyGameCodeButton.setOnClickListener(v -> {
            ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
            ClipData clip = ClipData.newPlainText("Game Code", gameCode);
            clipboard.setPrimaryClip(clip);
            Toast.makeText(this, "Game code copied!", Toast.LENGTH_SHORT).show();
        });

        // Create game entry in Firebase
        gameRef = FirebaseDatabase.getInstance().getReference("games").child(gameCode);

        Map<String, Object> gameData = new HashMap<>();
        gameData.put("state", "waiting");
        gameData.put("player1", "host"); // Add the host as player1
        gameData.put("player2", null);  // No player2 initially

        gameRef.setValue(gameData).addOnSuccessListener(aVoid -> {
            statusTextView.setText("Waiting for an opponent...");

            // Listen for opponent joining
            gameRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.child("player2").getValue() != null) {
                        statusTextView.setText("Opponent joined! Starting game...");
                        gameRef.child("state").setValue("started"); // Mark game as started

                        // Navigate to the game screen
                        Intent intent = new Intent(CreateGameActivity.this, GameActivity.class);
                        intent.putExtra("gameCode", gameCode);
                        intent.putExtra("playerRole", "host");
                        startActivity(intent);
                        finish();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    statusTextView.setText("Error: " + error.getMessage());
                }
            });
        });

        // Timeout if no opponent joins
        new Handler().postDelayed(() -> {
            statusTextView.setText("No opponent joined. Please try again.");
            gameRef.removeValue(); // Clean up if no one joins
        }, 200000); // 200 seconds timeout
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (gameRef != null) {
            gameRef.child("state").setValue("abandoned");
        }
    }
}
