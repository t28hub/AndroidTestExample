package com.t28.android.example.api.request;

import android.os.Build;

import com.android.volley.Request;
import com.android.volley.Response;
import com.t28.android.example.api.parser.Parser;
import com.t28.android.example.data.model.Model;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import static org.assertj.core.api.Assertions.assertThat;

@Config(emulateSdk = Build.VERSION_CODES.JELLY_BEAN_MR2, manifest = "src/test/AndroidManifest.xml")
@RunWith(RobolectricTestRunner.class)
public class AbsRequestTest {
    private static final String URL = "http://example.com";

    @Test
    public void constructor_shouldReturnInstance() {
        // exercise
        final AbsRequest request = new TestAbsRequest(Request.Method.GET, URL, null, null);

        // verify
        assertThat(request).isNotNull();
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
