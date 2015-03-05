package com.example.android.yamba;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;

public class SettingsActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Action bar stuff
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Check if this activity was created before
        if (savedInstanceState == null) {
            // PreferenceFragment is not included in the support library
            // We must use the core fragment manager (API 11+)
            SettingsFragment fragment = new SettingsFragment();
            getFragmentManager()
                    .beginTransaction()
                    .add(android.R.id.content, fragment,
                            fragment.getClass().getSimpleName()).commit();
        }
    }
}
