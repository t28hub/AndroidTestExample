package com.t28.android.example.api.request;

import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
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
import java.util.concurrent.TimeUnit;

import static com.t28.android.example.test.assertion.Assertions.assertThat;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

/**
 * <code>{@link com.t28.android.example.api.request.AbsRequest}</code>のテスト
 */
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
    public void parseNetworkResponse_shouldReturnSuccessResponseWhenParsingSucceeded() {
        // setup
        final byte[] data = "android".getBytes();
        final NetworkResponse networkResponse = new NetworkResponse(data);
        final AbsRequest<User> request = new UserRequest(Request.Method.GET, URL, null, null);

        // exercise
        final Response<User> response = request.parseNetworkResponse(networkResponse);

        // verify
        assertThat(response)
                .hasResult()
                .hasNoError();
    }

    @Test
    public void parseNetworkResponse_shouldReturnSuccessResponseWithoutCacheEntryWhenTimeToLiveAndSoftTimeToLiveAreZero() {
        // setup
        final byte[] data = "android".getBytes();
        final NetworkResponse networkResponse = new NetworkResponse(data);
        final AbsRequest<User> spiedRequest = spy(new UserRequest(Request.Method.GET, URL, null, null));

        // exercise
        final Response<User> response = spiedRequest.parseNetworkResponse(networkResponse);

        // verify
        assertThat(response)
                .hasNoCacheEntry();
    }

    @Test
    public void parseNetworkResponse_shouldReturnSuccessResponseWithCacheEntryWhenTimeToLiveIsGreaterThanZero() {
        // setup
        final byte[] data = "android".getBytes();
        final NetworkResponse networkResponse = new NetworkResponse(data);
        final AbsRequest<User> spiedRequest = spy(new UserRequest(Request.Method.GET, URL, null, null));
        when(spiedRequest.getTimeToLive()).thenReturn(TimeUnit.MINUTES.toMillis(10));

        // exercise
        final Response<User> response = spiedRequest.parseNetworkResponse(networkResponse);

        // verify
        assertThat(response)
                .hasCacheEntry();
    }

    @Test
    public void parseNetworkResponse_shouldReturnSuccessResponseWithCacheEntryWhenSoftTimeToLiveIsGreaterThanZero() {
        // setup
        final byte[] data = "android".getBytes();
        final NetworkResponse networkResponse = new NetworkResponse(data);
        final AbsRequest<User> spiedRequest = spy(new UserRequest(Request.Method.GET, URL, null, null));
        when(spiedRequest.getSoftTimeToLive()).thenReturn(TimeUnit.MINUTES.toMillis(10));

        // exercise
        final Response<User> response = spiedRequest.parseNetworkResponse(networkResponse);

        // verify
        assertThat(response)
                .hasCacheEntry();
    }

    @Test
    public void parseNetworkResponse_shouldReturnErrorResponseWhenResponseBodyIsNull() {
        // setup
        final NetworkResponse networkResponse = new NetworkResponse(null);
        final AbsRequest<User> request = new UserRequest(Request.Method.GET, URL, null, null);

        // exercise
        final Response<User> response = request.parseNetworkResponse(networkResponse);

        // verify
        assertThat(response)
                .hasNoResult()
                .hasErrorInstanceOf(VolleyError.class);
    }

    @Test
    public void parseNetworkResponse_shouldReturnErrorResponseWhenResponseBodyIsEmpty() {
        // setup
        final byte[] data = "".getBytes();
        final NetworkResponse networkResponse = new NetworkResponse(data);
        final AbsRequest<User> request = new UserRequest(Request.Method.GET, URL, null, null);

        // exercise
        final Response<User> response = request.parseNetworkResponse(networkResponse);

        // verify
        assertThat(response)
                .hasNoResult()
                .hasErrorInstanceOf(VolleyError.class);
    }

    @Test
    public void parseNetworkResponse_shouldReturnErrorResponseWhenParserReturnsNull() throws ParseException {
        // setup
        final byte[] data = "android".getBytes();
        final NetworkResponse networkResponse = new NetworkResponse(data);

        final Parser<User> mockedParser = mock(UserParser.class);
        when(mockedParser.parse(eq(data))).thenReturn(null);

        final AbsRequest<User> spiedRequest = spy(new UserRequest(Request.Method.GET, URL, null, null));
        when(spiedRequest.createParser()).thenReturn(mockedParser);

        // exercise
        final Response<User> response = spiedRequest.parseNetworkResponse(networkResponse);

        // verify
        assertThat(response)
                .hasNoResult()
                .hasErrorInstanceOf(ParseError.class);
    }

    @Test
    public void parseNetworkResponse_shouldReturnErrorResponseWhenParserReturnsInvalidUser() throws ParseException {
        // setup
        final byte[] data = "android".getBytes();
        final NetworkResponse networkResponse = new NetworkResponse(data);

        final User invalidUser = new User("");
        final Parser<User> mockedParser = mock(UserParser.class);
        when(mockedParser.parse(eq(data))).thenReturn(invalidUser);

        final AbsRequest<User> spiedRequest = spy(new UserRequest(Request.Method.GET, URL, null, null));
        when(spiedRequest.createParser()).thenReturn(mockedParser);

        // exercise
        final Response<User> response = spiedRequest.parseNetworkResponse(networkResponse);

        // verify
        assertThat(response)
                .hasNoResult()
                .hasErrorInstanceOf(ParseError.class);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void deliverResponse_shouldCallOnResponseWhenListenerExists() {
        // setup
        final Response.Listener<User> mockedListener = mock(Response.Listener.class);
        final AbsRequest<User> request = new UserRequest(Request.Method.GET, URL, mockedListener, null);
        final User user = new User("android");

        // exercise
        request.deliverResponse(user);

        // verify
        verify(mockedListener).onResponse(eq(user));
    }

    @SuppressWarnings("unchecked")
    @Test
    public void deliverResponse_shouldNotCallOnResponseWhenListenerDoesNotExist() {
        // setup
        final Response.Listener<User> mockedListener = mock(Response.Listener.class);
        final AbsRequest<User> request = new UserRequest(Request.Method.GET, URL, null, null);
        final User user = new User("android");

        // exercise
        request.deliverResponse(user);

        // verify
        verifyZeroInteractions(mockedListener);
    }

    @Test
    public void getTimeToLive_shouldReturnZero() {
        // setup
        final AbsRequest<User> request = new UserRequest(Request.Method.GET, URL, null, null);

        // exercise
        final long timeToLive = request.getTimeToLive();

        // verify
        assertThat(timeToLive)
                .overridingErrorMessage("Expected time to live 0 but was <%d>.", timeToLive)
                .isZero();
    }

    @Test
    public void getSoftTimeToLive_shouldReturnZero() {
        // setup
        final AbsRequest<User> request = new UserRequest(Request.Method.GET, URL, null, null);

        // exercise
        final long softTimeToLive = request.getSoftTimeToLive();

        // verify
        assertThat(softTimeToLive)
                .overridingErrorMessage("Expected soft time to live 0 but was <%d>.", softTimeToLive)
                .isZero();
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
