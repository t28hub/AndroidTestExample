package com.t28.android.example.data.model;

import android.text.TextUtils;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

public class Entry implements Model {
    private static final long NO_TIME = 0L;

    private final String mTitle;
    private final String mContent;
    private final String mContentSnippet;
    private final Date mPublishedDate;
    private final Set<String> mCategories;

    private Entry(Builder builder) {
        mTitle = builder.mTitle;
        mContent = builder.mContent;
        mContentSnippet = builder.mContentSnippet;
        mPublishedDate = new Date(builder.mPublishedDate.getTime());
        mCategories = new HashSet<>(builder.mCategories);
    }

    @Override
    public boolean isValid() {
        if (TextUtils.isEmpty(mTitle)) {
            return false;
        }

        if (TextUtils.isEmpty(mContent)) {
            return false;
        }

        if (TextUtils.isEmpty(mContentSnippet)) {
            return false;
        }

        final long published = mPublishedDate.getTime();
        if (published == NO_TIME) {
            return false;
        }
        return true;
    }

    public String getTitle() {
        return mTitle;
    }

    public String getContent() {
        return mContent;
    }

    public String getContentSnippet() {
        return mContentSnippet;
    }

    public Date getPublishedDate() {
        return new Date(mPublishedDate.getTime());
    }

    public Set<String> getCategories() {
        return new HashSet<>(mCategories);
    }

    public static final class Builder {
        private final Date mPublishedDate;
        private final Set<String> mCategories;

        private String mTitle;
        private String mContent;
        private String mContentSnippet;

        public Builder() {
            mPublishedDate = new Date();
            mCategories = new HashSet<>();
        }

        public Builder setTitle(String title) {
            mTitle = title;
            return this;
        }

        public Builder setContent(String content) {
            mContent = content;
            return this;
        }

        public Builder setContentSnippet(String contentSnippet) {
            mContentSnippet = contentSnippet;
            return this;
        }

        public Builder setPublishedDate(long timeMs) {
            mPublishedDate.setTime(timeMs);
            return this;
        }

        public Builder addCategory(String category) {
            mCategories.add(category);
            return this;
        }

        public Entry build() {
            return new Entry(this);
        }
    }
}
