package com.endorphinapps.kemikal.hangman;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class SplashScreen extends AppCompatActivity {

    private Button btn_originalGame;
    private Button btn_timedGame;
    private Button btn_wordLength;
    private Button btn_wordCategory;
    private Boolean isTimed;
    private String category = "any";
    private MediaPlayer mediaPlayer;
    private TextView tv_splashScreenTitle;

    //Start the music playing again when the activity is back in focus
    @Override
    protected void onRestart() {
        mediaPlayer = MediaPlayer.create(this, R.raw.marimba_music);
        mediaPlayer.setLooping(true);
        mediaPlayer.start();
        super.onRestart();
    }

    //Stop the music from playing when the activity has lost focus
    @Override
    protected void onPause() {
        mediaPlayer.stop();
        super.onPause();
    }

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        //Find all views
        findViews();

        mediaPlayer = MediaPlayer.create(this, R.raw.marimba_music);
        mediaPlayer.setLooping(true);
        mediaPlayer.start();

        //Set Font for Views containing text
        final Typeface typeface = Typeface.createFromAsset(getAssets(), "fonts/crayon_font.ttf");
        setFonts(typeface);

        //Start original game
        btn_originalGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mediaPlayer.stop();
                isTimed = false;
                Intent intent = new Intent(SplashScreen.this, MainActivity.class);
                intent.putExtra("EXTRAS_TIMED", isTimed);
                intent.putExtra("EXTRAS_CATEGORY", category);
                startActivity(intent);
            }
        });

        //Start timed game
        btn_timedGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mediaPlayer.stop();
                isTimed = true;
                Intent intent = new Intent(SplashScreen.this, MainActivity.class);
                intent.putExtra("EXTRAS_TIMED", isTimed);
                intent.putExtra("EXTRAS_CATEGORY", category);
                startActivity(intent);
            }
        });

        //Show dialogue box to word Length
        btn_wordLength.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showLengthDialogueBox(v);
            }
        });

        //Show dialogue box to chose a word category
        btn_wordCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCategoryDialogueBox(v);
            }
        });
    }

    /** Find all Views **/
    private void findViews() {
        tv_splashScreenTitle = (TextView) findViewById(R.id.splash_screen_title);
        btn_originalGame = (Button) findViewById(R.id.original_game);
        btn_timedGame = (Button) findViewById(R.id.timed_game);
        btn_wordLength = (Button) findViewById(R.id.word_length);
        btn_wordCategory = (Button) findViewById(R.id.word_category);
    }

    /** Apply fonts to all views containing text **/
    /** @param typeface
     */
    private void setFonts(Typeface typeface) {
        tv_splashScreenTitle.setTypeface(typeface);
        btn_originalGame.setTypeface(typeface);
        btn_timedGame.setTypeface(typeface);
        btn_wordLength.setTypeface(typeface);
        btn_wordCategory.setTypeface(typeface);
    }

    /** Show Dialogue box to select a number **/
     /** @param view
     */
    public void showLengthDialogueBox(View view){

        //TODO - Create custom layout for the dialogue box

        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.activity_dialog_box);

        //Array of possible numbers for the list
        String[] amount = {"3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13"};
        //Create a Dialogue box
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        //Add a title
        alertDialogBuilder.setTitle("How many letters?");
        //Set array as items
        alertDialogBuilder.setItems(amount, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                int numberOfLetters = 0;

                switch (which) {
                    case 0 : numberOfLetters = 3;
                        break;
                    case 1 : numberOfLetters = 4;
                        break;
                    case 2 : numberOfLetters = 5;
                        break;
                    case 3 : numberOfLetters = 6;
                        break;
                    case 4 : numberOfLetters = 7;
                        break;
                    case 5 : numberOfLetters = 8;
                        break;
                    case 6 : numberOfLetters = 9;
                        break;
                    case 7 : numberOfLetters = 10;
                        break;
                    case 8 : numberOfLetters = 11;
                        break;
                    case 9 : numberOfLetters = 12;
                        break;
                    case 10 : numberOfLetters = 13;
                        break;
                }
                mediaPlayer.stop();
                Intent intent = new Intent(SplashScreen.this, MainActivity.class);
                intent.putExtra("EXTRAS_LENGTH", numberOfLetters);
                intent.putExtra("EXTRAS_CATEGORY", category);
                startActivity(intent);
            }
        });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    /** Show dialogue box to select a category **/
     /** @param view
     */
    private void showCategoryDialogueBox(View view) {
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.activity_dialog_box);

        //Array of possible categories
        final String[] categories = {"Animals", "Body", "Boys Names", "Car Parts", "Clothes", "Colours", "Countries", "Elements", "Family",
                "Fruits", "Girls Names", "Herbs/Spices", "Instruments", "Metals", "Shapes", "Space", "Sports",
                "Transport", "Vegetables", "Weather"};
        //Create a Dialogue box
        AlertDialog.Builder alertDialogueBuilder = new AlertDialog.Builder(this);
        //Add a title
        alertDialogueBuilder.setTitle("Pick a category");
        //Set array as items
        alertDialogueBuilder.setItems(categories, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //Onclick position is relevant to the index of the array
                category = categories[which];

                mediaPlayer.stop();
                Intent intent = new Intent(SplashScreen.this, MainActivity.class);
                intent.putExtra("EXTRAS_CATEGORY", category);
                startActivity(intent);
            }
        });
        AlertDialog alertDialog = alertDialogueBuilder.create();
        alertDialog.show();
    }
}
