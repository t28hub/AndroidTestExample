package com.t28.android.example.activity;

import android.app.Activity;
import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.support.v4.view.ViewPager;
import android.test.ActivityInstrumentationTestCase2;
import android.test.suitebuilder.annotation.LargeTest;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.t28.android.example.R;
import com.t28.android.example.test.AssetReader;
import com.t28.android.example.test.CountingIdlingResource;
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

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.registerIdlingResources;
import static android.support.test.espresso.Espresso.unregisterIdlingResources;
import static com.t28.android.example.test.Assertions.isGone;
import static com.t28.android.example.test.Assertions.isVisible;
import static com.t28.android.example.test.Matchers.atPage;
import static com.t28.android.example.test.Matchers.withChildId;
import static org.assertj.android.api.Assertions.assertThat;

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

        mRequestIdlingResource = new CountingIdlingResource(RequestQueue.class.getName());
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

    /**
     * <p>
     * NOTE: テスト実行時にJUnit3系のテストメソッドが存在しないと他のテストメソッドも実行されない。
     * </p>
     */
    @Test
    public void test_activityShouldNotNull() {
    }

    /**
     * 読込中にLoadingViewが表示される
     */
    @Test
    public void entryListFragment_shouldShowLoadingViewWhenWaitingForResponse() {
        final ViewPager pager = (ViewPager) mActivity.findViewById(R.id.main_view_pager);
        onView(withChildId(atPage(pager, PAGE_POSITION_FIRST), R.id.entry_list_loading)).check(isVisible());
        onView(withChildId(atPage(pager, PAGE_POSITION_FIRST), R.id.entry_list_success)).check(isGone());
        onView(withChildId(atPage(pager, PAGE_POSITION_FIRST), R.id.entry_list_failure)).check(isGone());
    }

    /**
     * エントリーが存在しない場合にSuccessViewが表示される
     *
     * @throws IOException assetファイル読み込みに失敗した場合
     */
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

        final ViewPager pager = (ViewPager) mActivity.findViewById(R.id.main_view_pager);
        assertThat(pager.findViewById(R.id.entry_list_loading)).isGone();
        assertThat(pager.findViewById(R.id.entry_list_success)).isVisible();
        assertThat(pager.findViewById(R.id.entry_list_failure)).isGone();
    }

    /**
     * エントリーが存在する場合にSuccessViewが表示される
     *
     * @throws IOException assetファイル読み込みに失敗した場合
     */
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
        onView(withChildId(atPage(pager, PAGE_POSITION_FIRST), R.id.entry_list_loading)).check(isGone());
        onView(withChildId(atPage(pager, PAGE_POSITION_FIRST), R.id.entry_list_success)).check(isVisible());
        onView(withChildId(atPage(pager, PAGE_POSITION_FIRST), R.id.entry_list_failure)).check(isGone());
    }

    /**
     * エントリーが複数存在する場合にSuccessViewが表示される
     *
     * @throws IOException assetファイル読み込みに失敗した場合
     */
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

        final ViewPager pager = (ViewPager) mActivity.findViewById(R.id.main_view_pager);
        onView(withChildId(atPage(pager, PAGE_POSITION_FIRST), R.id.entry_list_loading)).check(isGone());
        onView(withChildId(atPage(pager, PAGE_POSITION_FIRST), R.id.entry_list_success)).check(isVisible());
        onView(withChildId(atPage(pager, PAGE_POSITION_FIRST), R.id.entry_list_failure)).check(isGone());
    }

    /**
     * 読込失敗時にFailureViewが表示される
     *
     * @throws IOException assetファイル読み込みに失敗した場合
     */
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

        final ViewPager pager = (ViewPager) mActivity.findViewById(R.id.main_view_pager);
        onView(withChildId(atPage(pager, PAGE_POSITION_FIRST), R.id.entry_list_loading)).check(isGone());
        onView(withChildId(atPage(pager, PAGE_POSITION_FIRST), R.id.entry_list_success)).check(isGone());
        onView(withChildId(atPage(pager, PAGE_POSITION_FIRST), R.id.entry_list_failure)).check(isVisible());
    }

    /**
     * 読込失敗時にFailureViewが表示される
     *
     * @throws IOException assetファイル読み込みに失敗した場合
     */
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

        final ViewPager pager = (ViewPager) mActivity.findViewById(R.id.main_view_pager);
        onView(withChildId(atPage(pager, PAGE_POSITION_FIRST), R.id.entry_list_loading)).check(isGone());
        onView(withChildId(atPage(pager, PAGE_POSITION_FIRST), R.id.entry_list_success)).check(isGone());
        onView(withChildId(atPage(pager, PAGE_POSITION_FIRST), R.id.entry_list_failure)).check(isVisible());
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
