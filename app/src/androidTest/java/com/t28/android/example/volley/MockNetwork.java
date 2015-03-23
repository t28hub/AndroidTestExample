package com.t28.android.example.volley;

import com.android.volley.Network;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.VolleyError;

public class MockNetwork implements Network {
    private final NetworkDispatcher mDispatcher;

    public MockNetwork(NetworkDispatcher dispatcher) {
        if (dispatcher == null) {
            throw new NullPointerException("dispatcher == null");
        }
        mDispatcher = dispatcher;
    }

    @Override
    public NetworkResponse performRequest(Request<?> request) throws VolleyError {
        return mDispatcher.dispatch(request);
    }
}
