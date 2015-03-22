package com.t28.android.example.api.request;

import android.net.Uri;
import android.os.Build;

import com.android.volley.Request;
import com.android.volley.Response;
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
        final AbsRequest request = new TestAbsRequest(Request.Method.GET, URL, null, null);

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

    public static class TestModel implements Model {

        @Override
        public boolean isValid() {
            return true;
        }
    }

    public static class TestAbsRequest extends AbsRequest<TestModel> {

        public TestAbsRequest(int method, String url,Response.Listener<TestModel> listener,
                              Response.ErrorListener errorListener) {
            super(method, url, listener, errorListener);
        }

        @Override
        protected Parser<TestModel> createParser() {
            return null;
        }
    }
}
