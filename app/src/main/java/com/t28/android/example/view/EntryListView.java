package com.t28.android.example.view;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.ViewGroup;

public class EntryListView extends RecyclerView {
    private static final int NO_DEFAULT_STYLE = 0;

    public EntryListView(Context context) {
        this(context, null, NO_DEFAULT_STYLE);
    }

    public EntryListView(Context context, AttributeSet attrs) {
        this(context, attrs, NO_DEFAULT_STYLE);
    }

    public EntryListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setLayoutManager(new NoLayoutManager());
    }

    private class NoLayoutManager extends LayoutManager {

        public NoLayoutManager() {
        }

        @Override
        public LayoutParams generateDefaultLayoutParams() {
            return new LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
            );
        }
    }
}
