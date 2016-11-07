package com.bikefit.wedgecalculator.main;

import android.support.test.InstrumentationRegistry;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.bikefit.wedgecalculator.R;
import com.bikefit.wedgecalculator.test.TestFragmentActivity;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

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

    //endregion

    //region NON-MOCKS -----------------------------------------------------------------------------
    //endregion

    //region SETUP ---------------------------------------------------------------------------------

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

        // GIVEN the webview returns settings
        when(mMockWebview.getSettings()).thenReturn(mMockWebSettings);

    }

    //endregion

    //region TESTS ---------------------------------------------------------------------------------

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
                String webviewUrl = fragment.webView.getOriginalUrl();
                assertTrue(webviewUrl.contains(partialUrl));
            }
        });

    }


    @Test
    public void testNewInstance_uiTestEmptyUrl() throws Exception {

        TestFragmentActivity activity = mActivityRule.launchActivity(null);

        final String expectedUrl = activity.getString(R.string.orientation_video_url);

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
                String webviewUrl = fragment.webView.getOriginalUrl();
                assertTrue(webviewUrl.contains(expectedUrl));
            }
        });

    }

    @Test
    public void testNewInstance_uiTestNullUrl() throws Exception {

        TestFragmentActivity activity = mActivityRule.launchActivity(null);

        final String expectedUrl = activity.getString(R.string.orientation_video_url);

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
                String webviewUrl = fragment.webView.getOriginalUrl();
                assertTrue(webviewUrl.contains(expectedUrl));
            }
        });

    }


    //endregion

    //region PRIVATE METHODS -----------------------------------------------------------------------
    //endregion

}