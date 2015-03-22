package com.t28.android.example.api.parser;

import android.os.Build;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import static org.assertj.core.api.Assertions.assertThat;

@Config(emulateSdk = Build.VERSION_CODES.JELLY_BEAN_MR2, manifest = "src/test/AndroidManifest.xml")
@RunWith(RobolectricTestRunner.class)
public class ParseExceptionTest {
    @Test
    public void constructor_shouldReturnInstanceWithMessage() {
        // setup
        final String message = "Failed to parsing response";

        // exercise
        final ParseException exception = new ParseException(message);

        // verify
        assertThat(exception)
                .hasMessage(message)
                .hasNoCause();
    }

    @Test
    public void constructor_shouldReturnInstanceWithThrowable() {
        // setup
        final Throwable throwable = new Exception("Failed to parsing response");

        // exercise
        final ParseException exception = new ParseException(throwable);

        // verify
        assertThat(exception)
                .hasMessage(throwable.toString())
                .hasCauseInstanceOf(Exception.class);
    }
}
