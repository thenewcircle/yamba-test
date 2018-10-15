package com.example.android.yamba;

import android.Manifest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.rule.GrantPermissionRule;

import com.example.android.yamba.pages.MainPageObject;
import com.example.android.yamba.runners.MyRunner;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.CoreMatchers.not;

@RunWith(MyRunner.class)
public class TimelineActivityTest {
    @Rule
    public GrantPermissionRule permissionRule =
            GrantPermissionRule.grant(Manifest.permission.WRITE_EXTERNAL_STORAGE);

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
        // Let's break this one
        onView(withId(R.id.status_text)).check(matches(not(isDisplayed())));
    }
}