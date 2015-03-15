package com.t28.android.example.api.request;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.t28.android.example.data.model.Feed;

public class FeedRequest extends Request<Feed> {
    private static final String BASE_URL = "https://ajax.googleapis.com/ajax/services/feed/load";

    public FeedRequest(String url, Response.ErrorListener listener) {
        super(Method.GET, url, listener);
    }

    @Override
    protected Response<Feed> parseNetworkResponse(NetworkResponse response) {
        final byte[] data = response.data;
        if (data == null || data.length == 0) {
            final VolleyError error = new VolleyError("Empty response data");
            return Response.error(error);
        }

        return null;
    }

    @Override
    protected void deliverResponse(Feed response) {

    }
}
