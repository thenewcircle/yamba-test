package com.example.android.yamba;

import android.content.Context;
import android.support.test.espresso.Espresso;
import android.support.test.espresso.NoMatchingViewException;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

@RunWith(AndroidJUnit4.class)
public class TimelineActivityTest {

    //Instantiate and provide access to the Activity under test
    @Rule
    public ActivityTestRule<MainActivity> activityRule =
            new ActivityTestRule<>(MainActivity.class);

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
}