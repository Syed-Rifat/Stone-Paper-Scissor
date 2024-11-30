package com.sdrt.stonepaperscissors;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class JoinGameActivity extends AppCompatActivity {

    private EditText gameCodeEditText;
    private Button joinGameButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join_game);

        gameCodeEditText = findViewById(R.id.gameCodeEditText);
        joinGameButton = findViewById(R.id.joinGameButton);

        joinGameButton.setOnClickListener(v -> {
            String gameCode = gameCodeEditText.getText().toString().trim();

            if (!gameCode.isEmpty()) {
                joinGameButton.setEnabled(false); // Disable button to prevent duplicate taps
                validateAndJoinGame(gameCode);
            } else {
                Toast.makeText(this, "Please enter a game code.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void validateAndJoinGame(String gameCode) {
        DatabaseReference gameRef = FirebaseDatabase.getInstance().getReference("games").child(gameCode);

        gameRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String state = snapshot.child("state").getValue(String.class);

                    if ("waiting".equals(state)) {
                        // Update game state and player2 information
                        gameRef.child("state").setValue("joined");
                        gameRef.child("player2").setValue("guest");

                        // Navigate to GameActivity
                        Intent intent = new Intent(JoinGameActivity.this, GameActivity.class);
                        intent.putExtra("gameCode", gameCode);
                        intent.putExtra("playerRole", "guest");
                        startActivity(intent);
                        finish();
                    } else if ("joined".equals(state)) {
                        Toast.makeText(JoinGameActivity.this, "Game already in progress.", Toast.LENGTH_SHORT).show();
                    } else if ("abandoned".equals(state)) {
                        Toast.makeText(JoinGameActivity.this, "Game is no longer available.", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(JoinGameActivity.this, "Invalid game state.", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(JoinGameActivity.this, "Invalid or unavailable game code.", Toast.LENGTH_SHORT).show();
                }
                joinGameButton.setEnabled(true); // Re-enable button
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(JoinGameActivity.this, "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                joinGameButton.setEnabled(true); // Re-enable button
            }
        });
    }
}
