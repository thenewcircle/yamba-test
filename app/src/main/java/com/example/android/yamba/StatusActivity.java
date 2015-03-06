package com.example.android.yamba;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;

public class StatusActivity extends ActionBarActivity {
    private static final String TAG = StatusActivity.class.getSimpleName();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreated");
    }

}
