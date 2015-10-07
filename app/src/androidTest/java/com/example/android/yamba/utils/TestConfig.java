package com.example.android.yamba.utils;


import android.os.Bundle;
import android.support.test.InstrumentationRegistry;

public class TestConfig {
    private static final String USER = "tester";
    private static final String PASSWORD = "test";
    private static final String MESSAGE = "blah blah";

    private static Bundle sTestArguments =
            InstrumentationRegistry.getArguments();

    public static String getUsername() {
        return sTestArguments.getString("testUser", USER);
    }

    public static String getPassword() {
        return sTestArguments.getString("testPassword", PASSWORD);
    }

    public static String getTestMessage() {
        return sTestArguments.getString("testMessage", MESSAGE);
    }
}
