package com.t28.android.example.pages;

import android.support.annotation.IdRes;
import android.support.test.espresso.ViewInteraction;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewParent;
import android.widget.TextView;

import com.t28.android.example.R;
import com.t28.android.example.activity.MainActivity;
import com.t28.android.example.view.SlidingTabLayout;

import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;

import java.lang.ref.WeakReference;

import butterknife.ButterKnife;
import butterknife.InjectView;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

public class Main {
    @InjectView(R.id.main_toolbar)
    Toolbar mToolbar;
    @InjectView(R.id.main_sliding_tab)
    SlidingTabLayout mSlidingTabLayout;
    @InjectView(R.id.main_view_pager)
    ViewPager mViewPager;

    public Main(MainActivity activity) {
        if (activity == null) {
            throw new NullPointerException("activity == null");
        }
        ButterKnife.inject(this, activity);
    }

    public void showOptionsMenu() {
        mToolbar.showOverflowMenu();
    }

    public void hideOptionsMenu() {
        mToolbar.hideOverflowMenu();
    }

    public void selectOptionsMenu(@IdRes int id) {
        final Menu menu = mToolbar.getMenu();
        final MenuItem menuItem = menu.findItem(id);
        if (menuItem == null) {
            throw new IllegalStateException("MenuItem is not found:" + id);
        }

        final String title = menuItem.getTitle().toString();
        onView(withText(title)).perform(click());
    }

    public void clickTabAt(int position) {
        final ViewInteraction tabInteraction = findTabAt(position);
        tabInteraction.perform(click());
    }

    private ViewInteraction findTabAt(int position) {
        final PagerAdapter adapter = getPagerAdapter();
        final CharSequence title = adapter.getPageTitle(position);
        return onView(new SlidingTabTitleMatcher(title));
    }

    private PagerAdapter getPagerAdapter() {
        return mViewPager.getAdapter();
    }

    static class StrictViewMatcher extends TypeSafeMatcher<View> {
        private final WeakReference<View> mView;

        public StrictViewMatcher(View view) {
            mView = new WeakReference<View>(view);
        }

        @Override
        public boolean matchesSafely(View view) {
            final View expectedView = getView();
            if (expectedView == null) {
                return false;
            }
            return expectedView.equals(view);
        }

        @Override
        public void describeTo(Description description) {
        }

        private View getView() {
            return mView.get();
        }
    }

    static class SlidingTabTitleMatcher extends TypeSafeMatcher<View> {
        private final CharSequence mTitle;

        SlidingTabTitleMatcher(CharSequence title) {
            if (TextUtils.isEmpty(title)) {
                throw new IllegalArgumentException("'title' must not be empty");
            }
            mTitle = title;
        }

        @Override
        public boolean matchesSafely(View view) {
            final ViewParent parent = view.getParent();
            if (!(parent instanceof SlidingTabLayout)) {
                return false;
            }

            if (!(view instanceof TextView)) {
                return false;
            }

            final TextView textView = (TextView) view;
            return TextUtils.equals(mTitle, textView.getText());
        }

        @Override
        public void describeTo(Description description) {
        }
    }
}
