package com.t28.android.example.api.parser;

import android.content.Context;
import android.os.Build;

import com.t28.android.example.data.model.Feed;
import com.t28.android.example.test.AssetReader;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

@Config(emulateSdk = Build.VERSION_CODES.JELLY_BEAN_MR2, manifest = "src/test/AndroidManifest.xml")
@RunWith(RobolectricTestRunner.class)
public class FeedParserTest {

    private AssetReader mAssetReader;

    @Before
    public void setUp() {
        final Context context = Robolectric.application;
        mAssetReader = new AssetReader(context.getAssets());
    }

    @Test
    public void parse_shouldReturnFeedWhenResponseHasNoEntry() throws IOException, ParseException {
        // setup
        final byte[] data = mAssetReader.read("feed_load_success_0.json");
        final FeedParser parser = new FeedParser();

        // exercise
        final Feed feed = parser.parse(data);

        // verify
        assertThat(feed).isNotNull();
    }

    @Test
    public void parse_shouldReturnFeedWhenResponseHas1Entry() throws IOException, ParseException {
        // setup
        final byte[] data = mAssetReader.read("feed_load_success_1.json");
        final FeedParser parser = new FeedParser();

        // exercise
        final Feed feed = parser.parse(data);

        // verify
        assertThat(feed).isNotNull();
    }

    @Test
    public void parse_shouldReturnFeedWhenResponseHas10Entries() throws IOException, ParseException {
        // setup
        final byte[] data = mAssetReader.read("feed_load_success_10.json");
        final FeedParser parser = new FeedParser();

        // exercise
        final Feed feed = parser.parse(data);

        // verify
        assertThat(feed).isNotNull();
    }

    @Test
    public void parse_shouldReturnFeedWhenResponseHas100Entries() throws IOException, ParseException {
        // setup
        final byte[] data = mAssetReader.read("feed_load_success_100.json");
        final FeedParser parser = new FeedParser();

        // exercise
        final Feed feed = parser.parse(data);

        // verify
        assertThat(feed).isNotNull();
    }

    @Test(expected = ParseException.class)
    public void parse_shouldThrowExceptionWhenPublishedDateIsInvalid() throws IOException, ParseException {
        // setup
        final byte[] data = mAssetReader.read("feed_load_failure_invalid_published_date.json");
        final FeedParser parser = new FeedParser();

        // exercise
        parser.parse(data);
    }
}
