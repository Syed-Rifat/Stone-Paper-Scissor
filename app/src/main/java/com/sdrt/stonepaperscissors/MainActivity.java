package com.sdrt.stonepaperscissors;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private TextView userScoreTextView, compScoreTextView, msgTextView;
    private int userScore = 0, compScore = 0;
    private Random random;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        userScoreTextView = findViewById(R.id.userScore);
        compScoreTextView = findViewById(R.id.compScore);
        msgTextView = findViewById(R.id.msg);
        Button resetButton = findViewById(R.id.resetButton);

        random = new Random();

        ImageButton rockButton = findViewById(R.id.rockButton);
        ImageButton paperButton = findViewById(R.id.paperButton);
        ImageButton scissorsButton = findViewById(R.id.scissorsButton);

        // Set click listeners for the buttons
        rockButton.setOnClickListener(view -> playGame("Rock"));
        paperButton.setOnClickListener(view -> playGame("Paper"));
        scissorsButton.setOnClickListener(view -> playGame("Scissors"));

        // Reset button listener
        resetButton.setOnClickListener(view -> resetScores());
    }

    private void playGame(String userSelection) {
        String computerSelection = getComputerChoice();
        msgTextView.setText("Computer chose: " + computerSelection);

        // Determine the winner
        if (userSelection.equals(computerSelection)) {
            msgTextView.append("\nIt's a tie!");
        } else if ((userSelection.equals("Rock") && computerSelection.equals("Scissors")) ||
                (userSelection.equals("Paper") && computerSelection.equals("Rock")) ||
                (userSelection.equals("Scissors") && computerSelection.equals("Paper"))) {
            msgTextView.append("\nYou win!");
            userScore++;
        } else {
            msgTextView.append("\nYou lose!");
            compScore++;
        }

        // Update the scoreboard
        updateScoreboard();
    }

    private String getComputerChoice() {
        String[] choices = {"Rock", "Paper", "Scissors"};
        return choices[random.nextInt(choices.length)];
    }

    private void updateScoreboard() {
        userScoreTextView.setText(String.valueOf(userScore));
        compScoreTextView.setText(String.valueOf(compScore));
    }

    private void resetScores() {
        userScore = 0;
        compScore = 0;
        updateScoreboard();
        msgTextView.setText(R.string.play_your_move);
    }
}
