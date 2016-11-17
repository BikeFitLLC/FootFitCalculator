package com.bikefit.wedgecalculator.settings;

import android.content.Context;
import android.util.Log;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

/**
 * Wrapper for Google Analytics (Singleton via enum)
 */
public enum AnalyticsTracker {

    // declare INSTANCE of the Enum
    INSTANCE;

    //region STATIC LOCAL CONSTANTS ----------------------------------------------------------------

    private static final String PROPERTY_ID = "A-87599004-1";

    //endregion

    //region CLASS VARIABLES -----------------------------------------------------------------------

    GoogleAnalytics mAnalytics;
    boolean mAnalyticsEnabled = false;
    Tracker mTracker;

    //endregion

    //region CONSTRUCTOR ---------------------------------------------------------------------------
    //endregion

    //region ACCESSORS -----------------------------------------------------------------------------
    //endregion

    //region PUBLIC CLASS METHODS ------------------------------------------------------------------

    public synchronized void setupAnalytics(Context context) {
        mAnalytics = GoogleAnalytics.getInstance(context);
        mAnalyticsEnabled = !mAnalytics.getAppOptOut();
        mTracker = mAnalytics.newTracker(PROPERTY_ID);
    }

    public synchronized boolean setAnalyticsEnabled(boolean enable) {
        if (mAnalytics == null) {
            mAnalyticsEnabled = false;
        } else {
            mAnalytics.setAppOptOut(!enable);
            mAnalyticsEnabled = enable;
        }
        return mAnalyticsEnabled;
    }

    public boolean getAnalyticsEnabled() {
        mAnalyticsEnabled = mAnalytics != null && !mAnalytics.getAppOptOut();
        return mAnalyticsEnabled;
    }

    /**
     * Send a screen view to Google Analytics.
     * Clear the screen from the tracker before returning.
     *
     * @param screenName Name of the screen.
     */
    synchronized public void sendAnalyticsScreen(String screenName) {

        if (!mAnalyticsEnabled) {
            Log.i(this.getClass().getSimpleName(), "Send Analytics Screen CANCELED (" + screenName + "): Analytics Disabled");
            return;
        }

        mTracker.setScreenName(screenName);
        mTracker.send(new HitBuilders.AppViewBuilder().build());
        mTracker.setScreenName(null);
    }

    //endregion

    //region PRIVATE METHODS -----------------------------------------------------------------------
    //endregion

    //region INNER CLASSES -------------------------------------------------------------------------
    //endregion

}
