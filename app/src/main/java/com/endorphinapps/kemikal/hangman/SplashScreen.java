package com.endorphinapps.kemikal.hangman;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class SplashScreen extends AppCompatActivity {

    private Button btn_originalGame;
    private Button btn_timedGame;
    private Boolean isTimed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
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
    }

    /** Find all Views **/
    private void findViews() {
        btn_originalGame = (Button) findViewById(R.id.original_game);
        btn_timedGame = (Button) findViewById(R.id.timed_game);
    }
}
