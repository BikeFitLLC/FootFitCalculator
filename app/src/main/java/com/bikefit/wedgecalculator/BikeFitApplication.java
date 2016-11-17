package com.bikefit.wedgecalculator;

import android.app.Application;
import android.content.Context;
import android.support.v7.app.AppCompatDelegate;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.squareup.leakcanary.LeakCanary;
import com.squareup.leakcanary.RefWatcher;

/**
 * Application class for the app.
 */
public class BikeFitApplication extends Application {
    private static BikeFitApplication instance;
    private RefWatcher refWatcher;

    private static final String PROPERTY_ID = "A-87599004-1";
    private Tracker mTracker;

    // allow resource vectors in API 19
    static {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
    }


    public static BikeFitApplication getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;

        if (BuildConfig.DEBUG) {
            if (LeakCanary.isInAnalyzerProcess(this)) {
                // This process is dedicated to leakCanary for heap analysis.
                // You should not init your app in this process.
                return;
            }
            refWatcher = LeakCanary.install(this);
        }

    }

    public static RefWatcher getRefWatcher(Context context) {
        if (BuildConfig.DEBUG) {
            BikeFitApplication application = (BikeFitApplication) context.getApplicationContext();
            return application.refWatcher;
        } else {
            return null;
        }
    }

    synchronized public Tracker getDefaultTracker() {
        if (mTracker == null) {
            GoogleAnalytics analytics = GoogleAnalytics.getInstance(this);
            mTracker = analytics.newTracker(PROPERTY_ID);
        }
        return mTracker;
    }

    /**
     * Send a screen view to Google Analytics.
     * Clear the screen from the tracker before returning.
     *
     * @param screenName Name of the screen.
     */
    synchronized public void sendAnalyticsView(String screenName) {
        if (mTracker == null) {
            getDefaultTracker();
        }
        mTracker.setScreenName(screenName);
        mTracker.send(new HitBuilders.AppViewBuilder().build());
        mTracker.setScreenName(null);
    }


}
