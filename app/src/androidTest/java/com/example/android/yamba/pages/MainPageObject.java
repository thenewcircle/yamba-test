package com.example.android.yamba.pages;


import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.Espresso;
import android.support.test.espresso.NoMatchingViewException;

import com.example.android.yamba.R;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.CoreMatchers.allOf;

public class MainPageObject {

    // overflow menu
    private static void openOverflowMenu() {
        Context context = InstrumentationRegistry.getTargetContext();
        Espresso.openActionBarOverflowOrOptionsMenu(context);
    }

    // settings overflow item
    private static void clickSettingsOverflow() {
        onView(allOf(withText(R.string.action_settings), withId(R.id.title)))
                .perform(click());
    }

    // post overflow item
    private static void clickPostOverflow() {
        onView(allOf(withText(R.string.action_post), withId(R.id.title)))
                .perform(click());
    }

    // post action item
    private static void clickPostItem() {
        onView(withId(R.id.action_post))
                .perform(click());
    }

    // INTERACTIONS
    public static void navigateToSettings() {
        openOverflowMenu();
        clickSettingsOverflow();
    }

    public static void navigateToPost() {
        try {
            //Attempt to get the action bar item first
            clickPostItem();
        } catch (NoMatchingViewException e) {
            //Try the overflow menu instead
            openOverflowMenu();
            clickPostOverflow();
        }
    }
}
