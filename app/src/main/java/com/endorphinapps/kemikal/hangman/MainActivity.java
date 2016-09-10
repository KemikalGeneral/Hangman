package com.endorphinapps.kemikal.hangman;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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

    private TextView tv_info;
    private Button btn_reset;

    private LinearLayout linearLayout;
    private String word;

    private EditText et_inputtedLetter;
    private String currentLetter;
    private Button btn_go;

    private TextView textView;

    private MediaPlayer mediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Find all views
        findViews();

        //Initialise Word Bank and get current amount of possible words
        wordBank = new WordBank();
        final int sizeOfWordBank = wordBank.getSizeOfWordBank();

        //Get random word from Word Bank
        word = generateRandomWord(sizeOfWordBank);
        tv_info.setText(word);

        //Reset word
        btn_reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetWord(sizeOfWordBank);
            }
        });

        //Split word into an array of characters
        final List<String> letters = splitWordIntoAnArrayOfLetters(word);

        //Create as many TextViews as there are letters in the random word
        //add them to the existing LinearLayout
        createViewsForLetters(letters);

        btn_go.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentLetter = et_inputtedLetter.getText().toString().toLowerCase().trim();
                actionUserEntry(letters);
            }
        });
    }

    /** Find all views **/
    private void findViews() {
        tv_info = (TextView) findViewById(R.id.tv_info);
        btn_reset = (Button) findViewById(R.id.btn_reset);
        linearLayout = (LinearLayout) findViewById(R.id.linear_layout);
        et_inputtedLetter = (EditText) findViewById(R.id.ed_inputted_letter);
        btn_go = (Button) findViewById(R.id.btn_enter_letter);
    }

    /** Generate a Random Word From Word Bank **/
     /** @param sizeOfWordBank
     * @return
     */
    private String generateRandomWord(int sizeOfWordBank) {
        //Random number generator
        Random random = new Random();
        int randomNumber = random.nextInt(sizeOfWordBank) + 1;

        //Retrieve a word
        word = wordBank.getWord(randomNumber);

        return word;
    }

    /** Split selected random word in to and array of characters **/
     /** @param word
     * @return
     */
    private List<String> splitWordIntoAnArrayOfLetters(String word) {
        List<String> letters = new ArrayList<>();
        String[] tempArray = word.split("");

        for (String string : tempArray) {
            if(string != "" && string.length() > 0) {
                letters.add(string);
            }
        }
        return letters;
    }

    /** Create TextViews for each letter in the Array **/
     /** @param letters
     */
    private void createViewsForLetters(List<String> letters) {
        for (String letter : letters) {
            textView = new TextView(this);
            textView.setTag(letter);
            textView.setText(letter);
            textView.setTextSize(50);
            textView.setLetterSpacing(0.5f);
            textView.setVisibility(View.INVISIBLE); //Will become visible on successful entry
            linearLayout.addView(textView);
        }
    }

    /** Action user entry, correct or incorrect **/
     /** @param letters
     */
    private void actionUserEntry(List<String> letters) {
        if (currentLetter.isEmpty()) {
            Toast.makeText(getApplicationContext(), "You must enter something!", Toast.LENGTH_LONG).show();
        } else {
            //Correct entry - if the Letters array contains the entered letter
            if (letters.toString().contains(currentLetter)) {
                //Loop through the child elements containing the TextViews
                for (int i = 0; i < linearLayout.getChildCount(); i++) {
                    View view = linearLayout.getChildAt(i);
                    //If the current TextView contains the selected letter
                    if (view.getTag().equals(currentLetter)) {
                        //Play 'correct' sound
                        mediaPlayer = MediaPlayer.create(this, R.raw.correct_answer);
                        mediaPlayer.start();
                        //Make the letter visible
                        view.setVisibility(View.VISIBLE);
                    }
                }
            }
            //Incorrect entry
            else {
                //Play 'incorrect' sound
                mediaPlayer = MediaPlayer.create(this, R.raw.wrong_answer);
                mediaPlayer.start();
            }
        }
        //Set the keyboard back to blank
        et_inputtedLetter.setText("");
    }

    /** Reset the word (choose another and start again) **/
     /** @param sizeOfWordBank
     */
    private void resetWord(int sizeOfWordBank) {
        generateRandomWord(sizeOfWordBank);
    }
}