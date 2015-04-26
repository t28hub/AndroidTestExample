package com.t28.android.example.data.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class FragmentAdapter extends FragmentStatePagerAdapter {
    private final List<FragmentFactory> mFragmentFactories;

    public FragmentAdapter(FragmentManager manager, List<FragmentFactory> creators) {
        super(manager);
        if (creators == null) {
            mFragmentFactories = Collections.emptyList();
        } else {
            mFragmentFactories = new ArrayList<>(creators);
        }
    }

    @Override
    public Fragment getItem(int position) {
        final FragmentFactory creator = mFragmentFactories.get(position);
        return creator.create();
    }

    @Override
    public int getCount() {
        return mFragmentFactories.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        final FragmentFactory factory = mFragmentFactories.get(position);
        return factory.getTitle();
    }

    public interface FragmentFactory {
        public Fragment create();

        public CharSequence getTitle();
    }
}
