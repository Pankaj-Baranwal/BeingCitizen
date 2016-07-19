package com.beingcitizen.beingcitizen;

/**
 * Created by saransh on 14-06-2015.
 */
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;

public class splashscreen extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(splashscreen.this);
        if (sp.contains("id")) {
            Intent i = new Intent(splashscreen.this, MainActivity.class);
            startActivity(i);
        } else {
            Intent i = new Intent(splashscreen.this, LoginMain.class);
            startActivity(i);
        }
        finish();
    }

    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }
}