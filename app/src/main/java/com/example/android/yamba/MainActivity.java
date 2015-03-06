package com.example.android.yamba;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.ActionBarActivity;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends ActionBarActivity implements
        LoaderManager.LoaderCallbacks<Cursor> {
    private static final String TAG = MainActivity.class.getSimpleName();

    private static final String[] FROM = {
            StatusContract.Column.USER,
            StatusContract.Column.MESSAGE,
            StatusContract.Column.CREATED_AT };
    private static final int[] TO = {
            R.id.text_user,
            R.id.text_message,
            R.id.text_created_at };

    private static final int LOADER_ID = 42;

    private SimpleCursorAdapter mAdapter;

    private static final SimpleCursorAdapter.ViewBinder VIEW_BINDER = new SimpleCursorAdapter.ViewBinder() {

        @Override
        public boolean setViewValue(View view, Cursor cursor, int columnIndex) {
            long timestamp;

            // Custom binding
            switch (view.getId()) {
                case R.id.text_created_at:
                    timestamp = cursor.getLong(columnIndex);
                    CharSequence relTime = DateUtils
                            .getRelativeTimeSpanString(timestamp);
                    ((TextView) view).setText(relTime);
                    return true;
                default:
                    return false;
            }
        }
    };

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        ListView timeline = new ListView(this);
        setContentView(timeline);

        mAdapter = new SimpleCursorAdapter(this, R.layout.list_item,
                null, FROM, TO, 0);
        mAdapter.setViewBinder(VIEW_BINDER);

        timeline.setAdapter(mAdapter);

        getSupportLoaderManager().initLoader(LOADER_ID, null, this);
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
			startActivity(new Intent(this, StatusActivity.class));
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

    // --- Loader Callbacks ---

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        if (id != LOADER_ID)
            return null;
        Log.d(TAG, "onCreateLoader");

        return new CursorLoader(this, StatusContract.CONTENT_URI,
                null, null, null, StatusContract.DEFAULT_SORT);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        Log.d(TAG, "onLoadFinished with cursor: " + cursor.getCount());
        mAdapter.swapCursor(cursor);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mAdapter.swapCursor(null);
    }
}
