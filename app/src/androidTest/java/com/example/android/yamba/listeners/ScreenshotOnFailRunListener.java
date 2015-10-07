package com.example.android.yamba.listeners;

import android.os.Environment;
import android.support.test.InstrumentationRegistry;
import android.support.test.uiautomator.UiDevice;
import android.util.Log;

import org.junit.runner.Description;
import org.junit.runner.notification.Failure;
import org.junit.runner.notification.RunListener;

import java.io.File;
import java.io.IOException;

public class ScreenshotOnFailRunListener extends RunListener {
    private static final String TAG =
            ScreenshotOnFailRunListener.class.getSimpleName();

    private UiDevice mDevice;
    private File mScreenshotDirectory;

    @Override
    public void testRunStarted(Description description) throws Exception {
        mDevice = UiDevice.getInstance(
                InstrumentationRegistry.getInstrumentation());
        mScreenshotDirectory = new File(
                Environment.getExternalStorageDirectory(),
                "test_screenshots");

        if (!mScreenshotDirectory.exists()) {
            boolean success = mScreenshotDirectory.mkdirs();
            if (!success) {
                Log.w(TAG, "Unable to create screenshot directory…");
            }
        } else {
            //Clear directory on new test runs
            Log.i(TAG, "Clearing out previous test screenshots…");
            for (File file : mScreenshotDirectory.listFiles()) {
                boolean success = file.delete();
                if (!success) {
                    Log.w(TAG, "Unable to delete "+file);
                }
            }
        }
    }

    @Override
    public void testFailure (Failure failure) throws IOException {
        final Description description = failure.getDescription();
        // build filename from class and method using .png as the file type
        String name = String.format("%s.%s.png",
                description.getClassName(),
                description.getMethodName());

        File screenshotFile = new File(mScreenshotDirectory, name);

        Log.i(TAG, "Attempting to capture screenshot at path: "
                + screenshotFile);

        mDevice.takeScreenshot(screenshotFile);
        mDevice.pressHome();
    }
}
