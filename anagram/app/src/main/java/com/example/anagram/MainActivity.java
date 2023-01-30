package com.example.anagram;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.FirebaseApp;


public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private TextView wordTv;
    private EditText wordEnteredTv;
    private Button validate, newGame;
    private String wordToFind;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Context context = getApplicationContext();
        FirebaseApp.initializeApp(context);
        setContentView(R.layout.activity_main);
        wordTv = (TextView) findViewById(R.id.wordTv);
//        score = (TextView) findViewById(R.id.highScore);
//        strikes =
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
        String w = wordEnteredTv.getText().toString();

        if (wordToFind.equals(w)) {
            Toast.makeText(this, "Congratulations ! You found the word " + wordToFind, Toast.LENGTH_SHORT).show();
//            score++;
            newWord(0);
        } else {
            Toast.makeText(this, "Retry !", Toast.LENGTH_SHORT).show();
//            strikes++;
//            if(strikes > 3) {
//                Toast.makeText(this, "3 strikes and you're out!", Toast.LENGTH_SHORT).show();
//            }
        }
    }

    private void newWord(int score) {
        wordToFind = Anagram.randomWord(score);
        String wordShuffled = Anagram.shuffleWord(wordToFind);
        wordTv.setText(wordShuffled);
        wordEnteredTv.setText("");
    }

    private void newGame() {
        newWord(0);
//        wordToFind = Anagram.randomWord(score);
//        String wordShuffled = Anagram.shuffleWord(wordToFind);
//        wordTv.setText(wordShuffled);
//        wordEnteredTv.setText("");
    }
}