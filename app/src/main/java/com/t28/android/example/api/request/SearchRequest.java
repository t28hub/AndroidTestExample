package com.t28.android.example.api.request;

import android.text.TextUtils;

import com.android.volley.Cache;
import com.android.volley.NetworkResponse;
import com.android.volley.Response;
import com.t28.android.example.api.parser.Parser;
import com.t28.android.example.data.model.SearchResult;

import java.util.HashMap;
import java.util.Map;

public class SearchRequest extends AbsRequest<SearchResult> {
    private static final String BASE_URL = "https://ajax.googleapis.com/ajax/services/feed/find";

    private SearchRequest(Builder builder) {
        super(Method.GET, buildUrl(BASE_URL, builder.mParams), builder.mListener, builder.mErrorListener);
    }

    @Override
    protected Parser<SearchResult> createParser() {
        return null;
    }

    @Override
    protected Cache.Entry createCache(NetworkResponse response) {
        return null;
    }

    public static final class Builder {
        private static final String PARAM_QUERY = "q";
        private static final String PARAM_VERSION = "v";
        private static final String PARAM_LANGUAGE = "hl";
        private static final String PROTOCOL_VERSION = "1.0";

        private final Map<String, String> mParams;

        private long mTimeToLive;
        private long mSoftTimeToLive;
        private Response.Listener<SearchResult> mListener;
        private Response.ErrorListener mErrorListener;

        public Builder(String keyword) {
            if (TextUtils.isEmpty(keyword)) {
                throw new IllegalArgumentException("'keyword' must not be empty");
            }
            mParams = new HashMap<>();
            mParams.put(PARAM_QUERY, keyword);
            mParams.put(PARAM_VERSION, PROTOCOL_VERSION);
        }

        public Builder setLaunguage(String language) {
            mParams.put(PARAM_LANGUAGE, language);
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

        public Builder setListener(Response.Listener<SearchResult> listener) {
            mListener = listener;
            return this;
        }

        public Builder setErrorListener(Response.ErrorListener errorListener) {
            mErrorListener = errorListener;
            return this;
        }

        public SearchRequest build() {
            return new SearchRequest(this);
        }
    }
}
