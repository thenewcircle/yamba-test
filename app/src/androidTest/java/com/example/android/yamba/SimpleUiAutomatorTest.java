package com.example.android.yamba;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.support.test.InstrumentationRegistry;
import android.support.test.filters.SdkSuppress;
import android.support.test.runner.AndroidJUnit4;
import android.support.test.uiautomator.By;
import android.support.test.uiautomator.UiDevice;
import android.support.test.uiautomator.UiObject2;
import android.support.test.uiautomator.Until;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static com.google.common.truth.Truth.assertThat;

@RunWith(AndroidJUnit4.class)
@SdkSuppress(minSdkVersion = 18)
public class SimpleUiAutomatorTest {
    /**
     * The timeout to start the target app.
     */
    private static final int LAUNCH_TIMEOUT = 5000;

    /**
     * The target app package.
     */
    private static final String TARGET_PACKAGE =
            InstrumentationRegistry.getTargetContext().getPackageName();

    /**
     * The timeout to wait for UI actions.
     */
    private static final int UI_TIMEOUT = 1500;

    private UiDevice mDevice;

    @Before
    public void startActivityFromHomeScreen() {
        // Initialize UiDevice instance
        mDevice = UiDevice.getInstance(
                InstrumentationRegistry.getInstrumentation());

        // Start from the home screen
        mDevice.pressHome();

        // Wait for launcher
        final String launcherPackage = getLauncherPackageName();
        assertThat(launcherPackage).isNotNull();
        mDevice.wait(Until.hasObject(
                By.pkg(launcherPackage).depth(0)), LAUNCH_TIMEOUT);

        // Launch the target app
        Context context = InstrumentationRegistry.getContext();
        final Intent intent = context.getPackageManager()
                .getLaunchIntentForPackage(TARGET_PACKAGE);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        context.startActivity(intent);

        // Wait for the app to appear
        mDevice.wait(Until.hasObject(
                By.pkg(TARGET_PACKAGE).depth(0)), LAUNCH_TIMEOUT);
    }

    @After
    public void displayHomeAfterTest() {
        mDevice.pressHome();
    }

    @Test
    public void failedMessagePostShouldTriggerNotification() {
        String username = "tester";
        String password = "test";
        String testMessage = "blah blah";
        String errorNotificationMessage = "Error posting status update";

        //navigate to settings to log in as a user
        mDevice.findObject(By.descContains("More options"))
                .clickAndWait(Until.newWindow(), UI_TIMEOUT);
        mDevice.findObject(By.textContains("Settings"))
                .clickAndWait(Until.newWindow(), UI_TIMEOUT);

        //log in as an incorrect user: tester, password: test
        final Context context = InstrumentationRegistry.getTargetContext();
        String usernameTitle = context.getString(R.string.username);
        String passwordTitle = context.getString(R.string.password);

        mDevice.findObject(By.textContains(usernameTitle)).click();
        mDevice.wait(Until.hasObject(By.res("android", "edit")), UI_TIMEOUT);
        mDevice.findObject(By.res("android", "edit")).setText(username);
        mDevice.findObject(By.res("android", "button1"))
                .clickAndWait(Until.newWindow(), UI_TIMEOUT);

        mDevice.findObject(By.textContains(passwordTitle)).click();
        mDevice.wait(Until.hasObject(By.res("android", "edit")), UI_TIMEOUT);
        mDevice.findObject(By.res("android", "edit")).setText(password);
        mDevice.findObject(By.res("android", "button1"))
                .clickAndWait(Until.newWindow(), UI_TIMEOUT);

        //return to main activity
        mDevice.pressBack();

        //click post message
        UiObject2 postAction = mDevice.findObject(
                By.res(TARGET_PACKAGE, "action_post"));
        if (postAction == null) {
            // post is in the overflow on this device
            String postTitle = context.getString(R.string.action_post);
            mDevice.findObject(By.descContains("More options"))
                    .clickAndWait(Until.newWindow(), UI_TIMEOUT);
            mDevice.findObject(By.textContains(postTitle))
                    .clickAndWait(Until.newWindow(), UI_TIMEOUT);
        } else {
            postAction.clickAndWait(Until.newWindow(), UI_TIMEOUT);
        }

        //compose message and click post
        mDevice.findObject(By.res(TARGET_PACKAGE, "status_text"))
                .setText(testMessage);
        mDevice.findObject(By.res(TARGET_PACKAGE, "status_button"))
                .click();

        //open notifications shade and confirm contents
        boolean notificationOpen = mDevice.openNotification();
        assertThat(notificationOpen)
                .named("able to open notifications?").isTrue();

        boolean notificationTextExists = mDevice.wait(Until.hasObject(
                By.textContains(errorNotificationMessage)), UI_TIMEOUT);
        assertThat(notificationTextExists)
                .named("error notification exists?").isTrue();

        //on API 23+, the clear button is delayed for an animation
        mDevice.wait(Until.hasObject(
                By.descContains("Clear all notifications")), UI_TIMEOUT);
        //dismiss the notification
        mDevice.findObject(By.descContains("Clear all notifications"))
                .click();
    }

    private String getLauncherPackageName() {
        // Create launcher Intent
        final Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);

        // Use PackageManager to get the launcher package name
        PackageManager pm =
                InstrumentationRegistry.getContext().getPackageManager();
        ResolveInfo resolveInfo =
                pm.resolveActivity(intent, PackageManager.MATCH_DEFAULT_ONLY);
        return resolveInfo.activityInfo.packageName;
    }
}
