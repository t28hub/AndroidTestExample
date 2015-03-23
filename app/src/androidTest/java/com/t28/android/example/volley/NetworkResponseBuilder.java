package com.t28.android.example.volley;

import com.android.volley.NetworkResponse;

import org.apache.http.HttpStatus;

import java.util.HashMap;
import java.util.Map;

public class NetworkResponseBuilder {
    private static final byte[] EMPTY_BODY = "".getBytes();

    private final Map<String, String> mHeaders;

    private int mStatusCode = HttpStatus.SC_OK;
    private byte[] mBody = EMPTY_BODY;
    private boolean mNotModified;
    private long mNetworkTimeMs;

    public NetworkResponseBuilder() {
        mHeaders = new HashMap<>();
    }

    public NetworkResponseBuilder setStatusCode(int statusCode) {
        mStatusCode = statusCode;
        return this;
    }

    public NetworkResponseBuilder addHeader(String name, String value) {
        mHeaders.put(name, value);
        return this;
    }

    public NetworkResponseBuilder addHeaders(Map<String, String> headers) {
        if (headers != null) {
            mHeaders.putAll(headers);
        }
        return this;
    }

    public NetworkResponseBuilder setBody(byte[] body) {
        if (body == null) {
            mBody = EMPTY_BODY;
        } else {
            mBody = body;
        }
        return this;
    }

    public NetworkResponseBuilder setNotModified(boolean notModified) {
        mNotModified = notModified;
        return this;
    }

    public NetworkResponseBuilder setNetworkTimeMs(long timeMs) {
        mNetworkTimeMs = timeMs;
        return this;
    }

    public NetworkResponse build() {
        return new NetworkResponse(mStatusCode, mBody.clone(), new HashMap<>(mHeaders), mNotModified, mNetworkTimeMs);
    }
}
