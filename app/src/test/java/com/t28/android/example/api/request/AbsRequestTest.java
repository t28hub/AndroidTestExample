package com.t28.android.example.api.request;

import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.t28.android.example.api.parser.ParseException;
import com.t28.android.example.api.parser.Parser;
import com.t28.android.example.data.model.Model;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static com.t28.android.example.test.assertion.Assertions.assertThat;

@Config(emulateSdk = Build.VERSION_CODES.JELLY_BEAN_MR2, manifest = "src/test/AndroidManifest.xml")
@RunWith(RobolectricTestRunner.class)
public class AbsRequestTest {
    private static final String URL = "http://api.example.com/v1/user";

    @Test
    public void constructor_shouldReturnInstance() {
        // exercise
        final AbsRequest request = new UserRequest(Request.Method.GET, URL, null, null);

        // verify
        assertThat(request).isNotNull();
    }

    @Test
    public void buildUrl_shouldReturnUrlWithParameters() {
        // setup
        final Map<String, String> params = new HashMap<>();
        params.put("query", "android");

        // exercise
        final String urlString = AbsRequest.buildUrl(URL, params);

        // verify
        assertThat(Uri.parse(urlString))
                .hasScheme("http")
                .hasHost("api.example.com")
                .hasPath("/v1/user")
                .hasQueryParameter("query", "android");
    }

    @Test
    public void buildUrl_shouldReturnUrlWithEmptyParameter() {
        // exercise
        final String urlString = AbsRequest.buildUrl(URL, Collections.<String, String>emptyMap());

        // verify
        assertThat(Uri.parse(urlString))
                .hasScheme("http")
                .hasHost("api.example.com")
                .hasPath("/v1/user");
    }

    @Test
    public void buildUrl_shouldNotAppendQueryParameterWhenNameIsEmpty() {
        // setup
        final Map<String, String> params = new HashMap<>();
        params.put("query", "android");
        params.put("", "empty");

        // exercise
        final String urlString = AbsRequest.buildUrl(URL, params);

        // verify
        assertThat(Uri.parse(urlString))
                .hasScheme("http")
                .hasHost("api.example.com")
                .hasPath("/v1/user")
                .hasQueryParameter("query", "android")
                .hasNoQueryParameter("");
    }

    @Test
    public void buildUrl_shouldNotAppendQueryParameterWhenParameterIsEmpty() {
        // setup
        final Map<String, String> params = new HashMap<>();
        params.put("query", "android");
        params.put("empty", "");

        // exercise
        final String urlString = AbsRequest.buildUrl(URL, params);

        // verify
        assertThat(Uri.parse(urlString))
                .hasScheme("http")
                .hasHost("api.example.com")
                .hasPath("/v1/user")
                .hasQueryParameter("query", "android")
                .hasNoQueryParameter("empty");
    }

    @Test
    public void parseNetworkResponse_shouldReturnErrorResponseWhenResponseBodyIsNull() {
        // setup
        final NetworkResponse networkResponse = new NetworkResponse(null);
        final AbsRequest<User> request = new UserRequest(Request.Method.GET, URL, null, null);

        // exercise
        final Response<User> response = request.parseNetworkResponse(networkResponse);
    }

    @Test
    public void parseNetworkResponse_shouldReturnErrorResponseWhenResponseBodyIsEmpty() {
        // setup
        final NetworkResponse networkResponse = new NetworkResponse("".getBytes());
        final AbsRequest<User> request = new UserRequest(Request.Method.GET, URL, null, null);

        // exercise
        final Response<User> response = request.parseNetworkResponse(networkResponse);
    }

    public static class User implements Model {
        private final String mName;

        public User(String name) {
            mName = name;
        }

        @Override
        public boolean isValid() {
            return !TextUtils.isEmpty(mName);
        }

        public String getName() {
            return mName;
        }
    }

    public static class UserParser implements Parser<User> {

        public UserParser() {
        }

        @Override
        public User parse(@NonNull byte[] data) throws ParseException {
            final String name = new String(data);
            return new User(name);
        }
    }

    public static class UserRequest extends AbsRequest<User> {

        public UserRequest(int method, String url, Response.Listener<User> listener,
                           Response.ErrorListener errorListener) {
            super(method, url, listener, errorListener);
        }

        @Override
        protected Parser<User> createParser() {
            return new UserParser();
        }
    }
}
