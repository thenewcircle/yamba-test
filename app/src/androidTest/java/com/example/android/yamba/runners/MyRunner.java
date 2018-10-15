package com.example.android.yamba.runners;

import com.example.android.yamba.listeners.ScreenshotOnFailRunListener;

import org.junit.runner.notification.RunNotifier;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.junit.runners.model.InitializationError;

public class MyRunner extends BlockJUnit4ClassRunner {

    public MyRunner(Class<?> klass) throws InitializationError {
        super(klass);
    }

    @Override
    public void run(RunNotifier notifier) {
        notifier.addListener(new ScreenshotOnFailRunListener());
        notifier.fireTestRunStarted(getDescription());
        super.run(notifier);
    }
}