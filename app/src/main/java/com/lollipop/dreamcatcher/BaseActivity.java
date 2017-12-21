package com.lollipop.dreamcatcher;

import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by Vincent on 29/05/2016.
 */
public class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this.setColorTheme();
        super.onCreate(savedInstanceState);
    }

    protected void setColorTheme(){
        if (PreferenceManager.getDefaultSharedPreferences(this)
                .getBoolean(SettingsActivity.KEY_PREF_DARK_MODE, false)) {
            setTheme(R.style.AppTheme_Dark_NoActionBar);
        }
    }
}
