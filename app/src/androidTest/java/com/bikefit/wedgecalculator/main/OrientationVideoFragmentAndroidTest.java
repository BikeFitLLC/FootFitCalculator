package com.bikefit.wedgecalculator.main;

import android.support.test.InstrumentationRegistry;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.bikefit.wedgecalculator.R;
import com.bikefit.wedgecalculator.settings.AnalyticsTracker;
import com.bikefit.wedgecalculator.settings.InternetUtil;
import com.bikefit.wedgecalculator.test.TestFragmentActivity;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

@RunWith(AndroidJUnit4.class)
public class OrientationVideoFragmentAndroidTest {

    //region CLASS UNDER TEST ----------------------------------------------------------------------

    @Rule
    public ActivityTestRule<TestFragmentActivity> mActivityRule = new ActivityTestRule<>(
            TestFragmentActivity.class, false, false);

    //endregion

    //region MOCKS VIEWS ---------------------------------------------------------------------------

    @Mock
    WebView mMockWebview;

    //endregion

    //region MOCKS ---------------------------------------------------------------------------------

    @Mock
    WebSettings mMockWebSettings;

    @Mock
    View mMockMainView;

    @Mock
    FragmentActivity mMockActivity;

    @Mock
    InternetUtil mMockInternetUtil;

    //endregion

    //region NON-MOCKS -----------------------------------------------------------------------------
    //endregion

    //region SETUP ---------------------------------------------------------------------------------

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

        AnalyticsTracker.INSTANCE.setAnalyticsEnabled(false);

        // GIVEN the webview returns settings
        when(mMockWebview.getSettings()).thenReturn(mMockWebSettings);
    }

    //endregion

    //region TESTS ---------------------------------------------------------------------------------

    @Test
    public void testViewsAppear() throws Exception {

        // GIVEN an activity to load the fragment
        TestFragmentActivity activity = mActivityRule.launchActivity(null);

        // WHEN the fragment is loaded with default options
        final OrientationVideoFragment fragment = OrientationVideoFragment.newInstance("");
        activity.transactToFragment(fragment);

        // THEN the expected view elements are displayed
        onView(withId(R.id.toolbar)).check(matches(isDisplayed()));
        onView(withId(R.id.orientation_video_fragment_webview)).check(matches(isDisplayed()));
        onView(withId(R.id.orientation_video_fragment_done_button)).check(matches(isDisplayed()));
    }

    @Test
    public void testNewInstance_uiTest() throws Exception {

        TestFragmentActivity activity = mActivityRule.launchActivity(null);

        // GIVEN a known url
        final String partialUrl = "www.google.com";
        final String url = "http://" + partialUrl;

        // GIVEN a fragment with the given url
        final OrientationVideoFragment fragment = OrientationVideoFragment.newInstance(url);
        activity.transactToFragment(fragment);

        // THEN the webview is setup with the expected url
        InstrumentationRegistry.getInstrumentation().waitForIdleSync();

        //todo find a better way to wait for webview
        Thread.sleep(1000);

        InstrumentationRegistry.getInstrumentation().runOnMainSync(new Runnable() {
            @Override
            public void run() {
                String webviewUrl = fragment.mWebView.getOriginalUrl();
                assertTrue(webviewUrl.contains(partialUrl));
            }
        });

    }


    @Test
    public void testNewInstance_uiTestEmptyUrl() throws Exception {

        TestFragmentActivity activity = mActivityRule.launchActivity(null);

        final String expectedUrl = activity.getString(R.string.orientation_video_default_url);

        // GIVEN an empty url
        final String url = "";

        // GIVEN a fragment with the given url
        final OrientationVideoFragment fragment = OrientationVideoFragment.newInstance(url);
        activity.transactToFragment(fragment);

        // THEN the webview is setup with the expected url
        InstrumentationRegistry.getInstrumentation().waitForIdleSync();

        //todo find a better way to wait for webview
        Thread.sleep(1000);

        InstrumentationRegistry.getInstrumentation().runOnMainSync(new Runnable() {
            @Override
            public void run() {
                String webviewUrl = fragment.mWebView.getOriginalUrl();
                assertTrue(webviewUrl.contains(expectedUrl));
            }
        });

    }

    @Test
    public void testNewInstance_uiTestNullUrl() throws Exception {

        TestFragmentActivity activity = mActivityRule.launchActivity(null);

        final String expectedUrl = activity.getString(R.string.orientation_video_default_url);

        // GIVEN a null url
        final String url = null;

        // GIVEN a fragment with the given url
        final OrientationVideoFragment fragment = OrientationVideoFragment.newInstance(url);
        activity.transactToFragment(fragment);

        // THEN the webview is setup with the expected url
        InstrumentationRegistry.getInstrumentation().waitForIdleSync();

        //todo find a better way to wait for webview
        Thread.sleep(1000);

        InstrumentationRegistry.getInstrumentation().runOnMainSync(new Runnable() {
            @Override
            public void run() {
                String webviewUrl = fragment.mWebView.getOriginalUrl();
                assertTrue(webviewUrl.contains(expectedUrl));
            }
        });

    }

    @Test
    public void webView_NoInternet() throws Exception {

        //NOTE: it's too difficult to determine the text displayed in a webview, so we just
        // test on a boolean member variable in the fragment called mInternetAvailable

        TestFragmentActivity activity = mActivityRule.launchActivity(null);

        // GIVEN the app is set to NOT have an active internet connection
        when(mMockInternetUtil.checkInternet()).thenReturn(false);

        // WHEN the fragment is loaded (and we "inject" the Mock)
        final OrientationVideoFragment fragment = OrientationVideoFragment.newInstance("");
        fragment.mInternetUtil = mMockInternetUtil;
        activity.transactToFragment(fragment);

        // THEN the fragment reports there is no internet available
        assertFalse(fragment.mInternetAvailable);
    }

    //endregion

    //region PRIVATE METHODS -----------------------------------------------------------------------
    //endregion

}