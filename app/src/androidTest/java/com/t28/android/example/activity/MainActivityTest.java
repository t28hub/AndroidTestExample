package com.t28.android.example.activity;

import android.app.Activity;
import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.IdlingResource;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.runner.AndroidJUnit4;
import android.support.v4.view.ViewPager;
import android.test.ActivityInstrumentationTestCase2;
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
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicInteger;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.registerIdlingResources;
import static android.support.test.espresso.Espresso.unregisterIdlingResources;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withEffectiveVisibility;
import static com.t28.android.example.test.Matchers.atPage;
import static com.t28.android.example.test.Matchers.withChildId;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class MainActivityTest extends ActivityInstrumentationTestCase2<MainActivity> {
    private static final int PAGE_POSITION_FIRST = 0;

    private AssetReader mAssetReader;
    private MockRequestQueue mRequestQueue;
    private CountingIdlingResource mRequestIdlingResource;
    private Activity mActivity;

    public MainActivityTest() {
        super(MainActivity.class);
    }

    @BeforeClass
    public static void setUpBeforeClass() {
        VolleyProvider.injectRequestQueueFactory(new MockRequestQueueFactory());
    }

    @Before
    public void setUp() throws Exception {
        super.setUp();
        injectInstrumentation(InstrumentationRegistry.getInstrumentation());

        final Context context = InstrumentationRegistry.getContext();
        mAssetReader = new AssetReader(context.getAssets());

        mRequestQueue = (MockRequestQueue) VolleyProvider.get().getRequestQueue(context);
        mRequestQueue.pause();

        mRequestIdlingResource = new CountingIdlingResource();
        registerIdlingResources(mRequestIdlingResource);

        final RequestQueueListener queueListener = new RequestQueueListener(mRequestIdlingResource);
        mRequestQueue.setRequestAddedListener(queueListener);
        mRequestQueue.addRequestFinishedListener(queueListener);

        mActivity = getActivity();
    }

    @After
    public void tearDown() throws Exception {
        mRequestQueue.clean();
        unregisterIdlingResources(mRequestIdlingResource);
        super.tearDown();
    }

    @Test
    public void test_activityShouldNotNull() {
    }

    @Test
    public void entryListFragment_shouldShowSuccessViewWhenResponseHasNoEntry() throws IOException {
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
    }

    @Test
    public void entryListFragment_shouldShowSuccessViewWhenResponseHasEntry() throws IOException {
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

        final ViewPager pager = (ViewPager) mActivity.findViewById(R.id.main_view_pager);
        onView(withChildId(atPage(pager, PAGE_POSITION_FIRST), R.id.entry_list_loading))
                .check(matches(withEffectiveVisibility(ViewMatchers.Visibility.GONE)));
        onView(withChildId(atPage(pager, PAGE_POSITION_FIRST), R.id.entry_list_success))
                .check(matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)));
        onView(withChildId(atPage(pager, PAGE_POSITION_FIRST), R.id.entry_list_failure))
                .check(matches(withEffectiveVisibility(ViewMatchers.Visibility.GONE)));
    }

    @Test
    public void entryListFragment_shouldShowSuccessViewWhenResponseHasEntries() throws IOException {
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
    }

    @Test
    public void entryListFragment_shouldShowFailureViewWhenResponseReturnsNotLoaded() throws IOException {
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
    }

    @Test
    public void entryListFragment_shouldShowFailureViewWhenResponseReturnsInvalidVersion() throws IOException {
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
    }

    public static class CountingIdlingResource implements IdlingResource {
        private final AtomicInteger mCounter;

        private ResourceCallback mResourceCallback;

        public CountingIdlingResource() {
            mCounter = new AtomicInteger();
        }

        @Override
        public String getName() {
            return CountingIdlingResource.class.getCanonicalName();
        }

        @Override
        public boolean isIdleNow() {
            return mCounter.get() != 0;
        }

        @Override
        public void registerIdleTransitionCallback(ResourceCallback resourceCallback) {
            mResourceCallback = resourceCallback;
        }

        public void increment() {
            mCounter.incrementAndGet();
        }

        public void decrement() {
            final int count = mCounter.decrementAndGet();
            if (count > 0) {
                return;
            }
            synchronized (this) {
                if (mResourceCallback != null) {
                    mResourceCallback.onTransitionToIdle();
                }
            }
        }
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
            mIdlingResource.decrement();
        }
    }
}
