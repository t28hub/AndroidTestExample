package com.t28.android.example.test;

import android.support.test.espresso.IdlingResource;
import android.text.TextUtils;

import java.util.concurrent.atomic.AtomicInteger;

public class CountingIdlingResource implements IdlingResource {
    private final String mName;
    private final AtomicInteger mCounter;

    private ResourceCallback mResourceCallback;

    public CountingIdlingResource(String name) {
        if (TextUtils.isEmpty(name)) {
            throw new IllegalArgumentException("'name' must not be empty");
        }
        mName = name;
        mCounter = new AtomicInteger();
    }

    @Override
    public String getName() {
        return mName;
    }

    @Override
    public boolean isIdleNow() {
        return mCounter.get() == 0;
    }

    @Override
    public void registerIdleTransitionCallback(ResourceCallback resourceCallback) {
        mResourceCallback = resourceCallback;
    }

    public void increment() {
        mCounter.incrementAndGet();
    }

    public void decrement() {
        final int count = mCounter.decrementAndGet();
        if (count > 0) {
            return;
        }
        synchronized (this) {
            if (mResourceCallback != null) {
                mResourceCallback.onTransitionToIdle();
            }
        }
    }
}
