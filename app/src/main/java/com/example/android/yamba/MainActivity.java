package com.example.android.yamba;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

public class MainActivity extends ActionBarActivity implements TimelineFragment.OnTimelineItemSelectedListener {

    //Global notifier of when timeline is in the foreground
    private static boolean inTimeline = false;
    public static boolean isInTimeline() {
        return inTimeline;
    }

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
	}

    @Override
    protected void onResume() {
        super.onResume();
        inTimeline = true;
    }

    @Override
    protected void onPause() {
        super.onPause();
        inTimeline = false;
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
		case R.id.action_tweet:
			startActivity(new Intent("com.example.android.yamba.action.tweet"));
			return true;
		case R.id.action_refresh:
			startService(new Intent(this, RefreshService.class));
			return true;
		case R.id.action_purge:
			int rows = getContentResolver().delete(StatusContract.CONTENT_URI, null, null);
			Toast.makeText(this, "Deleted "+rows+" rows", Toast.LENGTH_LONG).show();
			return true;
		default:
			return false;
		}
	}

    //Handle list item selections from the timeline
    @Override
    public void onTimelineItemSelected(long id) {
        // Get the details fragment
        DetailsFragment fragment = (DetailsFragment) getSupportFragmentManager()
                .findFragmentById(R.id.fragment_details);

        // Is details fragment visible?
        if (fragment != null && fragment.isVisible()) {
            fragment.updateView(id);
        } else {
            Intent intent = new Intent(this, DetailsActivity.class);
            intent.putExtra(StatusContract.Column.ID, id);

            startActivity(intent);
        }
    }
}
