package com.t28.android.example.api.request;

import com.android.volley.Cache;
import com.android.volley.NetworkResponse;
import com.android.volley.Response;
import com.t28.android.example.api.parser.FeedParser;
import com.t28.android.example.api.parser.Parser;
import com.t28.android.example.data.model.Feed;

public class FeedRequest extends AbsRequest<Feed> {
    private static final String BASE_URL = "https://ajax.googleapis.com/ajax/services/feed/load";

    public FeedRequest(String url, Response.Listener<Feed> listener, Response.ErrorListener errorListener) {
        super(Method.GET, url, listener, errorListener);
    }

    @Override
    protected Parser<Feed> createParser() {
        return new FeedParser();
    }

    @Override
    protected Cache.Entry createCache(NetworkResponse response) {
        return null;
    }
}
