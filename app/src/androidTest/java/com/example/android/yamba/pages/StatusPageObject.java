package com.example.android.yamba.pages;

import com.example.android.yamba.R;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.clearText;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

public class StatusPageObject {

    private static final int MAX_CHARS = 140;

    //post button
    private static void clickPost() {
        onView(withText(R.string.status_button)).perform(click());
    }

    public static String expectedCount(String status) {
        return String.valueOf(MAX_CHARS - status.length());
    }

    // INTERACTIONS:
    public static void typeStatus(String status) {
        onView(withId(R.id.status_text)).perform(typeText(status));
    }

    public static void clearStatus() {
        onView(withId(R.id.status_text)).perform(clearText());
    }

    public static void sendStatus(String status) {
        typeStatus(status);
        clickPost();
    }
}
