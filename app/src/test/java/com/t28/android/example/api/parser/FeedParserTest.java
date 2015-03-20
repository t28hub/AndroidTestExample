package com.t28.android.example.api.parser;

import android.content.Context;
import android.content.res.AssetManager;
import android.os.Build;

import com.t28.android.example.data.model.Feed;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import static org.assertj.core.api.Assertions.assertThat;

@Config(emulateSdk = Build.VERSION_CODES.JELLY_BEAN_MR2, manifest = "src/test/AndroidManifest.xml")
@RunWith(RobolectricTestRunner.class)
public class FeedParserTest {

    @Test
    public void parse_shouldReturnFeedWhenResponseHasNoEntry() throws IOException, ParseException {
        final Context context = Robolectric.application;
        final AssetManager assetManager = context.getAssets();
        final InputStream input = assetManager.open("feed_load_success_0.json");
        final ByteArrayOutputStream output = new ByteArrayOutputStream();
        final byte[] buffer = new byte[1024];
        while (input.read(buffer) != -1) {
            output.write(buffer);
        }

        final byte[] data = output.toByteArray();
        final FeedParser parser = new FeedParser();
        final Feed feed = parser.parse(data);

        assertThat(feed).isNotNull();
    }

    @Test
    public void parse_shouldReturnFeedWhenResponseHas1Entry() throws IOException, ParseException {
    }

    @Test
    public void parse_shouldReturnFeedWhenResponseHas10Entries() throws IOException, ParseException {
    }

    @Test
    public void parse_shouldReturnFeedWhenResponseHas100Entries() throws IOException, ParseException {
    }
}
