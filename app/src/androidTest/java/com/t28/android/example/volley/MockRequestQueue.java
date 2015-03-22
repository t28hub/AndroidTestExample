package com.t28.android.example.volley;

import com.android.volley.Cache;
import com.android.volley.Network;
import com.android.volley.Request;
import com.android.volley.RequestQueue;

import java.util.LinkedList;
import java.util.Queue;

public class MockRequestQueue extends RequestQueue {
    private final Queue<Request<?>> mWaitingRequests;

    private boolean mIsPaused;

    public MockRequestQueue(Cache cache, Network network) {
        super(cache, network);
        mWaitingRequests = new LinkedList<>();
    }

    @Override
    public <T> Request<T> add(Request<T> request) {
        if (mIsPaused) {
            mWaitingRequests.add(request);
            return request;
        }
        return super.add(request);
    }

    public void resume() {
        if (!mIsPaused) {
            return;
        }

        Request request;
        while ((request = mWaitingRequests.poll()) != null) {
            add(request);
        }
        mIsPaused = false;
    }

    public void pause() {
        mIsPaused = true;
    }
}
