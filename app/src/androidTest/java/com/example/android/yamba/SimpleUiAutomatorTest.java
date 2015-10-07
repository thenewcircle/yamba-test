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
import android.support.test.uiautomator.Until;

import com.example.android.yamba.pages.MainPageObject;
import com.example.android.yamba.pages.SettingsPageObject;
import com.example.android.yamba.pages.StatusPageObject;
import com.example.android.yamba.utils.SystemUiHelper;
import com.example.android.yamba.utils.SystemUiHelperFactory;
import com.example.android.yamba.utils.TestConfig;

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
    private SystemUiHelper mUiHelper;

    @Before
    public void startActivityFromHomeScreen() {
        // Initialize UiDevice instance
        mDevice = UiDevice.getInstance(
                InstrumentationRegistry.getInstrumentation());
        mUiHelper = SystemUiHelperFactory.create(mDevice);

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
        String username = TestConfig.getUsername();
        String password = TestConfig.getPassword();
        String testMessage = TestConfig.getTestMessage();
        String errorNotificationMessage = "Error posting status update";

        //navigate to settings to log in as a user
        MainPageObject.navigateToSettings();

        //log in as an incorrect user: tester, password: test
        SettingsPageObject.setUsername(username);
        SettingsPageObject.setPassword(password);

        //return to main activity
        mDevice.pressBack();

        //click post message
        MainPageObject.navigateToPost();

        //compose message and click post
        StatusPageObject.sendStatus(testMessage);

        //open notifications shade and confirm contents
        boolean notificationOpen = mDevice.openNotification();
        assertThat(notificationOpen)
                .named("able to open notifications?").isTrue();

        boolean notificationTextExists = mDevice.wait(Until.hasObject(
                By.textContains(errorNotificationMessage)), UI_TIMEOUT);
        assertThat(notificationTextExists)
                .named("error notification exists?").isTrue();

        //dismiss the notification
        mUiHelper.clearAllNotifications(UI_TIMEOUT);
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
