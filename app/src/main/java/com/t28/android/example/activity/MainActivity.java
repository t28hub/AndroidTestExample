package com.t28.android.example.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.t28.android.example.R;
import com.t28.android.example.data.adapter.FragmentAdapter;
import com.t28.android.example.fragment.EntryListFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;


public class MainActivity extends ActionBarActivity {
    @InjectView(R.id.main_view_pager)
    ViewPager mFragmentPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.inject(this);

        final List<FragmentAdapter.FragmentCreator> creators = new ArrayList<>();
        creators.add(new FragmentAdapter.FragmentCreator() {
            private static final String FEED_URL = "http://feeds.feedburner.com/hatena/b/hotentry";

            @Override
            public Fragment create() {
                return EntryListFragment.newInstance(FEED_URL, 50);
            }

            @Override
            public CharSequence getTitle() {
                return FEED_URL;
            }
        });
        creators.add(new FragmentAdapter.FragmentCreator() {
            private static final String FEED_URL = "http://feeds.feedburner.com/blogspot/hsDu";

            @Override
            public Fragment create() {
                return EntryListFragment.newInstance(FEED_URL, 50);
            }

            @Override
            public CharSequence getTitle() {
                return FEED_URL;
            }
        });
        final PagerAdapter adapter = new FragmentAdapter(getSupportFragmentManager(), creators);
        mFragmentPager.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        final int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
