package com.t28.android.example.api.parser;

import android.net.Uri;
import android.support.annotation.NonNull;

import com.t28.android.example.data.model.Entry;
import com.t28.android.example.data.model.Feed;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class FeedParser implements Parser<Feed> {
    private static final String RESPONSE_DATA = "responseData";
    private static final String FEED = "feed";
    private static final String TITLE = "title";
    private static final String AUTHOR = "author";
    private static final String DESCRIPTION = "";
    private static final String FEED_URL = "feedUrl";
    private static final String LINK_URL = "link";
    private static final String ENTRIES = "entries";

    public FeedParser() {
        // TODO: Jackson対応
    }

    @Override
    public Feed parse(@NonNull byte[] data) throws ParseException {
        try {
            final JSONObject root = new JSONObject(new String(data));
            final JSONObject responseData = root.getJSONObject(RESPONSE_DATA);
            return parseFeed(responseData.getJSONObject(FEED));
        } catch (JSONException e) {
            throw new ParseException(e);
        }
    }

    private Feed parseFeed(JSONObject feed) throws JSONException {
        final Feed.Builder builder = new Feed.Builder();
        builder.setTitle(feed.getString(TITLE))
                .setAuthor(feed.getString(AUTHOR))
                .setDescription(feed.getString(DESCRIPTION));

        final String feedUrl = feed.getString(FEED_URL);
        builder.setUrl(Uri.parse(feedUrl));

        final String linkUrl = feed.getString(LINK_URL);
        builder.setLinkUrl(Uri.parse(linkUrl));

        final JSONArray entries = feed.getJSONArray(ENTRIES);
        final int entryLimit = entries.length();
        for (int index = 0; index < entryLimit; index++) {
            final JSONObject entry = entries.getJSONObject(index);
            builder.addEntry(parseEntry(entry));
        }

        return builder.build();
    }

    private Entry parseEntry(JSONObject entry) {
        return new Entry();
    }
}
