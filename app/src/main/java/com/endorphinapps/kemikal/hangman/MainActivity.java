package com.endorphinapps.kemikal.hangman;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private LinearLayout ll_pageContainer;
    private WordBank wordBank;
    private TextView tv_category;
    private LinearLayout currentLettersContainer;
    private LinearLayout usedLettersContainer;
    private String word;
    private List<String> letters;
    private String currentLetter;
    private Button btn_go;
    private EditText et_inputtedLetter;
    private Button btn_reset;
    private TextView tv_splitWordLetters;
    private ImageView iv_hangman;
    private MediaPlayer mediaPlayer;
    private int correctAnswerCounter;
    private int wrongAnswerCounter;
    private TextView tv_winLose;
    private InputMethodManager inputMethodManager;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Find all views
        findViews();

        //Initialise Word Bank
        wordBank = new WordBank();

        //Start Game
        //Generate a random word from the word bank
        //Split into an array of letters
        //Create textViews for letter
        //Action user entry
        startGame();

        //Action the user entry on click of the GO button
        btn_go.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                actionUserEntry(letters);
            }
        });

        //Reset the game on RESET button click
        btn_reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetGame();
            }
        });

        //Clear and hide the keyboard when focus is lost
        ll_pageContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearAndHideKeyboard();
            }
        });
    }

    /** Find all views **/
    private void findViews() {
        ll_pageContainer = (LinearLayout) findViewById(R.id.page_container);
        currentLettersContainer = (LinearLayout) findViewById(R.id.current_letters_container);
        usedLettersContainer = (LinearLayout) findViewById(R.id.used_letters_container);
        tv_category = (TextView) findViewById(R.id.tv_category);
        btn_reset = (Button) findViewById(R.id.btn_reset);
        et_inputtedLetter = (EditText) findViewById(R.id.ed_inputted_letter);
        btn_go = (Button) findViewById(R.id.btn_enter_letter);
        iv_hangman = (ImageView) findViewById(R.id.iv_hangman);
        tv_winLose = (TextView) findViewById(R.id.tv_winLose);
    }

    /** Start the game **/
    /**
     * Generate a random word from the word bank,
     * Split into an array of letters,
     * Create textViews for letter,
     * Action user entry,
     */
    private void startGame() {
        //Get current amount of possible words
        final int sizeOfWordBank = wordBank.getSizeOfWordBank();

        //Get random word from Word Bank
        word = generateRandomWord(sizeOfWordBank);
//        tv_category.setText(word); //Just for testing

        //Split word into an array of characters
        letters = splitWordIntoAnArrayOfLetters(word);

        //Create as many TextViews as there are letters in the random word
        //add them to the existing LinearLayout
        createViewsForLetters(letters);

        et_inputtedLetter.setShowSoftInputOnFocus(true);
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
            tv_splitWordLetters = new TextView(this);
            //Set TAG as the same value as the letter it contains
            tv_splitWordLetters.setTag(letter);
            //Styling
            tv_splitWordLetters.setText(letter);
            tv_splitWordLetters.setTextSize(50);
            tv_splitWordLetters.setTextColor(Color.parseColor("#00000000"));
            tv_splitWordLetters.setAllCaps(true);
            tv_splitWordLetters.setLetterSpacing(0.25f);
            tv_splitWordLetters.setBackgroundResource(R.drawable.box_outline);
            //Add views to layout
            currentLettersContainer.addView(tv_splitWordLetters);
        }
    }

    /** Action user entry, correct or incorrect **/
     /** @param letters
     */
    private void actionUserEntry(List<String> letters) {
        currentLetter = et_inputtedLetter.getText().toString().toLowerCase().trim();
        //Hide the keyboard
        inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        //If nothing has been entered show a toast
        if (currentLetter.isEmpty()) {
            Toast.makeText(getApplicationContext(), "You must enter something!", Toast.LENGTH_LONG).show();
        } else {
            //If the letter is valid
            if (letters.toString().contains(currentLetter)) {
                correctAnswer();
            }
            //Else if the letter is invalid
            else {
                wrongAnswer();
            }
            addToUsedLetters(currentLetter);
        }
        clearAndHideKeyboard();
    }

    /** What to do on a correct user entry **/
    private void correctAnswer() {
        //Loop through the child elements containing the TextViews
        for (int i = 0; i < currentLettersContainer.getChildCount(); i++) {
            View view = currentLettersContainer.getChildAt(i);
            //If the current TextView contains the selected letter
            if (view.getTag().equals(currentLetter)) {
                //Advance correct answer counter
                correctAnswerCounter++;
                //Play 'correct' sound
                mediaPlayer = MediaPlayer.create(this, R.raw.correct_answer);
                mediaPlayer.start();
                //Make the letter visible;
                TextView tv = (TextView) view;
                tv.setTextColor(Color.GRAY);
                //If all the letters have been shown
                if (correctAnswerCounter == letters.size()) {
                    youWin();
                }
            }
        }
    }

    /** What to do on an incorrect user entry **/
    /**
     * Advance 'wrong answer' counter
     * Set image resource to the current stage in the game
     */
    private void wrongAnswer() {
        //Advance 'wrong answer' counter
        wrongAnswerCounter++;
        //Play 'incorrect' sound
        mediaPlayer = MediaPlayer.create(this, R.raw.wrong_answer);
        mediaPlayer.start();
        //Use 'wrong answer' counter to select which stage of hangman to show
        int resources = 0;
        switch (wrongAnswerCounter) {
            case 1: resources = R.drawable.hangman_1;
                break;
            case 2: resources = R.drawable.hangman_2;
                break;
            case 3: resources = R.drawable.hangman_3;
                break;
            case 4: resources = R.drawable.hangman_4;
                break;
            case 5: resources = R.drawable.hangman_5;
                break;
            case 6: resources = R.drawable.hangman_6;
                youLose();
                break;
        }
        //Set hangman image
        iv_hangman.setImageResource(resources);
    }

    /** Reset Game on click of reset button **/
    /**
     * Set hangman image back to the first stage
     * Remove all views from parent Linear Layout
     * Start game
     */
    private void resetGame() {
        clearAndHideKeyboard();
        //Clear both counters
        correctAnswerCounter = 0;
        wrongAnswerCounter = 0;
        //Clear text
        tv_winLose.setText("");
        //Reset hangman image
        iv_hangman.setImageResource(R.drawable.hangman_0);
        iv_hangman.setAlpha(1.0f);
        //Clear textViews ready for new word
        currentLettersContainer.removeAllViews();

        startGame();
    }

    /** You Win **/
    private void youWin() {
        //Set hangman image to half transparency
        iv_hangman.setAlpha(0.25f);
        //Set text to Win, Green and Visible
        tv_winLose.setText("WIN");
        tv_winLose.setTextColor(Color.GREEN);
        tv_winLose.setVisibility(View.VISIBLE);
        //Disable the keyboard from further entries
        et_inputtedLetter.setShowSoftInputOnFocus(false);
    }

    /** You Lose **/
    /**
     * Set hangman image to half transparency
     * Set 'DEAD' text to visible
     */
    private void youLose() {
        //Set hangman image to half transparency
        iv_hangman.setAlpha(0.25f);
        //Set text to DEAD, Red and visible
        tv_winLose.setText("DEAD");
        tv_winLose.setTextColor(Color.RED);
        tv_winLose.setVisibility(View.VISIBLE);
        //Disable the keyboard from further entries
        et_inputtedLetter.setShowSoftInputOnFocus(false);
    }

    /** Clear and hide the keyboard **/
    private void clearAndHideKeyboard() {
        //Clear the keyboard
        et_inputtedLetter.setText("");
        //Hide the keyboard
        inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    }

    /** Show which letters have already been used **/
     /**
     * @param currentLetter
     */
    private void addToUsedLetters(String currentLetter) {
        //If the container is empty, add a label
        if (usedLettersContainer.getChildCount() == 0) {
            TextView tv_label = new TextView(this);
            tv_label.setText("Used letters: ");
            usedLettersContainer.addView(tv_label);
        }
        //Create new text views for each letter entered
        TextView tv_usedLetter = new TextView(this);
        tv_usedLetter.setText(currentLetter);
        tv_usedLetter.setTextSize(24);
        tv_usedLetter.setAllCaps(true);
        tv_usedLetter.setLetterSpacing(0.25f);
        tv_usedLetter.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);
        usedLettersContainer.addView(tv_usedLetter);
    }
}