package com.example.android.yamba;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.example.android.yamba.pages.MainPageObject;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
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
        //Open OverflowMenu and click settings
        MainPageObject.navigateToSettings();

        //Confirm navigation takes you to page with content specific to settings
        onView(withText(R.string.username_summary)).check(matches(isDisplayed()));
    }

    @Test
    public void launchPostFromOptionsMenu() {
        //Discover and click post
        MainPageObject.navigateToPost();

        //Confirm navigation takes you to page with content specific to status
        onView(withId(R.id.status_text)).check(matches(isDisplayed()));
    }
}