package com.bikefit.wedgecalculator.settings;

import android.content.Context;

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

    boolean mDryRun = false;
    GoogleAnalytics mAnalytics;
    Tracker mTracker;

    //endregion

    //region CONSTRUCTOR ---------------------------------------------------------------------------
    //endregion

    //region ACCESSORS -----------------------------------------------------------------------------
    //endregion

    //region PUBLIC CLASS METHODS ------------------------------------------------------------------

    public synchronized void setupAnalytics(Context context) {
        mAnalytics = GoogleAnalytics.getInstance(context);
        mTracker = mAnalytics.newTracker(PROPERTY_ID);
        mAnalytics.setDryRun(mDryRun);
        //BuildConfig.DEBUG
    }

    public boolean getDryRun() {
        return mDryRun;
    }

    /**
     * Set the dryRun flag for Google Analytics.  If the Analytics object hasn't been setup yet, this
     * class will set the value whenever the obejct has been setup (setupAnalytics)
     * <p>
     * From Documentation:
     * The SDK provides a dryRun flag that when set, prevents any data from being sent to Google Analytics.
     * The dryRun flag should be set whenever you are testing or debugging an implementation and
     * do not want test data to appear in your Google Analytics reports.
     *
     * @param dryRun enable dryRun or not
     */
    public synchronized void setDryRun(boolean dryRun) {
        mDryRun = dryRun;

        // if mAnalytics has already been setup, update its dryRun flag
        if (mAnalytics != null) {
            mAnalytics.setDryRun(mDryRun);
        }
    }

    /**
     * Send a screen view to Google Analytics.
     * Clear the screen from the tracker before returning.
     *
     * @param screenName Name of the screen.
     */
    synchronized public void sendAnalyticsScreen(String screenName) {

        if (mAnalytics == null || mTracker == null) {
            return;
        }

        mTracker.setScreenName(screenName);
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());
        mTracker.setScreenName(null);
    }

    //endregion

    //region PRIVATE METHODS -----------------------------------------------------------------------
    //endregion

    //region INNER CLASSES -------------------------------------------------------------------------
    //endregion

}
