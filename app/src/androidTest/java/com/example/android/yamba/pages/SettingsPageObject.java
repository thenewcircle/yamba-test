package com.example.android.yamba.pages;


import com.example.android.yamba.R;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.clearText;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.matcher.ViewMatchers.withClassName;
import static android.support.test.espresso.matcher.ViewMatchers.withContentDescription;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.CoreMatchers.endsWith;

public class SettingsPageObject {

    //User Name
    private static void clickUsername() {
        onView(withText(R.string.username_summary)).perform(click());
    }

    //Password
    private static void clickPassword() {
        onView(withText(R.string.password_summary)).perform(click());
    }

    // INTERACTIONS:
    public static void setUsername(String username) {
        clickUsername();
        onView(withClassName(endsWith("EditText"))).perform(clearText());
        onView(withClassName(endsWith("EditText"))).perform(typeText(username));
        onView(withId(android.R.id.button1)).perform(click());
    }

    public static void setPassword(String password) {
        clickPassword();
        onView(withClassName(endsWith("EditText"))).perform(clearText());
        onView(withClassName(endsWith("EditText"))).perform(typeText(password));
        onView(withId(android.R.id.button1)).perform(click());
    }

    public static void navigateToTimeline() {
        onView(withContentDescription("Navigate up")).perform(click());
    }
}
