package com.sdrt.stonepaperscissors;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.UUID;

public class CreateGameActivity extends AppCompatActivity {

    private DatabaseReference gameRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_game);

        TextView gameCodeTextView = findViewById(R.id.gameCodeTextView);
        TextView statusTextView = findViewById(R.id.statusTextView);

        // Generate a unique game code
        String gameCode = UUID.randomUUID().toString().substring(0, 6).toUpperCase();
        gameCodeTextView.setText("Game Code: " + gameCode);

        // Create game entry in Firebase
        gameRef = FirebaseDatabase.getInstance().getReference("games").child(gameCode);
        gameRef.setValue("waiting").addOnSuccessListener(aVoid -> {
            // Listen for opponent joining
            gameRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists() && snapshot.getValue().equals("joined")) {
                        statusTextView.setText("Opponent joined! Starting game...");
                        // Navigate to the game screen
                        Intent intent = new Intent(CreateGameActivity.this, GameActivity.class);
                        intent.putExtra("gameCode", gameCode);
                        intent.putExtra("playerRole", "host"); // or "guest"
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
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (gameRef != null) {
            gameRef.removeValue();
        }
    }
}
