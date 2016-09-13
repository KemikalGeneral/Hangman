package com.endorphinapps.kemikal.hangman;

import android.support.v7.app.AppCompatActivity;

/**
 * Created by Richard 'Kemikal General' Denton on 08/09/2016.
 */
public class WordBank extends AppCompatActivity{

    private String[] words = new String[] {"red", "orange", "yellow", "green", "blue", "indigo", "violet", "a", "bb", "ccc", "dddd", "eeeee", "ffffff"};


    public String getWord(int number) {
        return words[number];
    }

    public int getSizeOfWordBank() {
        return words.length-1;
    }
}
