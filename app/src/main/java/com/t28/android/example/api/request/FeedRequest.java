package com.t28.android.example.api.request;

import android.text.TextUtils;

import com.android.volley.Cache;
import com.android.volley.NetworkResponse;
import com.android.volley.Response;
import com.t28.android.example.api.parser.FeedParser;
import com.t28.android.example.api.parser.Parser;
import com.t28.android.example.data.model.Feed;

import java.util.HashMap;
import java.util.Map;

public class FeedRequest extends AbsRequest<Feed> {
    private static final long NO_TIME_TO_LIVE = 0;
    private static final String BASE_URL = "https://ajax.googleapis.com/ajax/services/feed/load";

    private final long mTimeToLive;
    private final long mSoftTimeToLive;

    private FeedRequest(Builder builder) {
        super(Method.GET, buildUrl(BASE_URL, builder.mParams), builder.mListener, builder.mErrorListener);
        mTimeToLive = builder.mTimeToLive;
        mSoftTimeToLive = builder.mSoftTimeToLive;
    }

    @Override
    protected Parser<Feed> createParser() {
        return new FeedParser();
    }

    @Override
    protected Cache.Entry createCache(NetworkResponse response) {
        if (mTimeToLive == NO_TIME_TO_LIVE && mSoftTimeToLive == NO_TIME_TO_LIVE) {
            return null;
        }

        final Cache.Entry entry = new Cache.Entry();
        entry.data = response.data;
        entry.responseHeaders = response.headers;
        entry.ttl = mTimeToLive;
        entry.softTtl = mSoftTimeToLive;
        return entry;
    }

    public static final class Builder {
        private static final String PARAM_QUERY = "q";
        private static final String PARAM_VERSION = "v";
        private static final String PARAM_LANGUAGE = "hl";
        private static final String PARAM_NUMBER = "num";
        private static final String PROTOCOL_VERSION = "1.0";

        private final Map<String, String> mParams;

        private long mTimeToLive;
        private long mSoftTimeToLive;
        private Response.Listener<Feed> mListener;
        private Response.ErrorListener mErrorListener;

        public Builder(String feedUrl) {
            if (TextUtils.isEmpty(feedUrl)) {
                throw new IllegalArgumentException("'feedUrl' must not be empty");
            }
            mParams = new HashMap<>();
            mParams.put(PARAM_QUERY, feedUrl);
            mParams.put(PARAM_VERSION, PROTOCOL_VERSION);
        }

        public Builder setHostLanguage(String language) {
            mParams.put(PARAM_LANGUAGE, language);
            return this;
        }

        public Builder setNumber(int number) {
            mParams.put(PARAM_NUMBER, String.valueOf(number));
            return this;
        }

        public Builder setTimeToLive(long timeMs) {
            mTimeToLive = timeMs;
            return this;
        }

        public Builder setSoftTimeToLive(long timeMs) {
            mSoftTimeToLive = timeMs;
            return this;
        }

        public Builder setListener(Response.Listener<Feed> listener) {
            mListener = listener;
            return this;
        }

        public Builder setErrorListener(Response.ErrorListener errorListener) {
            mErrorListener = errorListener;
            return this;
        }

        public FeedRequest build() {
            return new FeedRequest(this);
        }
    }
}
