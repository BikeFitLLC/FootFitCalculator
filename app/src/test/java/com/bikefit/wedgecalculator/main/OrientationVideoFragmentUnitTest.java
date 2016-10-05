package com.bikefit.wedgecalculator.main;

import android.view.View;
import android.webkit.WebView;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.verify;


public class OrientationVideoFragmentUnitTest {

    //region CLASS UNDER TEST ----------------------------------------------------------------------
    //endregion

    //region MOCKS ---------------------------------------------------------------------------------

    @Mock
    WebView mMockWebview;

    @Mock
    View mMockMainView;

    //endregion

    //region NON-MOCKS -----------------------------------------------------------------------------
    //endregion

    //region SETUP ---------------------------------------------------------------------------------

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
    }

    //endregion

    //region TESTS ---------------------------------------------------------------------------------

    @Test
    public void testNewInstance() throws Exception {

        // GIVEN a known url
        String url = "_known_url_";

        // GIVEN a fragment with a known url
        OrientationVideoFragment fragment = OrientationVideoFragment.newInstance(url);

        // GIVEN fragment has a webview
        fragment.webView = mMockWebview;

        // WHEN the onViewCreated method is called
        fragment.onViewCreated(mMockMainView, null);

        // THEN the webview is setup with the expected url
        verify(mMockWebview).loadUrl(url);
    }

    //endregion

    //region PRIVATE METHODS -----------------------------------------------------------------------
    //endregion

}