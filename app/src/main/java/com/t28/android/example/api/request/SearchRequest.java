package com.t28.android.example.api.request;

import com.android.volley.Cache;
import com.android.volley.NetworkResponse;
import com.android.volley.Response;
import com.t28.android.example.api.parser.Parser;
import com.t28.android.example.data.model.SearchResult;

public class SearchRequest extends AbsRequest<SearchResult> {
    public SearchRequest(int method, String url, Response.Listener<SearchResult> listener, Response.ErrorListener errorListener) {
        super(method, url, listener, errorListener);
    }

    @Override
    protected Parser<SearchResult> createParser() {
        return null;
    }

    @Override
    protected Cache.Entry createCache(NetworkResponse response) {
        return null;
    }
}
