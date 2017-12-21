package com.lollipop.dreamcatcher;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceManager;
import android.support.v4.content.IntentCompat;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

/**
 * Created by Vincent on 28/05/2016.
 */
public class SettingsActivity extends AppCompatActivity {

    public static final String KEY_PREF_DARK_MODE = "pref_key_dark_mode_switch";
    public static final String KEY_PREF_NOTIFICATION = "pref_key_notification_switch";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Choose theme with an action bar
        if (PreferenceManager.getDefaultSharedPreferences(this)
                .getBoolean(SettingsActivity.KEY_PREF_DARK_MODE, false)) {
            setTheme(R.style.AppTheme_Dark);
        }
        super.onCreate(savedInstanceState);

        // Display the fragment as the main content.
        getFragmentManager().beginTransaction()
                .replace(android.R.id.content, new SettingsFragment())
                .commit();
    }
}
