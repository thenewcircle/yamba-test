package com.example.android.yamba;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class StatusActivity extends ActionBarActivity implements
        View.OnClickListener, TextWatcher {
    private static final String TAG = StatusActivity.class.getSimpleName();

    private Button mButtonTweet;
    private EditText mTextStatus;
    private TextView mTextCount;
    private int mDefaultColor;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_status);
        Log.d(TAG, "onCreated");

        mButtonTweet = (Button) findViewById(R.id.status_button_tweet);
        mTextStatus = (EditText) findViewById(R.id.status_text);
        mTextCount = (TextView) findViewById(R.id.status_text_count);

        mDefaultColor = mTextCount.getTextColors().getDefaultColor();

        mButtonTweet.setOnClickListener(this);
        mTextStatus.addTextChangedListener(this);

        //If the intent contains our message (from an error retry), populate it
        // This also triggers our text watcher to set the initial character count
        mTextStatus.setText(getIntent().getStringExtra(StatusUpdateService.EXTRA_MESSAGE));
    }

    // -- TextWatcher --

    @Override
    public void afterTextChanged(Editable s) {
        int count = 140 - s.length();
        mTextCount.setText(Integer.toString(count));

        if (count < 10) {
            mTextCount.setTextColor(Color.RED);
        } else {
            mTextCount.setTextColor(mDefaultColor);
        }

        mButtonTweet.setEnabled(count >= 0);
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) { }

    // -- OnClickListener --

    @Override
    public void onClick(View v) {
        String status = mTextStatus.getText().toString();
        Log.d(TAG, "onClicked");

        //Send the update to our background service
        Intent intent = new Intent(this, StatusUpdateService.class);
        intent.putExtra(StatusUpdateService.EXTRA_MESSAGE, status);

        startService(intent);

        //Clear the UI
        mTextStatus.getText().clear();
    }

    // Called to lazily initialize the action bar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    // Called every time user clicks on an action
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                startActivity(new Intent(this, SettingsActivity.class));
                return true;
            case R.id.action_refresh:
                startService(new Intent(this, RefreshService.class));
                return true;
            default:
                return false;
        }
    }
}
