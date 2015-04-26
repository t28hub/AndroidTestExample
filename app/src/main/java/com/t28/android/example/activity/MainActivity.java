package com.t28.android.example.activity;

import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.t28.android.example.R;
import com.t28.android.example.data.adapter.FragmentAdapter;
import com.t28.android.example.fragment.EntryListFragment;
import com.t28.android.example.view.SlidingTabLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;


public class MainActivity extends ActionBarActivity {
    @InjectView(R.id.main_sliding_tab)
    SlidingTabLayout mSlidingTab;

    @InjectView(R.id.main_view_pager)
    ViewPager mFragmentPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.inject(this);

        final List<FragmentAdapter.FragmentFactory> factories = createFactories();
        final PagerAdapter adapter = new FragmentAdapter(getSupportFragmentManager(), factories);
        mFragmentPager.setAdapter(adapter);

        mSlidingTab.setViewPager(mFragmentPager);
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

    private List<FragmentAdapter.FragmentFactory> createFactories() {
        final List<FragmentAdapter.FragmentFactory> factories = new ArrayList<>();
        factories.add(new EntryListFragment.Factory("http://b.hatena.ne.jp/hotentry.rss", "総合"));
        factories.add(new EntryListFragment.Factory("http://b.hatena.ne.jp/hotentry/general.rss", "一般"));
        factories.add(new EntryListFragment.Factory("http://b.hatena.ne.jp/hotentry/social.rss", "世の中"));
        factories.add(new EntryListFragment.Factory("http://b.hatena.ne.jp/hotentry/economics.rss", "政治と経済"));
        factories.add(new EntryListFragment.Factory("http://b.hatena.ne.jp/hotentry/life.rss", "暮らし"));
        factories.add(new EntryListFragment.Factory("http://b.hatena.ne.jp/hotentry/knowledge.rss", "学び"));
        factories.add(new EntryListFragment.Factory("http://b.hatena.ne.jp/hotentry/it.rss", "テクノロジー"));
        factories.add(new EntryListFragment.Factory("http://b.hatena.ne.jp/hotentry/fun.rss", "おもしろ"));
        factories.add(new EntryListFragment.Factory("http://b.hatena.ne.jp/hotentry/entertainment.rss", "エンタメ"));
        return factories;
    }
}
