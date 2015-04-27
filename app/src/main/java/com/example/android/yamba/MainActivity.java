package com.example.android.yamba;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.app.AppCompatActivity;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements
        LoaderManager.LoaderCallbacks<Cursor>,
        SimpleCursorAdapter.ViewBinder {
    private static final String TAG = MainActivity.class.getSimpleName();

    private static final int LOADER_ID = 42;

    private static final String[] FROM = {
            StatusContract.Column.USER,
            StatusContract.Column.MESSAGE,
            StatusContract.Column.CREATED_AT };
    private static final int[] TO = {
            R.id.text_user,
            R.id.text_message,
            R.id.text_created_at };

    private SimpleCursorAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ListView timeline = new ListView(this);
        setContentView(timeline);

        mAdapter = new SimpleCursorAdapter(this, R.layout.list_item,
                null, FROM, TO, 0);
        mAdapter.setViewBinder(this);

        timeline.setAdapter(mAdapter);

        getSupportLoaderManager().initLoader(LOADER_ID, null, this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_yamba, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch (id) {
            case R.id.action_settings:
                return true;
            case R.id.action_refresh:
                startService(new Intent(this, RefreshService.class));
                return true;
            case R.id.action_purge:
                int rows = getContentResolver()
                        .delete(StatusContract.CONTENT_URI, null, null);
                Toast.makeText(this, "Deleted " + rows + " rows",
                        Toast.LENGTH_LONG).show();
                return true;
            case R.id.action_post:
                startActivity(new Intent(this, StatusActivity.class));
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        //Return all items from StatusProvider
        return new CursorLoader(this, StatusContract.CONTENT_URI,
                null, null, null, StatusContract.DEFAULT_SORT);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        Log.d(TAG, "onLoadFinished with cursor: " + data.getCount());
        mAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mAdapter.swapCursor(null);
    }

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
}
