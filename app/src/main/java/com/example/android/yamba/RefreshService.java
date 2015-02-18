package com.example.android.yamba;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat;
import android.text.TextUtils;
import android.util.Log;

import com.marakana.android.yamba.clientlib.YambaClient;
import com.marakana.android.yamba.clientlib.YambaClient.Status;
import com.marakana.android.yamba.clientlib.YambaClientException;

import java.util.List;

public class RefreshService extends IntentService {
	private static final String TAG = RefreshService.class.getSimpleName();

    public static final int NOTIFICATION_ID = 42;

    private NotificationManager mNotificationManager;

	public RefreshService() {
		super(TAG);
	}

	@Override
	public void onCreate() {
		super.onCreate();
		Log.d(TAG, "onCreated");
        mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
	}

	// Executes on a worker thread
	@Override
	protected void onHandleIntent(Intent intent) {
		SharedPreferences prefs = PreferenceManager
				.getDefaultSharedPreferences(this);
		final String username = prefs.getString("username", "");
		final String password = prefs.getString("password", "");

		// Check that username and password are not empty
		if (TextUtils.isEmpty(username) || TextUtils.isEmpty(password)) {
			Log.w(TAG, "Please update your username and password");
			return;
		}
		Log.d(TAG, "onStarted");

		ContentValues values = new ContentValues();

		YambaClient cloud = new YambaClient(username, password);
		try {
			int count = 0;
			List<Status> timeline = cloud.getTimeline(20);
			for (Status status : timeline) {
				values.clear();
				values.put(StatusContract.Column.ID, status.getId());
				values.put(StatusContract.Column.USER, status.getUser());
				values.put(StatusContract.Column.MESSAGE, status.getMessage());
				values.put(StatusContract.Column.CREATED_AT, status
						.getCreatedAt().getTime());

				Uri uri = getContentResolver().insert(
						StatusContract.CONTENT_URI, values);
				if (uri != null) {
					count++;
					Log.d(TAG,
							String.format("%s: %s", status.getUser(),
									status.getMessage()));
				}
			}

			if (count > 0 && !MainActivity.isInTimeline()) {
				postStatusNotification(count);
			}

		} catch (YambaClientException e) {
			Log.e(TAG, "Failed to fetch the timeline", e);
			e.printStackTrace();
		}
	}

    private void postStatusNotification(int count) {
        PendingIntent operation = PendingIntent.getActivity(this, 0,
                new Intent(this, MainActivity.class),
                PendingIntent.FLAG_ONE_SHOT);

        Notification notification = new NotificationCompat.Builder(this)
                .setContentTitle("New tweets!")
                .setContentText("You've got " + count + " new tweets")
                .setSmallIcon(android.R.drawable.sym_action_email)
                .setContentIntent(operation)
                .setAutoCancel(true)
                .build();

        mNotificationManager.notify(NOTIFICATION_ID, notification);
    }

	@Override
	public void onDestroy() {
		super.onDestroy();
		Log.d(TAG, "onDestroyed");
	}

}
