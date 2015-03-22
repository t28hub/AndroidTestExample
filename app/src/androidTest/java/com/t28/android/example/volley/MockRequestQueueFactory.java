package com.t28.android.example.volley;

import android.content.Context;
import android.support.annotation.NonNull;

import com.android.volley.Cache;
import com.android.volley.Network;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.NoCache;

public class MockRequestQueueFactory implements RequestQueueFactory {

    public MockRequestQueueFactory() {
    }

    @NonNull
    @Override
    public RequestQueue create(Context context) {
        final Cache cache = new NoCache();
        final Network network = new BasicNetwork(new HurlStack());
        return new MockRequestQueue(cache, network);
    }
}
