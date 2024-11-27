package com.sdrt.stonepaperscissors;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

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

public class JoinGameActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join_game);

        EditText gameCodeEditText = findViewById(R.id.gameCodeEditText);
        Button joinGameButton = findViewById(R.id.joinGameButton);

        joinGameButton.setOnClickListener(v -> {
            String gameCode = gameCodeEditText.getText().toString().trim();

            if (!gameCode.isEmpty()) {
                DatabaseReference gameRef = FirebaseDatabase.getInstance().getReference("games").child(gameCode);

                gameRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists() && snapshot.getValue().equals("waiting")) {
                            gameRef.setValue("joined");
                            Intent intent = new Intent(JoinGameActivity.this, GameActivity.class);
                            intent.putExtra("gameCode", gameCode);
                            intent.putExtra("playerRole", "guest");
                            startActivity(intent);
                            finish();
                        } else {
                            Toast.makeText(JoinGameActivity.this, "Invalid or unavailable game code.", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(JoinGameActivity.this, "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            } else {
                Toast.makeText(this, "Please enter a game code.", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
