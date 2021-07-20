package com.lakshyagupta7089.triviaapp.util;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

public class Prefs {
    public static final String HIGHEST_SCORE = "highest_score";
    private SharedPreferences sharedPreferences;

    public Prefs(Activity context) {
        this.sharedPreferences = context.getPreferences(Context.MODE_PRIVATE);
    }

    public void saveHighestScore(int score) {
        int lastScore = sharedPreferences.getInt(HIGHEST_SCORE, 0);

        if (score > lastScore) {
            sharedPreferences.edit().putInt(HIGHEST_SCORE, score).apply();
        }
    }

    public int getHighestScore() {
        return sharedPreferences.getInt(HIGHEST_SCORE, 0);
    }
}
