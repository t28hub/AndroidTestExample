package com.t28.android.example.activity;

import android.app.Activity;
import android.content.Context;
import android.content.res.AssetManager;
import android.support.annotation.NonNull;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.test.ActivityInstrumentationTestCase2;
import android.test.suitebuilder.annotation.LargeTest;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.t28.android.example.util.IoUtils;
import com.t28.android.example.volley.MockRequestQueue;
import com.t28.android.example.volley.MockRequestQueueFactory;
import com.t28.android.example.volley.NetworkDispatcher;
import com.t28.android.example.volley.RequestMatcher;
import com.t28.android.example.volley.VolleyHolder;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class MainActivityTest extends ActivityInstrumentationTestCase2<MainActivity> {
    private Context mContext;
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
        mContext = InstrumentationRegistry.getContext();
        mRequestQueue = (MockRequestQueue) VolleyHolder.get().getRequestQueue(mContext);
        mRequestQueue.pause();

        mActivity = getActivity();
    }

    @After
    public void tearDown() throws Exception {
        mRequestQueue.clean();
        super.tearDown();
    }

    @Test
    public void testStartActivity() {
        final AssetManager assetManager = mContext.getAssets();
        BufferedInputStream input = null;
        ByteArrayOutputStream output = null;
        byte[] body = "".getBytes();
        try {
            input = new BufferedInputStream(assetManager.open("feed_load_success_10.json"));
            output = new ByteArrayOutputStream();
            final byte[] buffer = new byte[1024];
            while (input.read(buffer) != -1) {
                output.write(buffer);
            }
            body = output.toByteArray();
        } catch (IOException e) {
            fail(e.getMessage());
        } finally {
            IoUtils.close(input);
            IoUtils.close(output);
        }

        final NetworkDispatcher dispatcher = mRequestQueue.getNetworkDispatcher();
        dispatcher.append(new RequestMatcher() {
            @Override
            public boolean match(@NonNull Request<?> request) {
                return true;
            }
        }, new NetworkResponse(body));
        mRequestQueue.resume();

        try {
            Thread.sleep(1500);
        } catch (InterruptedException e) {
            fail(e.getMessage());
        }
    }
}
