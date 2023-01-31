package com.example.anagram;

import static com.example.anagram.Anagram.loadAllWords;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.firebase.FirebaseApp;


public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private TextView wordTv, scoreBoard, highScoreBoard, strikesBoard;
    private EditText wordEnteredTv;
    private Button validate, newGame;
    private String wordToFind;

    Context context;

    private static int score = 0, highScore = 0, strikes = 3, permission = 0;

    private boolean loadOccurred = false;

    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    /**
     * Checks if the app has permission to write to device storage
     *
     * If the app does not has permission then the user will be prompted to grant permissions
     *
     * @param activity
     */
    public int verifyStoragePermissions(Activity activity) {
        // Check if we have write permission
        int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.READ_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            Toast.makeText(this, "Please Allow Permission for the app to work!", Toast.LENGTH_LONG).show();

            ActivityCompat.requestPermissions(
                    activity,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
        }
        return permission;
    }

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        permission = verifyStoragePermissions(this);
        context = getApplicationContext();
        FirebaseApp.initializeApp(context);
        loadAllWords(context);
        setContentView(R.layout.activity_main);
        wordTv = (TextView) findViewById(R.id.wordTv);
        highScoreBoard = (TextView) findViewById(R.id.highScoreTV);
        scoreBoard = (TextView) findViewById(R.id.scoreTV);
        strikesBoard = (TextView) findViewById(R.id.guessTV);
        wordEnteredTv = (EditText) findViewById(R.id.wordEnteredEt);
        validate = (Button) findViewById(R.id.validate);
        validate.setOnClickListener(this);
        newGame = (Button) findViewById(R.id.newGame);
        newGame.setOnClickListener(this);
        newGame();
    }

    @Override
    public void onClick(View view) {
        if (view == validate) {
            validate();
        } else if (view == newGame) {
            newGame();
        }
    }

    private void validate() {
        if (wordEnteredTv == null) {
            Toast.makeText(this, "Enter Text please!", Toast.LENGTH_SHORT).show();
        }
        String w = wordEnteredTv.getText().toString();
        if (wordToFind.equals(w)) {
            Toast.makeText(this, "Congratulations ! You found the word " + wordToFind, Toast.LENGTH_SHORT).show();
            score++;
            scoreBoard.setText("Score: " + score);
            newWord();
        } else {
            strikes--;
            strikesBoard.setText("Guesses remaining: " + strikes);
            Toast.makeText(this, "Retry ! " + strikes + " Guesses Remaining.", Toast.LENGTH_SHORT).show();
            if(strikes == 0) {
                Toast.makeText(this, "3 strikes and you're out!", Toast.LENGTH_SHORT).show();
                newGame();
            }
        }
    }

    private void newWord() {
        wordToFind = Anagram.randomWord(score);
        if (permission == PackageManager.PERMISSION_GRANTED) {
            if(!loadOccurred) {
                loadAllWords(context);
                loadOccurred = true;
            }
        } else {
            permission = verifyStoragePermissions(this);
        }
        String wordShuffled = Anagram.shuffleWord(wordToFind);
        wordTv.setText(wordShuffled);
        wordEnteredTv.setText("");
    }

    private void newGame() {
        if(score > highScore) {
            highScore = score;
            Toast.makeText(this, "WOW! A new High Score! " + highScore, Toast.LENGTH_SHORT).show();
        }
        score = 0;
        strikes = 3;
        strikesBoard.setText("Guesses remaining: " + strikes);
        highScoreBoard.setText("High Score: " + highScore);
        scoreBoard.setText("Score: " + score);
        Toast.makeText(this, "NEW GAME", Toast.LENGTH_SHORT).show();
        newWord();
    }
}