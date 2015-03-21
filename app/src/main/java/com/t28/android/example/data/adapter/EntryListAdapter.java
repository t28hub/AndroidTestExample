package com.t28.android.example.data.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.t28.android.example.R;
import com.t28.android.example.data.model.Entry;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class EntryListAdapter extends RecyclerView.Adapter<EntryListAdapter.ItemViewHolder> {
    private List<Entry> mEntries;

    public EntryListAdapter() {
        super();
        mEntries = Collections.emptyList();
    }

    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        final View itemView = inflater.inflate(R.layout.layout_entry_list_item, parent, false);
        return new ItemViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ItemViewHolder holder, int position) {
        final Entry entry = mEntries.get(position);
        holder.bind(entry);
    }

    @Override
    public int getItemCount() {
        return mEntries.size();
    }

    public void changeEntries(List<Entry> entries) {
        if (entries == null) {
            mEntries = Collections.emptyList();
        } else {
            mEntries = new ArrayList<>(entries);
        }
        notifyDataSetChanged();
    }

    public static class ItemViewHolder extends RecyclerView.ViewHolder {
        private final TextView mTitleView;

        public ItemViewHolder(View itemView) {
            super(itemView);
            mTitleView = (TextView) itemView.findViewById(R.id.entry_list_item_title);
        }

        public void bind(Entry entry) {
            mTitleView.setText(entry.getTitle());
        }
    }
}
