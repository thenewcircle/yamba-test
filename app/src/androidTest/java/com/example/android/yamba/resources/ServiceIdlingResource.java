package com.example.android.yamba.resources;

import android.app.ActivityManager;
import android.app.Service;
import android.content.Context;
import android.support.test.espresso.IdlingResource;

import java.util.List;

public class ServiceIdlingResource implements IdlingResource {
    private ActivityManager mActivityManager;
    private Class<? extends Service> mResourceClass;
    private ResourceCallback mResourceCallback;

    public ServiceIdlingResource(Context context,
                                 Class<? extends Service> classParameter) {
        mActivityManager = (ActivityManager) context
                .getSystemService(Context.ACTIVITY_SERVICE);
        mResourceClass = classParameter;
    }

    //Unique name required for each resource
    @Override
    public String getName() {
        return mResourceClass.getSimpleName();
    }

    //Called by Espresso periodically to check status
    @Override
    public boolean isIdleNow() {
        boolean idle = !isServiceRunning();
        //Must do this before returning true to avoid IllegalStateException
        if (idle && mResourceCallback != null) {
            mResourceCallback.onTransitionToIdle();
        }
        return idle;
    }

    //Called by Espresso with the callback we need to notify state changes
    @Override
    public void registerIdleTransitionCallback(ResourceCallback resourceCallback) {
        mResourceCallback = resourceCallback;
    }

    private boolean isServiceRunning() {
        List<ActivityManager.RunningServiceInfo> services =
                mActivityManager.getRunningServices(Integer.MAX_VALUE);

        for (ActivityManager.RunningServiceInfo info : services) {
            if (info.service.getClassName().equals(mResourceClass.getName())) {
                return true;
            }
        }

        return false;
    }
}
