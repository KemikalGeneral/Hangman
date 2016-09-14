package com.endorphinapps.kemikal.hangman;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    //Nav Drawer
    private DrawerLayout dl_drawerLayout;
    private ListView listView_left;
    private ListView listView_right;
    private ArrayAdapter<String> arrayAdapter_left;
    private ArrayAdapter<String> arrayAdapter_right;
    private ImageView iv_gameMenuIcon;
    private ImageView iv_settingsMenuIcon;

    private LinearLayout ll_pageContainer;
    private WordBank wordBank;
    private TextView tv_category;
    private LinearLayout currentLettersContainer;
    private LinearLayout usedLettersContainer;
    private List<String> usedLettersArray = new ArrayList<>();
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
    private Typeface typeface;
    private Boolean isTimed;
    private CountDownTimer countDownTimer;
    private Boolean isTimerRunning;
    private int wordLengthSelected;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Set font
        typeface = Typeface.createFromAsset(getAssets(), "fonts/crayon_font.ttf");

        //Find all views
        findViews();

        //Setup Nav Drawer
        addDrawerItemAnListener();

        //Initialise Word Bank
        wordBank = new WordBank();

        //Check whether the game is timed or not
        isTimed = getIntent().getBooleanExtra("IS_TIMED", false);
        wordLengthSelected = getIntent().getIntExtra("WORD_LENGTH", 0);

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

        //Open the Game Menu
        iv_gameMenuIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dl_drawerLayout.openDrawer(listView_left);
            }
        });

        //Open the Settings Menu
        iv_settingsMenuIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dl_drawerLayout.openDrawer(listView_right);
            }
        });
    }

    /** Find all views **/
    private void findViews() {
        ll_pageContainer = (LinearLayout) findViewById(R.id.page_container);
        currentLettersContainer = (LinearLayout) findViewById(R.id.current_letters_container);
        usedLettersContainer = (LinearLayout) findViewById(R.id.used_letters_container);
        tv_category = (TextView) findViewById(R.id.tv_category);
            tv_category.setTypeface(typeface);
        tv_winLose = (TextView) findViewById(R.id.tv_winLose);
            tv_winLose.setTypeface(typeface);
        btn_reset = (Button) findViewById(R.id.btn_reset);
            btn_reset.setTypeface(typeface);
        btn_go = (Button) findViewById(R.id.btn_enter_letter);
            btn_go.setTypeface(typeface);
        et_inputtedLetter = (EditText) findViewById(R.id.ed_inputted_letter);
            et_inputtedLetter.setTypeface(typeface);
        iv_hangman = (ImageView) findViewById(R.id.iv_hangman);
        //Nav Drawer
        listView_left = (ListView) findViewById(R.id.listView_left);
        listView_right = (ListView) findViewById(R.id.listView_right);
        dl_drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        iv_gameMenuIcon = (ImageView) findViewById(R.id.icon_game_menu);
        iv_settingsMenuIcon = (ImageView) findViewById(R.id.icon_settings_menu);

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

        //If it is a TIMED game, start timer, if not, set running to false
        if (isTimed) {
            introCounter();
        } else {
            isTimerRunning = false;
        }
    }

    /** Generate a Random Word From Word Bank **/
     /** @param sizeOfWordBank
     * @return
     */
    private String generateRandomWord(int sizeOfWordBank) {
        //Random number generator
        Random random = new Random();
        int randomNumber = random.nextInt(sizeOfWordBank);
        //Retrieve a word
        word = wordBank.getWord(randomNumber);
        Log.v("z!", "" + word.length());
        if (wordLengthSelected != 0) {
            if (word.length() != wordLengthSelected){
                generateRandomWord(sizeOfWordBank);
            }
        }

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
        //Loop through the letters in the array
        for (String letter : letters) {
            //Create a new TextView
            tv_splitWordLetters = new TextView(this);
            //Set TAG to the same value as the letter it contains
            tv_splitWordLetters.setTag(letter);
            //Styling
            tv_splitWordLetters.setText(letter);
            tv_splitWordLetters.setTextSize(56);
            tv_splitWordLetters.setTextColor(Color.parseColor("#00000000"));
            tv_splitWordLetters.setAllCaps(true);
            tv_splitWordLetters.setLetterSpacing(0.25f);
            tv_splitWordLetters.setBackgroundResource(R.drawable.box_outline);
            tv_splitWordLetters.setTypeface(typeface);
            //Add views to layout
            currentLettersContainer.addView(tv_splitWordLetters);
        }
    }

    /** Action user entry, correct or incorrect **/
     /** @param letters
     */
    private void actionUserEntry(List<String> letters) {
        currentLetter = et_inputtedLetter.getText().toString().toLowerCase().trim();
        //Clear and hide the keyboard
        clearAndHideKeyboard();
        //If nothing has been entered show a toast
        if (currentLetter.isEmpty()) {
            Toast.makeText(getApplicationContext(), "You must enter something!", Toast.LENGTH_LONG).show();
        } else {
            //If the letter has already been used show a toast
            if (usedLettersArray.contains(currentLetter)) {
                Toast.makeText(getApplicationContext(), "You've already used the letter " + currentLetter + "!", Toast.LENGTH_SHORT).show();
            } else {
                //Add the current letter to the array of used letters and show on screen
                addToUsedLetters(currentLetter);
                //If the letter is valid
                if (letters.toString().contains(currentLetter)) {
                    correctAnswer();
                }
                //Else if the letter is invalid
                else {
                    wrongAnswer();
                }
            }
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
                tv.setTextColor(Color.BLACK);
                tv.setTypeface(typeface);
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
        //Stop the count down timer if it's running
        if (isTimerRunning) {
            countDownTimer.cancel();
        }
        //Clear both counters
        correctAnswerCounter = 0;
        wrongAnswerCounter = 0;
        //Clear used letters
        usedLettersContainer.removeAllViews();
        usedLettersArray.clear();
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
        //Stop the count down timer if it's running
        if (isTimerRunning) {
            countDownTimer.cancel();
        }
        //Set hangman image to half transparency
        iv_hangman.setAlpha(0.25f);
        //Set text to Win, Green, Font and Visible
        tv_winLose.setText("WIN");
        tv_winLose.setTextColor(Color.GREEN);
        tv_winLose.setTypeface(typeface);
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
        //Stop the count down timer if it's running
        if (isTimerRunning) {
            countDownTimer.cancel();
        }
        //Set hangman image to half transparency
        iv_hangman.setAlpha(0.25f);
        //Set text to DEAD, Red, Font and visible
        tv_winLose.setText("DEAD");
        tv_winLose.setTextColor(Color.RED);
        tv_winLose.setTypeface(typeface);
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
        //Add letter to the used letters array
        usedLettersArray.add(currentLetter);
        //If the container is empty, add a label
        if (usedLettersContainer.getChildCount() == 0) {
            TextView tv_label = new TextView(this);
            tv_label.setText("Used letters: ");
            tv_label.setTextSize(24);
            tv_label.setTextColor(Color.GRAY);
            tv_label.setTypeface(typeface);
            usedLettersContainer.addView(tv_label);
        }
        //Create new text views for each letter entered
        TextView tv_usedLetter = new TextView(this);
        tv_usedLetter.setText(currentLetter);
        tv_usedLetter.setTextSize(32);
        tv_usedLetter.setTextColor(Color.RED);
        tv_usedLetter.setAllCaps(true);
        tv_usedLetter.setLetterSpacing(0.25f);
        tv_usedLetter.setTypeface(typeface);
        usedLettersContainer.addView(tv_usedLetter);
    }

    /** INTRO Count Down Timer 3-2-1 for timed game **/
    /**
     * Starts timer
     * countDownTimer() when the timer has finished
     */
    private void introCounter() {
        //Set length of time for countdown 3-2-1
        long time = 3000;

        CountDownTimer introTimer = new CountDownTimer(time, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                Log.v("z!", "Countdown" + millisUntilFinished);
                tv_winLose.setText("" + millisUntilFinished / 1000);
                tv_winLose.setTextColor(getResources().getColor(R.color.pink));
                tv_winLose.setVisibility(View.VISIBLE);
            }

            @Override
            public void onFinish() {
                Log.v("z!", "Finshed");
                countDownTimer();
            }
        };
        introTimer.start();
    }

    /** Count Down Timer for timed game **/
    /**
     * Starts timer
     * youLose() if the timer finished without being cancelled (win)
     */
    private void countDownTimer() {
        //Get number of letters in the split current word array
        int numberOfLettersInArray = letters.size();
        //Set length of time for countdown, 5seconds per letter
        long time = numberOfLettersInArray * 5000;
        countDownTimer = new CountDownTimer(time, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                Log.v("z!", "Counting Down" + millisUntilFinished);
                isTimerRunning = true;
                tv_winLose.setText("" + millisUntilFinished / 1000);
                tv_winLose.setTextColor(getResources().getColor(R.color.pink));
                tv_winLose.setVisibility(View.VISIBLE);
            }

            @Override
            public void onFinish() {
                Log.v("z!", "Finshed");
                isTimerRunning = false;
                youLose();
            }
        };
        countDownTimer.start();
    }

    /** Setup Navigation Drawer **/
    private void addDrawerItemAnListener() {
        String[] drawerItems = getResources().getStringArray(R.array.nav_gameType);
        //Add a header
        View header_gameType = getLayoutInflater().inflate(R.layout.list_item_header, null);
        TextView tv = (TextView) header_gameType.findViewById(R.id.list_item_header);
        tv.setText("Pick a Game");
        tv.setTextColor(getResources().getColor(R.color.pink));
        listView_left.addHeaderView(header_gameType);
        //Set the adapter to the layout and array
        arrayAdapter_left = new ArrayAdapter<>(this, R.layout.list_item, drawerItems);
        //Bind the adapter to the listView
        listView_left.setAdapter(arrayAdapter_left);
        //On item click
        listView_left.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                switch (position) {
                    case 1 : isTimed = false;
                        break;
                    case 2 : isTimed = true;
                        break;
                    case 3 : wordLengthSelected = 3;
                        break;
                    case 4 : wordLengthSelected = 4;
                        break;
                    case 5 : wordLengthSelected = 5;
                        break;
                    case 6 : wordLengthSelected = 6;
                        break;
                    case 7 : wordLengthSelected = 7;
                        break;
                }
                Intent intent = new Intent(MainActivity.this, MainActivity.class);
                intent.putExtra("IS_TIMED", isTimed);
                intent.putExtra("WORD_LENGTH", wordLengthSelected);
                startActivity(intent);
            }
        });

        String[] drawerItems_right = getResources().getStringArray(R.array.nav_settings);
        //Add a header
        View header_settings = getLayoutInflater().inflate(R.layout.list_item_header, null);
        TextView tv2 = (TextView) header_settings.findViewById(R.id.list_item_header);
        tv2.setText("Settings");
        tv2.setTextColor(getResources().getColor(R.color.teal));
        listView_right.addHeaderView(header_settings);
        //Set the adapter to the layout and array
         arrayAdapter_right = new ArrayAdapter<>(this, R.layout.list_item, drawerItems_right);
        //Bind the adapter to the listView
        listView_right.setAdapter(arrayAdapter_right);
        //On item click
        listView_right.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                switch (position) {
                    case 1 : isTimed = false;
                        break;
                    case 2 : isTimed = true;
                        break;
                    case 3 : wordLengthSelected = 3;
                        break;
                    case 4 : wordLengthSelected = 4;
                        break;
                    case 5 : wordLengthSelected = 5;
                        break;
                    case 6 : wordLengthSelected = 6;
                        break;
                    case 7 : wordLengthSelected = 7;
                        break;
                }
                Intent intent = new Intent(MainActivity.this, MainActivity.class);
                intent.putExtra("IS_TIMED", isTimed);
                intent.putExtra("WORD_LENGTH", wordLengthSelected);
                startActivity(intent);
            }
        });
    }
}