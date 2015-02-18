package com.example.android.yamba;

import android.os.Bundle;

public class DetailsActivity extends SubActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// Check if this activity was created before
		if (savedInstanceState == null) {
			// Create a fragment
            long statusId = getIntent().getLongExtra(StatusContract.Column.ID, -1);
			DetailsFragment fragment = DetailsFragment.newInstance(statusId);
			getSupportFragmentManager()
					.beginTransaction()
					.add(android.R.id.content, fragment,
							fragment.getClass().getSimpleName()).commit();
		}
	}
}
