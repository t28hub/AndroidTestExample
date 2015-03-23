package com.t28.android.example.activity;

import android.app.Activity;
import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.test.ActivityInstrumentationTestCase2;
import android.test.suitebuilder.annotation.LargeTest;

import com.t28.android.example.test.AssetReader;
import com.t28.android.example.volley.BasicRequestMatcher;
import com.t28.android.example.volley.MethodMatcher;
import com.t28.android.example.volley.MockRequestQueue;
import com.t28.android.example.volley.MockRequestQueueFactory;
import com.t28.android.example.volley.NetworkDispatcher;
import com.t28.android.example.volley.NetworkResponseBuilder;
import com.t28.android.example.volley.StatusCode;
import com.t28.android.example.volley.VolleyHolder;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class MainActivityTest extends ActivityInstrumentationTestCase2<MainActivity> {
    private AssetReader mAssetReader;
    private MockRequestQueue mRequestQueue;
    private Activity mActivity;

    public MainActivityTest() {
        super(MainActivity.class);
    }

    @BeforeClass
    public static void setUpBeforeClass() {
        VolleyHolder.injectRequestQueueFactory(new MockRequestQueueFactory());
    }

    @Before
    public void setUp() throws Exception {
        super.setUp();
        injectInstrumentation(InstrumentationRegistry.getInstrumentation());

        final Context context = InstrumentationRegistry.getContext();
        mAssetReader = new AssetReader(context.getAssets());
        mRequestQueue = (MockRequestQueue) VolleyHolder.get().getRequestQueue(context);
        mRequestQueue.pause();

        mActivity = getActivity();
    }

    @After
    public void tearDown() throws Exception {
        mRequestQueue.clean();
        super.tearDown();
    }

    @Test
    public void test_activityShouldNotNull() {
    }

    @Test
    public void entryListFragment_shouldShowSuccessViewWhenResponseHasNoEntry() throws IOException {
        final NetworkDispatcher dispatcher = mRequestQueue.getNetworkDispatcher();
        dispatcher.append(
                new BasicRequestMatcher.Builder()
                        .setMethodMatcher(MethodMatcher.GET)
                        .setUrlPattern("^https://ajax.googleapis.com/ajax/services/feed/load.+")
                        .build(),
                new NetworkResponseBuilder()
                        .setStatusCode(StatusCode.OK)
                        .addHeader("Content-Type", "application/json")
                        .setBody(mAssetReader.read("feed_load_success_0.json"))
                        .build()
        );
        mRequestQueue.resume();

        try {
            Thread.sleep(1500);
        } catch (InterruptedException e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void entryListFragment_shouldShowSuccessViewWhenResponseHasEntry() throws IOException {
        final NetworkDispatcher dispatcher = mRequestQueue.getNetworkDispatcher();
        dispatcher.append(
                new BasicRequestMatcher.Builder()
                        .setMethodMatcher(MethodMatcher.GET)
                        .setUrlPattern("^https://ajax.googleapis.com/ajax/services/feed/load.+")
                        .build(),
                new NetworkResponseBuilder()
                        .setStatusCode(StatusCode.OK)
                        .addHeader("Content-Type", "application/json")
                        .setBody(mAssetReader.read("feed_load_success_1.json"))
                        .build()
        );
        mRequestQueue.resume();

        try {
            Thread.sleep(1500);
        } catch (InterruptedException e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void entryListFragment_shouldShowSuccessViewWhenResponseHasEntries() throws IOException {
        final NetworkDispatcher dispatcher = mRequestQueue.getNetworkDispatcher();
        dispatcher.append(
                new BasicRequestMatcher.Builder()
                        .setMethodMatcher(MethodMatcher.GET)
                        .setUrlPattern("^https://ajax.googleapis.com/ajax/services/feed/load.+")
                        .build(),
                new NetworkResponseBuilder()
                        .setStatusCode(StatusCode.OK)
                        .addHeader("Content-Type", "application/json")
                        .setBody(mAssetReader.read("feed_load_success_10.json"))
                        .build()
        );
        mRequestQueue.resume();

        try {
            Thread.sleep(1500);
        } catch (InterruptedException e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void entryListFragment_shouldShowFailureViewWhenResponseReturnsNotLoaded() throws IOException {
        final NetworkDispatcher dispatcher = mRequestQueue.getNetworkDispatcher();
        dispatcher.append(
                new BasicRequestMatcher.Builder()
                        .setMethodMatcher(MethodMatcher.GET)
                        .setUrlPattern("^https://ajax.googleapis.com/ajax/services/feed/load.+")
                        .build(),
                new NetworkResponseBuilder()
                        .setStatusCode(StatusCode.OK)
                        .addHeader("Content-Type", "application/json")
                        .setBody(mAssetReader.read("feed_load_failure_could_not_be_loaded.json"))
                        .build()
        );
        mRequestQueue.resume();

        try {
            Thread.sleep(1500);
        } catch (InterruptedException e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void entryListFragment_shouldShowFailureViewWhenResponseReturnsInvalidVersion() throws IOException {
        final NetworkDispatcher dispatcher = mRequestQueue.getNetworkDispatcher();
        dispatcher.append(
                new BasicRequestMatcher.Builder()
                        .setMethodMatcher(MethodMatcher.GET)
                        .setUrlPattern("^https://ajax.googleapis.com/ajax/services/feed/load.+")
                        .build(),
                new NetworkResponseBuilder()
                        .setStatusCode(StatusCode.OK)
                        .addHeader("Content-Type", "application/json")
                        .setBody(mAssetReader.read("feed_load_failure_invalid_version.json"))
                        .build()
        );
        mRequestQueue.resume();

        try {
            Thread.sleep(1500);
        } catch (InterruptedException e) {
            fail(e.getMessage());
        }
    }
}
