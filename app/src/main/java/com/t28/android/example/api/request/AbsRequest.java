package com.t28.android.example.api.request;

import android.net.Uri;
import android.text.TextUtils;

import com.android.volley.Cache;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.t28.android.example.api.parser.ParseException;
import com.t28.android.example.api.parser.Parser;
import com.t28.android.example.data.model.Model;

import java.util.Map;
import java.util.Set;

public abstract class AbsRequest<T extends Model> extends Request<T> {
    private final Response.Listener<T> mListener;

    public AbsRequest(int method, String url, Response.Listener<T> listener, Response.ErrorListener errorListener) {
        super(method, url, errorListener);
        mListener = listener;
    }

    protected static String buildUrl(String baseUrl, Map<String, String> params) {
        final Uri.Builder builder = Uri.parse(baseUrl).buildUpon();
        final Set<Map.Entry<String, String>> entries = params.entrySet();
        for (Map.Entry<String, String> entry : entries) {
            final String value = entry.getValue();
            if (TextUtils.isEmpty(value)) {
                continue;
            }
            builder.appendQueryParameter(entry.getKey(), value);
        }
        return builder.build().toString();
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
            if (parsed == null || !parsed.isValid()) {
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
