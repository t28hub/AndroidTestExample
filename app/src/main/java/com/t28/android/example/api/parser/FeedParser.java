package com.t28.android.example.api.parser;

import android.net.Uri;
import android.support.annotation.NonNull;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.t28.android.example.data.model.Entry;
import com.t28.android.example.data.model.Feed;
import com.t28.android.example.util.IoUtils;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

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

    private final JsonFactory mFactory;

    private JsonParser mParser;

    public FeedParser() {
        this(new JsonFactory());
    }

    public FeedParser(JsonFactory factory) {
        if (factory == null) {
            throw new NullPointerException("factory == null");
        }
        mFactory = factory;
    }

    @Override
    public Feed parse(@NonNull byte[] data) throws ParseException {
        try {
            mParser = mFactory.createParser(data);
            if (mParser.nextToken() != JsonToken.START_OBJECT) {
                final JsonToken token = mParser.getCurrentToken();
                throw new ParseException("Unexpected JSON token:" + token);
            }
            return parseRoot();
        } catch (IOException e) {
            throw new ParseException(e);
        } finally {
            IoUtils.close(mParser);
        }
    }

    private Feed parseRoot() throws ParseException, IOException {
        while (mParser.nextToken() != JsonToken.END_OBJECT) {
            final JsonToken token = mParser.getCurrentToken();
            final String name = mParser.getCurrentName();
            if (token != JsonToken.START_OBJECT || !RESPONSE_DATA.equals(name)) {
                mParser.skipChildren();
                continue;
            }
            return parseResponseData();
        }
        return null;
    }

    private Feed parseResponseData() throws IOException, ParseException {
        while (mParser.nextToken() != JsonToken.END_OBJECT) {
            final JsonToken token = mParser.getCurrentToken();
            final String name = mParser.getCurrentName();
            if (token != JsonToken.START_OBJECT || !FEED.equals(name)) {
                mParser.skipChildren();
                continue;
            }
            return parseFeed();
        }
        return null;
    }

    private Feed parseFeed() throws IOException, ParseException {
        final Feed.Builder builder = new Feed.Builder();
        while (mParser.nextToken() != JsonToken.END_OBJECT) {
            final JsonToken token = mParser.getCurrentToken();
            final String name = mParser.getCurrentName();
            if (token == JsonToken.START_ARRAY && ENTRIES.equals(name)) {
                final List<Entry> entries = parseEntries();
                builder.addEntries(entries);
                continue;
            }

            if (token != JsonToken.VALUE_STRING) {
                mParser.skipChildren();
                continue;
            }

            if (TITLE.equals(name)) {
                builder.setTitle(mParser.getText());
                continue;
            }
            if (AUTHOR.equals(name)) {
                builder.setAuthor(mParser.getText());
                continue;
            }
            if (DESCRIPTION.equals(name)) {
                builder.setDescription(mParser.getText());
                continue;
            }
            if (FEED_URL.equals(name)) {
                final Uri feedUrl = Uri.parse(mParser.getText());
                builder.setUrl(feedUrl);
                continue;
            }
            if (LINK_URL.equals(name)) {
                final Uri linkUrl = Uri.parse(mParser.getText());
                builder.setLinkUrl(linkUrl);
                continue;
            }
            mParser.skipChildren();
        }
        return builder.build();
    }

    private List<Entry> parseEntries() throws IOException, ParseException {
        final List<Entry> entries = new ArrayList<>();
        while (mParser.nextToken() != JsonToken.END_ARRAY) {
            final JsonToken token = mParser.getCurrentToken();
            if (token != JsonToken.START_OBJECT) {
                mParser.skipChildren();
                continue;
            }

            final Entry entry = parseEntry();
            entries.add(entry);
        }
        return entries;
    }

    private Entry parseEntry() throws IOException, ParseException {
        final Entry.Builder builder = new Entry.Builder();
        while (mParser.nextToken() != JsonToken.END_OBJECT) {
            final JsonToken token = mParser.getCurrentToken();
            final String name = mParser.getCurrentName();
            if (token == JsonToken.START_ARRAY && ENTRY_CATEGORIES.equals(name)) {
                final Set<String> categories = parseCategories();
                builder.addCategories(categories);
                continue;
            }

            if (token != JsonToken.VALUE_STRING) {
                mParser.skipChildren();
                continue;
            }

            if (ENTRY_TITLE.equals(name)) {
                builder.setTitle(mParser.getText());
                continue;
            }
            if (ENTRY_CONTENT.equals(name)) {
                builder.setContent(mParser.getText());
                continue;
            }
            if (ENTRY_CONTENT_SNIPPET.equals(name)) {
                builder.setContentSnippet(mParser.getText());
                continue;
            }
            if (ENTRY_URL.equals(name)) {
                builder.setUrl(Uri.parse(mParser.getText()));
                continue;
            }
            if (ENTRY_PUBLISHED_DATE.equals(name)) {
                final String publishedDate = mParser.getText();
                builder.setPublishedDate(parsePublishedDate(publishedDate));
                continue;
            }
            mParser.skipChildren();
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

    private Set<String> parseCategories() throws IOException {
        final Set<String> categories = new HashSet<>();
        while (mParser.nextToken() != JsonToken.END_ARRAY) {
            final JsonToken token = mParser.getCurrentToken();
            if (token != JsonToken.VALUE_STRING) {
                mParser.skipChildren();
                continue;
            }

            categories.add(mParser.getText());
        }
        return categories;
    }
}
