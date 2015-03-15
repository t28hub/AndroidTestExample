package com.t28.android.example.api.request;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.t28.android.example.data.model.Feed;

public class FeedRequest extends Request<Feed> {
    private static final String BASE_URL = "https://ajax.googleapis.com/ajax/services/feed/load";

    public FeedRequest(String url, Response.ErrorListener listener) {
        super(Method.GET, url, listener);
    }

    @Override
    protected Response<Feed> parseNetworkResponse(NetworkResponse response) {
        return null;
    }

    @Override
    protected void deliverResponse(Feed response) {

    }
}
