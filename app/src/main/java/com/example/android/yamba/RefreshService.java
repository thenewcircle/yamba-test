package com.example.android.yamba;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import com.thenewcircle.yamba.client.YambaClient;
import com.thenewcircle.yamba.client.YambaClientException;
import com.thenewcircle.yamba.client.YambaClientInterface;
import com.thenewcircle.yamba.client.YambaStatus;

import java.util.List;

public class RefreshService extends IntentService {
    private static final String TAG =
            RefreshService.class.getSimpleName();

    public RefreshService() {
        super(TAG);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        try {
            final String username = "student";
            final String password = "password";

            YambaClientInterface cloud = YambaClient.getClient(username, password);
            List<YambaStatus> timeline = cloud.getTimeline(20);

            //Log the outcome for now…
            Log.i(TAG, "Received " + timeline.size() + " updates");
        } catch (YambaClientException e) {
            Log.e(TAG, "Failed to fetch the timeline", e);
        }
    }
}
