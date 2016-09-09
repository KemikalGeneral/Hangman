package com.endorphinapps.kemikal.hangman;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private WordBank wordBank;

    private TextView tv_word;
    private Button btn_reset;

    private LinearLayout linearLayout;
    private String word;

    private EditText et_inputtedLetter;
    private String currentLetter;
    private Button btn_go;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Find all views
        findViews();

        //Initialise Word Bank and get a random word
        wordBank = new WordBank();
        final int sizeOfWordBank = wordBank.getSizeOfWordBank();

        //Get random word from Word Bank
        word = generateRandomWord(sizeOfWordBank);

        //Reset word
        btn_reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetWord(sizeOfWordBank);
            }
        });

        //Split word into an array of letters
//        final String[] letters = word.split("");
//        for(String letter : letters) {
//            Log.v("HMmsg LETTERS", letter);
//        }
        final List<String> letters = splitWord(word);

        //Create as many TextViews as there are letters in the random word
        //add them to the existing LinearLayout
        for (String letter : letters) {
            TextView textView = new TextView(this);
            textView.setText(letter);
            textView.setTextSize(50);
            textView.setLetterSpacing(0.5f);
            linearLayout.addView(textView);
        }

        btn_go.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentLetter = et_inputtedLetter.getText().toString().toLowerCase().trim();
                Log.v("HMmsg:", currentLetter);

                actionUserEntry(letters);
            }
        });
    }
    public void findViews() {
        tv_word = (TextView) findViewById(R.id.tv_word);
        btn_reset = (Button) findViewById(R.id.btn_reset);
        linearLayout = (LinearLayout) findViewById(R.id.linear_layout);
        et_inputtedLetter = (EditText) findViewById(R.id.ed_inputted_letter);
        btn_go = (Button) findViewById(R.id.btn_enter_letter);
    }

    public String generateRandomWord(int sizeOfWordBank) {
        //Random number generator
        Random random = new Random();
        int randomNumber = random.nextInt(sizeOfWordBank) + 1;

        //Retrieve a word
        word = wordBank.getWord(randomNumber);

        //Set word to screen
        tv_word.setText(word);

        return word;
    }

    public List<String> splitWord(String word) {
        List<String> letters = new ArrayList<>();
        String[] test = word.split("");

        for (String s : test) {
            if(s != "" && s.length() > 0) {
                letters.add(s);
            }
        }
        return letters;
    }

    private void actionUserEntry(List<String> letters) {
        if (currentLetter.isEmpty()) {
            Toast.makeText(getApplicationContext(), "You must enter something!", Toast.LENGTH_LONG).show();
        } else {
            if (letters.toString().contains(currentLetter)) {
                Log.v("HMmsg", currentLetter + " is in the word");
                linearLayout.setBackgroundColor(Color.GREEN);
            } else {
                Log.v("HMmsg", currentLetter + " is NOT in the word");
                linearLayout.setBackgroundColor(Color.RED);
            }
        }
    }

    public void resetWord(int sizeOfWordBank) {
        generateRandomWord(sizeOfWordBank);
    }
}
