package com.t28.android.example.data.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class FragmentAdapter extends FragmentStatePagerAdapter {
    private final List<FragmentCreator> mFragmentCreators;

    public FragmentAdapter(FragmentManager manager, List<FragmentCreator> creators) {
        super(manager);
        if (creators == null) {
            mFragmentCreators = Collections.emptyList();
        } else {
            mFragmentCreators = new ArrayList<>(creators);
        }
    }

    @Override
    public Fragment getItem(int position) {
        final FragmentCreator creator = mFragmentCreators.get(position);
        return creator.create();
    }

    @Override
    public int getCount() {
        return mFragmentCreators.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        final FragmentCreator creator = mFragmentCreators.get(position);
        return creator.getTitle();
    }

    public interface FragmentCreator {
        public Fragment create();

        public CharSequence getTitle();
    }
}
