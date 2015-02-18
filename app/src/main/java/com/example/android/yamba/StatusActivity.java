package com.example.android.yamba;

import android.content.Intent;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class StatusActivity extends SubActivity implements
        View.OnClickListener, TextWatcher {
    private static final String TAG = StatusActivity.class.getSimpleName();
    private static final String PROVIDER = LocationManager.GPS_PROVIDER;

    private Button mButtonTweet;
    private EditText mTextStatus;
    private TextView mTextCount;
    private int mDefaultColor;
    private LocationManager mLocationManager;
    private Location mLastLocation;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_status);
        Log.d(TAG, "onCreated");

        mLocationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        mLastLocation = mLocationManager.getLastKnownLocation(PROVIDER);

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

    @Override
    public void onResume() {
        super.onResume();
        mLocationManager.requestLocationUpdates(PROVIDER, 60000, 1000,
                LOCATION_LISTENER);
    }

    @Override
    public void onPause() {
        super.onPause();
        mLocationManager.removeUpdates(LOCATION_LISTENER);
    }

    private LocationListener LOCATION_LISTENER = new LocationListener() {
        @Override
        public void onLocationChanged(Location l) {
            mLastLocation = l;
        }

        @Override
        public void onProviderDisabled(String provider) {
        }

        @Override
        public void onProviderEnabled(String provider) {
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
        }
    };

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
        if (mLastLocation != null) {
            intent.putExtra(StatusUpdateService.EXTRA_LOCATION, mLastLocation);
        }
        startService(intent);

        //We're done here
        finish();
    }
}
