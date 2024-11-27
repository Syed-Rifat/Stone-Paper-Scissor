package com.sdrt.stonepaperscissors;

import android.os.Bundle;
import android.view.View;
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

public class GameActivity extends AppCompatActivity {

    private TextView opponentChoice, scoreBoard, gameStatus;
    private Button btnRock, btnPaper, btnScissors;

    private String gameCode;
    private String playerRole; // "host" or "guest"
    private String opponentMove = null;
    private String playerMove = null;

    private int playerScore = 0;
    private int opponentScore = 0;

    private DatabaseReference gameRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        // Initialize UI elements
        opponentChoice = findViewById(R.id.opponentChoice);
        scoreBoard = findViewById(R.id.scoreBoard);
        gameStatus = findViewById(R.id.gameStatus);
        btnRock = findViewById(R.id.btnRock);
        btnPaper = findViewById(R.id.btnPaper);
        btnScissors = findViewById(R.id.btnScissors);

        // Get game code and player role
        gameCode = getIntent().getStringExtra("gameCode");
        playerRole = getIntent().getStringExtra("playerRole"); // Either "host" or "guest"

        // Firebase reference for the game
        gameRef = FirebaseDatabase.getInstance().getReference("games").child(gameCode);

        // Listen for opponent's move
        gameRef.child(playerRole.equals("host") ? "guestMove" : "hostMove")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            opponentMove = snapshot.getValue(String.class);
                            updateGameStatus();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(GameActivity.this, "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

        // Set move buttons
        btnRock.setOnClickListener(v -> makeMove("Rock"));
        btnPaper.setOnClickListener(v -> makeMove("Paper"));
        btnScissors.setOnClickListener(v -> makeMove("Scissors"));
    }

    private void makeMove(String move) {
        playerMove = move;
        gameRef.child(playerRole.equals("host") ? "hostMove" : "guestMove").setValue(move);
        gameStatus.setText("You chose: " + move);
        updateGameStatus();
    }

    private void updateGameStatus() {
        if (playerMove != null && opponentMove != null) {
            String result = calculateWinner(playerMove, opponentMove);
            if (result.equals("win")) {
                playerScore++;
                gameStatus.setText("You won this round!");
            } else if (result.equals("lose")) {
                opponentScore++;
                gameStatus.setText("You lost this round!");
            } else {
                gameStatus.setText("It's a tie!");
            }

            // Update scores
            scoreBoard.setText("Score: You " + playerScore + " - " + opponentScore + " Opponent");

            // Reset for next round
            playerMove = null;
            opponentMove = null;
            gameRef.child("hostMove").removeValue();
            gameRef.child("guestMove").removeValue();
        }
    }

    private String calculateWinner(String playerMove, String opponentMove) {
        if (playerMove.equals(opponentMove)) {
            return "tie";
        } else if ((playerMove.equals("Rock") && opponentMove.equals("Scissors")) ||
                (playerMove.equals("Paper") && opponentMove.equals("Rock")) ||
                (playerMove.equals("Scissors") && opponentMove.equals("Paper"))) {
            return "win";
        } else {
            return "lose";
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (gameRef != null) {
            gameRef.removeValue();
        }
    }
}
