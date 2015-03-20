package com.t28.android.example.api.parser;

import android.net.Uri;
import android.support.annotation.NonNull;

import com.t28.android.example.data.model.Entry;
import com.t28.android.example.data.model.Feed;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class FeedParser implements Parser<Feed> {
    private static final String RESPONSE_DATA = "responseData";
    private static final String FEED = "feed";
    private static final String TITLE = "title";
    private static final String AUTHOR = "author";
    private static final String DESCRIPTION = "description";
    private static final String FEED_URL = "feedUrl";
    private static final String LINK_URL = "link";
    private static final String ENTRIES = "entries";
    private static final String ENTRY_TITLE = "title";
    private static final String ENTRY_CONTENT = "content";
    private static final String ENTRY_CONTENT_SNIPPET = "contentSnippet";
    private static final String ENTRY_URL = "link";
    private static final String ENTRY_PUBLISHED_DATE = "publishedDate";
    private static final String ENTRY_CATEGORIES = "categories";
    private static final String FORMAT_PUBLISHED_DATE = "EEE, d MMM yyyy HH:mm:ss Z";

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

    private Feed parseFeed(JSONObject feed) throws JSONException, ParseException {
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

    private Entry parseEntry(JSONObject entry) throws JSONException, ParseException {
        final Entry.Builder builder = new Entry.Builder();
        builder.setTitle(entry.getString(ENTRY_TITLE))
                .setContent(entry.getString(ENTRY_CONTENT))
                .setContentSnippet(entry.getString(ENTRY_CONTENT_SNIPPET));

        final String url = entry.getString(ENTRY_URL);
        builder.setUrl(Uri.parse(url));

        final String publishedDate = entry.getString(ENTRY_PUBLISHED_DATE);
        builder.setPublishedDate(parsePublishedDate(publishedDate));

        final JSONArray categories = entry.getJSONArray(ENTRY_CATEGORIES);
        final int categoryLimit = categories.length();
        for (int index = 0; index < categoryLimit; index++) {
            final String category = categories.getString(index);
            builder.addCategory(category);
        }

        return builder.build();
    }

    private Date parsePublishedDate(String publishedDate) throws ParseException {
        final DateFormat dateFormat = new SimpleDateFormat(FORMAT_PUBLISHED_DATE, Locale.ENGLISH);
        dateFormat.setLenient(false);
        try {
            return dateFormat.parse(publishedDate);
        } catch (java.text.ParseException e) {
            throw new ParseException(e);
        }
    }
}
