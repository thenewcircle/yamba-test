package com.example.android.yamba;


import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class StatusFragment extends Fragment {
	private static final String TAG = StatusFragment.class.getSimpleName();
	private static final String PROVIDER = LocationManager.GPS_PROVIDER;

	private Button mButtonTweet;
	private EditText mTextStatus;
	private TextView mTextCount;
	private int mDefaultColor;
	private LocationManager locationManager;
	private Location mLastLocation;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		locationManager = (LocationManager) getActivity().getSystemService(
				Context.LOCATION_SERVICE);
        mLastLocation = locationManager.getLastKnownLocation(PROVIDER);
	}

	@Override
	public void onResume() {
		super.onResume();
		locationManager.requestLocationUpdates(PROVIDER, 60000, 1000,
				LOCATION_LISTENER);
	}

	@Override
	public void onPause() {
		super.onPause();
		locationManager.removeUpdates(LOCATION_LISTENER);
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

	@Override
	public View onCreateView(final LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
        Log.d(TAG, "onCreated");
		View v = inflater.inflate(R.layout.fragment_status, container, false);

		mButtonTweet = (Button) v.findViewById(R.id.status_button_tweet);
		mTextStatus = (EditText) v.findViewById(R.id.status_text);
		mTextCount = (TextView) v.findViewById(R.id.status_text_count);
		mTextCount.setText(Integer.toString(140));
		mDefaultColor = mTextCount.getTextColors().getDefaultColor();

		mButtonTweet.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				String status = mTextStatus.getText().toString();
				Log.d(TAG, "onClicked");

                //Send the update to our background service
                Intent intent = new Intent(getActivity(), StatusUpdateService.class);
                intent.putExtra(StatusUpdateService.EXTRA_MESSAGE, status);
                if (mLastLocation != null) {
                    intent.putExtra(StatusUpdateService.EXTRA_LOCATION, mLastLocation);
                }
                getActivity().startService(intent);

                //We're done here
                getActivity().finish();
			}

		});

		mTextStatus.addTextChangedListener(new TextWatcher() {

			@Override
			public void afterTextChanged(Editable s) {
				int count = 140 - s.length();
				mTextCount.setText(Integer.toString(count));

				if (count < 50) {
					mTextCount.setTextColor(Color.RED);
				} else {
					mTextCount.setTextColor(mDefaultColor);
				}

                mButtonTweet.setEnabled(count >= 0);
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
			}

		});

		return v;
	}

}
