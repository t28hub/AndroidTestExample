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
        return getFactory(position).create();
    }

    @Override
    public int getCount() {
        return mFragmentFactories.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return getFactory(position).getTitle();
    }

    private FragmentFactory getFactory(int position) {
        return mFragmentFactories.get(position);
    }

    public interface FragmentFactory {
        Fragment create();

        CharSequence getTitle();
    }
}
