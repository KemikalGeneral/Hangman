package com.endorphinapps.kemikal.hangman;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class SplashScreen extends AppCompatActivity {

    private Boolean isMusicEnabled;
    private Button btn_originalGame;
    private Button btn_timedGame;
    private Button btn_wordLength;
    private Button btn_wordCategory;
    private Boolean isTimed;
    private ImageView iv_hangmanImage;
    private LinearLayout ll_pageContainer;
    private String category = "any";
    private MediaPlayer music;
    private TextView tv_splashScreenTitle;
    private SharedPreferences sharedPreferences;

    //Start the music playing again when the activity is back in focus
    @Override
    protected void onRestart() {
        if (isMusicEnabled) {
            music = MediaPlayer.create(this, R.raw.marimba_music);
            music.setLooping(true);
            music.start();
        }
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.top_rocking);
        iv_hangmanImage.startAnimation(animation);
        super.onRestart();
    }

    //Stop the music from playing when the activity has lost focus
    @Override
    protected void onPause() {
        if (isMusicEnabled) {
            stopMusic();
        }
        super.onPause();
    }

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        //Get SharedPreferences for music
        sharedPreferences = getSharedPreferences("hangmanPrefs", MODE_PRIVATE);
        isMusicEnabled = sharedPreferences.getBoolean("isMusicEnabled", true);

        //Find all views
        findViews();

        if (isMusicEnabled) {
            music = MediaPlayer.create(this, R.raw.marimba_music);
            music.setLooping(true);
            music.start();
        }

        //Set Font for Views containing text
        final Typeface typeface = Typeface.createFromAsset(getAssets(), "fonts/crayon_font.ttf");
        setFonts(typeface);

        //Start original game
        btn_originalGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopMusic();
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
                stopMusic();
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
                showLengthDialogueBox();
            }
        });

        //Show dialogue box to chose a word category
        btn_wordCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCategoryDialogueBox();
            }
        });

        onLoadAnimation();
    }

    /** Find all Views **/
    private void findViews() {
        tv_splashScreenTitle = (TextView) findViewById(R.id.splash_screen_title);
        iv_hangmanImage = (ImageView) findViewById(R.id.hangman_image);
        btn_originalGame = (Button) findViewById(R.id.original_game);
        btn_timedGame = (Button) findViewById(R.id.timed_game);
        btn_wordLength = (Button) findViewById(R.id.word_length);
        btn_wordCategory = (Button) findViewById(R.id.word_category);
        ll_pageContainer = (LinearLayout) findViewById(R.id.splash_screen_page_container);
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
    public void showLengthDialogueBox(){

        //TODO - Create custom layout for the dialogue box

        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.activity_dialog_box);

        //Array of possible numbers for the list
        final String[] amount = {"3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13"};
        //Create a Dialogue box
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        //Add a title
        alertDialogBuilder.setTitle("How many letters?");
        //Set array as items
        alertDialogBuilder.setItems(amount, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                //Onclick position is relevant to the index of the array
                int numberOfLetters = Integer.parseInt(amount[which]);

                stopMusic();
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
    private void showCategoryDialogueBox() {
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

                stopMusic();
                Intent intent = new Intent(SplashScreen.this, MainActivity.class);
                intent.putExtra("EXTRAS_CATEGORY", category);
                startActivity(intent);
            }
        });
        AlertDialog alertDialog = alertDialogueBuilder.create();
        alertDialog.show();
    }

    private void stopMusic() {
        if (isMusicEnabled) {
            music.stop();
        }
    }

    private void onLoadAnimation() {
        Animation animation5 = AnimationUtils.loadAnimation(this, R.anim.drop_down);
        btn_wordCategory.startAnimation(animation5);

        Animation animation4 = AnimationUtils.loadAnimation(this, R.anim.drop_down);
        animation4.setStartOffset(250);
        btn_wordLength.startAnimation(animation4);

        Animation animation3 = AnimationUtils.loadAnimation(this, R.anim.drop_down);
        animation3.setStartOffset(500);
        btn_timedGame.startAnimation(animation3);

        Animation animation2 = AnimationUtils.loadAnimation(this, R.anim.drop_down);
        animation2.setStartOffset(750);
        btn_originalGame.startAnimation(animation2);

        Animation animation1 = AnimationUtils.loadAnimation(this, R.anim.drop_down);
        animation1.setStartOffset(1000);
        tv_splashScreenTitle.startAnimation(animation1);

        AnimationSet animationSet = new AnimationSet(false);
        Animation zoom = AnimationUtils.loadAnimation(this, R.anim.zoom_from_center);
        zoom.setStartOffset(1250);
        animationSet.addAnimation(zoom);
        animationSet.addAnimation(AnimationUtils.loadAnimation(this, R.anim.top_rocking));
        iv_hangmanImage.startAnimation(animationSet);
    }
}
