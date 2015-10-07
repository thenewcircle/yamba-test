package com.example.android.yamba;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.clearText;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isEnabled;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.CoreMatchers.not;

@RunWith(AndroidJUnit4.class)
public class StatusActivityTest {

    //Instantiate and provide access to the Activity under test
    @Rule
    public ActivityTestRule<StatusActivity> activityRule =
            new ActivityTestRule<>(StatusActivity.class);

    @Test
    public void characterCounterInitiallyAtMax() {
        //Set up the test
        int maxChars = 140;

        //Check for proper initial conditions
        onView(withId(R.id.status_text_count))
                .check(matches(withText(String.valueOf(maxChars))));
    }

    //Validate the character counter returns the correct value
    @Test
    public void characterCounterReturnsCorrect() {
        int maxChars = 140;
        String testString = "Test status update";

        //Enter test string
        onView(withId(R.id.status_text))
                .perform(typeText(testString));

        int count = maxChars - testString.length();
        //Validate the change
        onView(withId(R.id.status_text_count))
                .check(matches(withText(String.valueOf(count))));
    }

    //Validate the character counter handles zero conditions correctly
    @Test
    public void maxCharacterCountIsAllowed() {
        //Set up the test
        String testString = "This status update is exactly 140 characters."
                + " Are you impressed that I was able to make this match"
                + " without extra chars? I worked really hard";

        //Enter test string
        onView(withId(R.id.status_text))
                .perform(typeText(testString));

        //Validate the change
        onView(withId(R.id.status_text_count))
                .check(matches(withText(String.valueOf(0))));

        //User should be able to send this status
        onView(withId(R.id.status_button))
                .check(matches(isEnabled()));
    }

    //Validate the counter disables status overflows
    @Test
    public void counterOverflowDisablesPost() {
        //Set up the test
        //Type a really long test string, or use a lorem ipsum generator…
        String testString = "Bacon ipsum dolor amet tongue meatball bresaola,"
                + " corned beef chicken short ribs ham ham hock cupim shankle"
                + " pork short loin tenderloin chuck. Salami leberkas cow ribeye pancetta.";

        //Enter test string
        onView(withId(R.id.status_text))
                .perform(typeText(testString));

        //User should NOT be able to send this status
        onView(withId(R.id.status_button))
                .check(matches(not(isEnabled())));

        //Clear entry box
        onView(withId(R.id.status_text))
                .perform(clearText());

        //Button should be active again
        onView(withId(R.id.status_button))
                .check(matches(isEnabled()));
    }
}
