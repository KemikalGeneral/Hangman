package com.endorphinapps.kemikal.hangman;

/**
 * Created by Richard 'Kemikal General' Denton on 08/09/2016.
 */
public class WordBank {

    private String[] words = new String[] {"red", "orange", "yellow", "green", "blue", "indigo", "violet"};

    public String getWord(int number) {
        return words[number];
    }

    public int getSizeOfWordBank() {
        return words.length-1;
    }
}
