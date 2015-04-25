package com.t28.android.example.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.contrib.CountingIdlingResource;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.v4.view.ViewPager;
import android.test.suitebuilder.annotation.LargeTest;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.t28.android.example.R;
import com.t28.android.example.test.AssetReader;
import com.t28.android.example.volley.BasicRequestMatcher;
import com.t28.android.example.volley.MethodMatcher;
import com.t28.android.example.volley.MockRequestQueue;
import com.t28.android.example.volley.MockRequestQueueFactory;
import com.t28.android.example.volley.NetworkDispatcher;
import com.t28.android.example.volley.NetworkResponseBuilder;
import com.t28.android.example.volley.StatusCode;
import com.t28.android.example.volley.VolleyProvider;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.registerIdlingResources;
import static android.support.test.espresso.Espresso.unregisterIdlingResources;
import static org.assertj.android.api.Assertions.assertThat;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class MainActivityTest {
    @Rule
    public ActivityTestRule<MainActivity> mActivityRule = new ActivityTestRule<>(MainActivity.class, true, false);

    private AssetReader mAssetReader;
    private MockRequestQueue mRequestQueue;
    private CountingIdlingResource mRequestIdlingResource;

    public MainActivityTest() {
    }

    @BeforeClass
    public static void setUpBeforeClass() {
        VolleyProvider.injectRequestQueueFactory(new MockRequestQueueFactory());
    }

    @Before
    public void setUp() {
        final Context context = InstrumentationRegistry.getContext();
        mAssetReader = new AssetReader(context.getAssets());

        mRequestQueue = (MockRequestQueue) VolleyProvider.get().getRequestQueue(context);
        mRequestQueue.pause();

        mRequestIdlingResource = new CountingIdlingResource(RequestQueue.class.getName());
        registerIdlingResources(mRequestIdlingResource);

        final RequestQueueListener queueListener = new RequestQueueListener(mRequestIdlingResource);
        mRequestQueue.setRequestAddedListener(queueListener);
        mRequestQueue.addRequestFinishedListener(queueListener);
    }

    @After
    public void tearDown() {
        mRequestQueue.clean();
        unregisterIdlingResources(mRequestIdlingResource);
    }

    /**
     * 読込中にLoadingViewが表示される
     */
    @Test
    public void entryListFragment_shouldShowLoadingViewWhenWaitingForResponse() {
        final Activity activity = getActivity();
        final ViewPager pager = (ViewPager) activity.findViewById(R.id.main_view_pager);
        assertThat(pager.findViewById(R.id.entry_list_loading)).isVisible();
        assertThat(pager.findViewById(R.id.entry_list_success)).isGone();
        assertThat(pager.findViewById(R.id.entry_list_failure)).isGone();
    }

    /**
     * エントリーが存在しない場合にSuccessViewが表示される
     */
    @Test
    public void entryListFragment_shouldShowSuccessViewWhenResponseHasNoEntry() {
        final NetworkDispatcher dispatcher = mRequestQueue.getNetworkDispatcher();
        dispatcher.append(
                new BasicRequestMatcher.Builder()
                        .setMethodMatcher(MethodMatcher.GET)
                        .setUrlPattern("^https://ajax.googleapis.com/ajax/services/feed/load.+")
                        .build(),
                new NetworkResponseBuilder()
                        .setStatusCode(StatusCode.OK)
                        .addHeader("Content-Type", "application/json")
                        .setBody(mAssetReader.read("feed_load_success_0.json"))
                        .build()
        );
        mRequestQueue.resume();

        final Activity activity = getActivity();
        final ViewPager pager = (ViewPager) activity.findViewById(R.id.main_view_pager);
        assertThat(pager.findViewById(R.id.entry_list_loading)).isGone();
        assertThat(pager.findViewById(R.id.entry_list_success)).isVisible();
        assertThat(pager.findViewById(R.id.entry_list_failure)).isGone();
    }

    /**
     * エントリーが存在する場合にSuccessViewが表示される
     */
    @Test
    public void entryListFragment_shouldShowSuccessViewWhenResponseHasEntry() {
        final NetworkDispatcher dispatcher = mRequestQueue.getNetworkDispatcher();
        dispatcher.append(
                new BasicRequestMatcher.Builder()
                        .setMethodMatcher(MethodMatcher.GET)
                        .setUrlPattern("^https://ajax.googleapis.com/ajax/services/feed/load.+")
                        .build(),
                new NetworkResponseBuilder()
                        .setStatusCode(StatusCode.OK)
                        .addHeader("Content-Type", "application/json")
                        .setBody(mAssetReader.read("feed_load_success_1.json"))
                        .build()
        );
        mRequestQueue.resume();

        final Activity activity = getActivity();
        final ViewPager pager = (ViewPager) activity.findViewById(R.id.main_view_pager);
        assertThat(pager.findViewById(R.id.entry_list_loading)).isGone();
        assertThat(pager.findViewById(R.id.entry_list_success)).isVisible();
        assertThat(pager.findViewById(R.id.entry_list_failure)).isGone();
    }

    /**
     * エントリーが複数存在する場合にSuccessViewが表示される
     */
    @Test
    public void entryListFragment_shouldShowSuccessViewWhenResponseHasEntries() {
        final NetworkDispatcher dispatcher = mRequestQueue.getNetworkDispatcher();
        dispatcher.append(
                new BasicRequestMatcher.Builder()
                        .setMethodMatcher(MethodMatcher.GET)
                        .setUrlPattern("^https://ajax.googleapis.com/ajax/services/feed/load.+")
                        .build(),
                new NetworkResponseBuilder()
                        .setStatusCode(StatusCode.OK)
                        .addHeader("Content-Type", "application/json")
                        .setBody(mAssetReader.read("feed_load_success_10.json"))
                        .build()
        );
        mRequestQueue.resume();

        final Activity activity = getActivity();
        final ViewPager pager = (ViewPager) activity.findViewById(R.id.main_view_pager);
        assertThat(pager.findViewById(R.id.entry_list_loading)).isGone();
        assertThat(pager.findViewById(R.id.entry_list_success)).isVisible();
        assertThat(pager.findViewById(R.id.entry_list_failure)).isGone();
    }

    /**
     * 読込失敗時にFailureViewが表示される
     */
    @Test
    public void entryListFragment_shouldShowFailureViewWhenResponseReturnsNotLoaded() {
        final NetworkDispatcher dispatcher = mRequestQueue.getNetworkDispatcher();
        dispatcher.append(
                new BasicRequestMatcher.Builder()
                        .setMethodMatcher(MethodMatcher.GET)
                        .setUrlPattern("^https://ajax.googleapis.com/ajax/services/feed/load.+")
                        .build(),
                new NetworkResponseBuilder()
                        .setStatusCode(StatusCode.OK)
                        .addHeader("Content-Type", "application/json")
                        .setBody(mAssetReader.read("feed_load_failure_could_not_be_loaded.json"))
                        .build()
        );
        mRequestQueue.resume();

        final Activity activity = getActivity();
        final ViewPager pager = (ViewPager) activity.findViewById(R.id.main_view_pager);
        assertThat(pager.findViewById(R.id.entry_list_loading)).isGone();
        assertThat(pager.findViewById(R.id.entry_list_success)).isGone();
        assertThat(pager.findViewById(R.id.entry_list_failure)).isVisible();
    }

    /**
     * 読込失敗時にFailureViewが表示される
     */
    @Test
    public void entryListFragment_shouldShowFailureViewWhenResponseReturnsInvalidVersion() {
        final NetworkDispatcher dispatcher = mRequestQueue.getNetworkDispatcher();
        dispatcher.append(
                new BasicRequestMatcher.Builder()
                        .setMethodMatcher(MethodMatcher.GET)
                        .setUrlPattern("^https://ajax.googleapis.com/ajax/services/feed/load.+")
                        .build(),
                new NetworkResponseBuilder()
                        .setStatusCode(StatusCode.OK)
                        .addHeader("Content-Type", "application/json")
                        .setBody(mAssetReader.read("feed_load_failure_invalid_version.json"))
                        .build()
        );
        mRequestQueue.resume();

        final Activity activity = getActivity();
        final ViewPager pager = (ViewPager) activity.findViewById(R.id.main_view_pager);
        assertThat(pager.findViewById(R.id.entry_list_loading)).isGone();
        assertThat(pager.findViewById(R.id.entry_list_success)).isGone();
        assertThat(pager.findViewById(R.id.entry_list_failure)).isVisible();
    }

    private MainActivity getActivity() {
        final Intent launchIntent = new Intent();
        return mActivityRule.launchActivity(launchIntent);
    }

    public static class RequestQueueListener implements MockRequestQueue.RequestAddedListener, RequestQueue.RequestFinishedListener {
        private final CountingIdlingResource mIdlingResource;

        public RequestQueueListener(CountingIdlingResource resource) {
            if (resource == null) {
                throw new NullPointerException("resource == null");
            }
            mIdlingResource = resource;
        }

        @Override
        public void onRequestAdded(Request request) {
            mIdlingResource.increment();
        }

        @Override
        public void onRequestFinished(Request request) {
            try {
                mIdlingResource.decrement();
            } catch (IllegalStateException ignore) {
            }
        }
    }
}
