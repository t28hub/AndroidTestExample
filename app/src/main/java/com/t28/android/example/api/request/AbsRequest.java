package com.t28.android.example.api.request;

import com.android.volley.Cache;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.t28.android.example.api.parser.ParseException;
import com.t28.android.example.api.parser.Parser;

public abstract class AbsRequest<T> extends Request<T> {
    private final Response.Listener<T> mListener;

    public AbsRequest(int method, String url, Response.Listener<T> listener, Response.ErrorListener errorListener) {
        super(method, url, errorListener);
        mListener = listener;
    }

    @Override
    protected final Response<T> parseNetworkResponse(NetworkResponse response) {
        final byte[] data = response.data;
        if (data == null || data.length == 0) {
            final VolleyError error = new VolleyError("Empty response data");
            return Response.error(error);
        }

        try {
            final Parser<T> parser = createParser();
            final T parsed = parser.parse(data);
            if (parsed == null) {
                throw new ParseException("Invalid parsed result:" + parsed);
            }
            return Response.success(parsed, createCache(response));
        } catch (ParseException e) {
            final VolleyError error = new ParseError(e);
            return Response.error(error);
        }
    }

    @Override
    protected final void deliverResponse(T response) {
        if (mListener == null) {
            return;
        }
        mListener.onResponse(response);
    }

    protected abstract Parser<T> createParser();

    protected abstract Cache.Entry createCache(NetworkResponse response);
}
