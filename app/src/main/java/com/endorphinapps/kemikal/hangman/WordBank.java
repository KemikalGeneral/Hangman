package com.endorphinapps.kemikal.hangman;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Richard 'Kemikal General' Denton on 08/09/2016.
 */
public class WordBank extends AppCompatActivity{

    private String word;
    private String category;
    private List<String> words = new ArrayList<>();
    private List<String> categories = new ArrayList<>();

    public List<String> readTextFileAsList(Context ctx, int resId) {
        InputStream inputStream = ctx.getResources().openRawResource(resId);
        InputStreamReader inputreader = new InputStreamReader(inputStream);
        BufferedReader bufferedreader = new BufferedReader(inputreader);
        String line;

        String category = null;
        try {
            //While there are still lines left
            while (( line = bufferedreader.readLine()) != null) {
                //Add to hash map
                if (line.contains(".")) {
                    //Remove the proceeding period, donating a category
                    line = line.substring(1, line.length());
                    //Set the category name to the current line
                    category = null;
                    category = line;
                } else {
                    //Add to the array
                    words.add(line.toLowerCase());
                    categories.add(category);
                }
            }
        }
        catch (IOException e) {
            return null;
        }
        return words;
    }

    public int getSizeOfWordBank() {
        Log.v("z! ====================", "================" );
        Log.v("z! getSizeOfWorkBank", "" + words.size());
        return words.size();
    }

    public String getWord(int number) {
        word = words.get(number);
//        Log.v("z! Words", words.toString());
        Log.v("z! Word", word);
        return word;
    }

    public String getCategory(int number) {
        category = categories.get(number);
//        Log.v("z! Categories", categories.toString());
        Log.v("z! Category", category);
        return category;
    }
}
