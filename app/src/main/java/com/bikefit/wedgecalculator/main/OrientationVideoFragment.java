package com.bikefit.wedgecalculator.main;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.bikefit.wedgecalculator.R;
import com.bikefit.wedgecalculator.settings.AnalyticsTracker;
import com.bikefit.wedgecalculator.settings.InternetUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Fragment to display Orientation Video
 * - loads the url given in key: URL_KEY
 * - if no url is given, it will default to the resource string: R.string.orientation_video_url
 */
public class OrientationVideoFragment extends Fragment {

    //region STATIC LOCAL CONSTANTS ----------------------------------------------------------------

    private static final String URL_KEY = "URL_KEY";

    //endregion

    //region INJECTED VIEWS ------------------------------------------------------------------------

    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    @BindView(R.id.orientation_video_fragment_webview)
    WebView mWebView;

    //endregion

    //region CLASS VARIABLES -----------------------------------------------------------------------

    private Unbinder mViewUnBinder;
    boolean mInternetAvailable = false;
    String mUrl = null;
    InternetUtil mInternetUtil = new InternetUtil();

    //endregion

    //region CONSTRUCTOR ---------------------------------------------------------------------------

    public static OrientationVideoFragment newInstance(String url) {
        Bundle args = new Bundle();
        args.putString(URL_KEY, url);

        OrientationVideoFragment fragment = new OrientationVideoFragment();
        fragment.setArguments(args);
        return fragment;
    }

    //endregion

    //region LIFECYCLE METHODS ---------------------------------------------------------------------

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.orientation_video_fragment, container, false);
        mViewUnBinder = ButterKnife.bind(this, view);

        mToolbar.setTitle(getResources().getString(R.string.orientation_video_fragment_title_label));
        mToolbar.setNavigationOnClickListener(mNavigationListener);

        AnalyticsTracker.INSTANCE.sendAnalyticsScreen(getResources().getString(R.string.orientation_video_fragment_title_label));

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mWebView.setWebViewClient(new WebViewClient());
        mWebView.getSettings().setJavaScriptEnabled(true);

        Bundle args = getArguments();

        if (args != null) {
            mUrl = args.getString(URL_KEY);
        }

        if (mUrl == null || mUrl.isEmpty()) {
            //default to known video url
            mUrl = getActivity().getResources().getString(R.string.orientation_video_default_url);
        }

        mInternetAvailable = mInternetUtil.checkInternet();
        loadWebView();
    }

    @Override
    public void onPause() {
        mWebView.onPause();
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        mWebView.onResume();
    }

    @Override
    public void onDestroyView() {
        mToolbar = null;
        mWebView = null;
        mInternetUtil = null;

        super.onDestroyView();
        mViewUnBinder.unbind();
    }

    //endregion

    //region ACCESSORS -----------------------------------------------------------------------------
    //endregion

    //region PUBLIC CLASS METHODS ------------------------------------------------------------------

    public void loadWebView() {
        if (mInternetAvailable) {
            mWebView.loadUrl(mUrl);
        } else {
            String noConnectionMessage = getString(R.string.orientation_video_no_connection);
            mWebView.loadData(noConnectionMessage, "text/html; charset=utf-8", "UTF-8");
        }
    }

    public void setUrl(String url) {
        mUrl = url;
    }

    //endregion

    //region PRIVATE METHODS -----------------------------------------------------------------------
    //endregion

    //region LISTENERS -----------------------------------------------------------------------------

    View.OnClickListener mNavigationListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            getActivity().onBackPressed();
        }
    };

    @OnClick(R.id.orientation_video_fragment_done_button)
    public void onDoneButton() {
        getActivity().onBackPressed();
    }

    //endregion

    //region INNER CLASSES -------------------------------------------------------------------------
    //endregion

}
