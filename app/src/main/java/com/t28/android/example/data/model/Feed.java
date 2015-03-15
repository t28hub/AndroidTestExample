package com.t28.android.example.data.model;

import android.net.Uri;

import java.util.ArrayList;
import java.util.List;

public class Feed {
    private final String mTitle;
    private final String mAuthor;
    private final String mDescription;
    private final Uri mUrl;
    private final Uri mLinkUrl;
    private final List<Entry> mEntries;

    private Feed(Builder builder) {
        mTitle = builder.mTitle;
        mAuthor = builder.mAuthor;
        mDescription = builder.mDescription;
        if (builder.mUrl == null) {
            mUrl = Uri.EMPTY;
        } else {
            mUrl = builder.mUrl.buildUpon().build();
        }
        if (builder.mLinkUrl == null) {
            mLinkUrl = Uri.EMPTY;
        } else {
            mLinkUrl = builder.mLinkUrl.buildUpon().build();
        }
        mEntries = new ArrayList<>(builder.mEntries);
    }

    public String getTitle() {
        return mTitle;
    }

    public String getAuthor() {
        return mAuthor;
    }

    public String getDescription() {
        return mDescription;
    }

    public Uri getUrl() {
        return mUrl;
    }

    public Uri getLinkUrl() {
        return mLinkUrl;
    }

    public List<Entry> getEntries() {
        return new ArrayList<>(mEntries);
    }

    public static final class Builder {
        private final List<Entry> mEntries;

        private String mTitle;
        private String mAuthor;
        private String mDescription;
        private Uri mUrl;
        private Uri mLinkUrl;

        public Builder() {
            mEntries = new ArrayList<>();
        }

        public Builder setTitle(String title) {
            mTitle = title;
            return this;
        }

        public Builder setAuthor(String author) {
            mAuthor = author;
            return this;
        }

        public Builder setDescription(String description) {
            mDescription = description;
            return this;
        }

        public Builder setUrl(Uri url) {
            mUrl = url;
            return this;
        }

        public Builder setLinkUrl(Uri linkUrl) {
            mLinkUrl = linkUrl;
            return this;
        }

        public Builder addEntry(Entry entry) {
            mEntries.add(entry);
            return this;
        }

        public Feed build() {
            return new Feed(this);
        }
    }
}
