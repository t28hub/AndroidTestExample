package com.t28.android.example.volley;

import com.android.volley.Cache;
import com.android.volley.Request;
import com.android.volley.RequestQueue;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.atomic.AtomicBoolean;

public class MockRequestQueue extends RequestQueue {
    private final AtomicBoolean mIsPaused;
    private final NetworkDispatcher mNetworkDispatcher;
    private final Queue<Request<?>> mWaitingRequests;

    public MockRequestQueue(Cache cache, NetworkDispatcher networkDispatcher) {
        super(cache, new MockNetwork(networkDispatcher));
        mIsPaused = new AtomicBoolean();
        mNetworkDispatcher = networkDispatcher;
        mWaitingRequests = new LinkedList<>();
    }

    @Override
    public <T> Request<T> add(Request<T> request) {
        if (mIsPaused.get()) {
            mWaitingRequests.add(request);
            return request;
        }
        return super.add(request);
    }

    public void resume() {
        if (!mIsPaused.get()) {
            return;
        }
        mIsPaused.set(false);

        Request request;
        while ((request = mWaitingRequests.poll()) != null) {
            add(request);
        }
    }

    public void pause() {
        mIsPaused.set(true);
    }

    public void clean() {
        mNetworkDispatcher.clear();
        mWaitingRequests.clear();
        cancelAll(new AnyRequestFilter());
    }
}
