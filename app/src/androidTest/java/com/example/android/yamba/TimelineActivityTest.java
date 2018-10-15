package com.example.android.yamba;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.Espresso;
import android.support.test.espresso.IdlingRegistry;
import android.support.test.espresso.NoMatchingViewException;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.example.android.yamba.resources.ServiceIdlingResource;
import com.thenewcircle.yamba.client.OfflineYambaClient;
import com.thenewcircle.yamba.client.YambaClient;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.doesNotExist;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withChild;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static com.google.common.truth.Truth.assertThat;
import static org.hamcrest.CoreMatchers.anything;

@RunWith(AndroidJUnit4.class)
public class TimelineActivityTest {

    private ServiceIdlingResource mIdlingResource;

    //Instantiate and provide access to the Activity under test
    @Rule
    public ActivityTestRule<MainActivity> activityRule =
            new ActivityTestRule<>(MainActivity.class);

    @Before
    public void injectMockYambaClient() {
        //Inject Yamba Client's hermetic test instance
        YambaClient.setClientInstance(OfflineYambaClient.newClient());
    }

    @Before
    public void attachIdlingResource() {
        final Context context = InstrumentationRegistry.getTargetContext();
        mIdlingResource =
                new ServiceIdlingResource(context, RefreshService.class);
        IdlingRegistry.getInstance().register(mIdlingResource);
    }

    @After
    public void detachIdlingResource() {
        IdlingRegistry.getInstance().unregister(mIdlingResource);
    }

    @Test
    public void launchSettingsFromOverflowMenu() {
        //Open overflow menu
        Context context = activityRule.getActivity().getApplicationContext();
        Espresso.openActionBarOverflowOrOptionsMenu(context);
        //Click settings item
        onView(withText(R.string.action_settings)).perform(click());

        //Confirm navigation takes you to page with content specific to settings
        onView(withText(R.string.username_summary)).check(matches(isDisplayed()));
    }

    @Test
    public void launchPostFromOptionsMenu() {
        try {
            //Attempt to click post from the action bar
            onView(withId(R.id.action_post)).perform(click());
        } catch (NoMatchingViewException e) {
            //Open OverflowMenu and click post
            Context context = activityRule.getActivity().getApplicationContext();
            Espresso.openActionBarOverflowOrOptionsMenu(context);
            onView(withText(R.string.action_post)).perform(click());
        }

        //Confirm navigation takes you to page with content specific to status
        onView(withId(R.id.status_text)).check(matches(isDisplayed()));
    }

    @Test
    public void refreshLoadsListData() {
        //Purge the provider
        Context context = InstrumentationRegistry.getTargetContext();
        Espresso.openActionBarOverflowOrOptionsMenu(context);
        onView(withText(R.string.action_purge))
                .perform(click());

        //Verify list is empty (no item views)
        onView(withId(R.id.text_user)).check(doesNotExist());

        //Trigger a refresh
        // IdlingResource will ensure test waits…
        onView(withId(R.id.action_refresh))
                .perform(click());

        //Verify list has contents (successful refresh)
        onData(anything())
                .atPosition(0)
                .check(matches(withChild(withId(R.id.text_user))));
    }

    @Test
    public void listItemClickShowsDetails() {
        //Trigger a refresh
        // IdlingResource will ensure test waits…
        onView(withId(R.id.action_refresh))
                .perform(click());

        //Reference the current local data set
        final ContentResolver resolver = InstrumentationRegistry
                .getTargetContext().getContentResolver();
        Cursor cursor = resolver.query(StatusContract.CONTENT_URI,
                null, null, null, StatusContract.DEFAULT_SORT);

        //Verify the cursor can be initialized
        assertThat(cursor).isNotNull();
        assertThat(cursor.moveToFirst()).isTrue();

        //Get the data found in that list item
        String itemUser = cursor.getString(
                cursor.getColumnIndex(StatusContract.Column.USER));
        String itemMessage = cursor.getString(
                cursor.getColumnIndex(StatusContract.Column.MESSAGE));

        //Click the associated list item
        onData(anything())
                .inAdapterView(withId(android.R.id.list))
                .atPosition(cursor.getPosition())
                .perform(click());

        cursor.close();

        //Verify selected item text is displayed on next activity
        onView(withId(R.id.text_user))
                .check(matches(withText(itemUser)));
        onView(withId(R.id.text_message))
                .check(matches(withText(itemMessage)));
    }
}