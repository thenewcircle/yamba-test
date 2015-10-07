package com.example.android.yamba.utils;

import android.os.Build;
import android.support.test.uiautomator.By;
import android.support.test.uiautomator.BySelector;
import android.support.test.uiautomator.UiDevice;
import android.support.test.uiautomator.Until;


public class SystemUiHelperFactory {
    //Return the proper impl for the current platform
    public static SystemUiHelper create(UiDevice device) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            return new LollipopSystemUiHelper(device);
        } else {
            return new BaseSystemUiHelper(device);
        }
    }

    //System UI actions for pre-21 devices
    private static class BaseSystemUiHelper implements SystemUiHelper {
        private UiDevice mDevice;
        public BaseSystemUiHelper(UiDevice device) {
            mDevice = device;
        }

        @Override
        public void clearAllNotifications(long timeout) {
            BySelector clearSelector =
                    By.res("com.android.systemui:id/clear_all_button");

            mDevice.wait(Until.hasObject(clearSelector), timeout);
            mDevice.findObject(clearSelector).click();
        }
    }

    //System UI actions for 21+ devices
    private static class LollipopSystemUiHelper implements SystemUiHelper {
        private UiDevice mDevice;
        public LollipopSystemUiHelper(UiDevice device) {
            mDevice = device;
        }

        @Override
        public void clearAllNotifications(long timeout) {
            BySelector clearSelector =
                    By.res("com.android.systemui:id/dismiss_text");

            mDevice.wait(Until.hasObject(clearSelector), timeout);
            mDevice.findObject(clearSelector).click();
        }
    }
}
