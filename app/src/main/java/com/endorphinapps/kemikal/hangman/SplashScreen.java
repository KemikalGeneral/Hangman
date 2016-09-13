package com.endorphinapps.kemikal.hangman;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class SplashScreen extends AppCompatActivity {

    private Button btn_originalGame;
    private Button btn_timedGame;
    private Button btn_wordLength;
    private Boolean isTimed;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        //Find all views
        findViews();

        //Start original game
        btn_originalGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isTimed = false;
                Intent intent = new Intent(SplashScreen.this, MainActivity.class);
                intent.putExtra("IS_TIMED", isTimed);
                startActivity(intent);
            }
        });

        //Start timed game
        btn_timedGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isTimed = true;
                Intent intent = new Intent(SplashScreen.this, MainActivity.class);
                intent.putExtra("IS_TIMED", isTimed);
                startActivity(intent);
            }
        });

        //Show dialogue box to start a Word Length game
        btn_wordLength.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialogueBox(v);
            }
        });
    }

    /** Find all Views **/
    private void findViews() {
        btn_originalGame = (Button) findViewById(R.id.original_game);
        btn_timedGame = (Button) findViewById(R.id.timed_game);
        btn_wordLength = (Button) findViewById(R.id.word_length);
    }

    /** Show Dialogue box to select a number **/
     /** @param view
     */
    public void showDialogueBox(View view){

        //TODO - Create custom layout for the dialogue box

        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.activity_dialog_box);

        //Array of possible numbers for the list
        String[] amount = {"3", "4", "5", "6", "7"};
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
                }
                Intent intent = new Intent(SplashScreen.this, MainActivity.class);
                intent.putExtra("WORD_LENGTH", numberOfLetters);
                startActivity(intent);
            }
        });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }
}
