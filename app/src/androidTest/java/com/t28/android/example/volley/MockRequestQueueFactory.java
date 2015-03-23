package com.t28.android.example.volley;

import android.content.Context;
import android.support.annotation.NonNull;

import com.android.volley.Cache;
import com.android.volley.RequestQueue;

public class MockRequestQueueFactory implements RequestQueueFactory {

    public MockRequestQueueFactory() {
    }

    @NonNull
    @Override
    public RequestQueue create(Context context) {
        final Cache cache = new MapCache();
        final NetworkDispatcher dispatcher = new NetworkDispatcher();
        return new MockRequestQueue(cache, dispatcher);
    }
}
