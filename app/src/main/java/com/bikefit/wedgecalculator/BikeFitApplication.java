package com.bikefit.wedgecalculator;

import android.app.Application;
import android.content.Context;
import android.support.v7.app.AppCompatDelegate;

import com.bikefit.wedgecalculator.settings.AnalyticsTracker;
import com.squareup.leakcanary.LeakCanary;
import com.squareup.leakcanary.RefWatcher;

/**
 * Application class for the app.
 */
public class BikeFitApplication extends Application {

    //region STATIC LOCAL CONSTANTS ----------------------------------------------------------------

    // allow resource vectors in API 19
    static {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
    }

    //endregion

    //region CLASS VARIABLES -----------------------------------------------------------------------

    private static BikeFitApplication instance;
    private RefWatcher refWatcher;

    //endregion

    //region CONSTRUCTOR ---------------------------------------------------------------------------

    public static BikeFitApplication getInstance() {
        return instance;
    }

    //endregion

    //region LIFECYCLE METHODS ---------------------------------------------------------------------

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

        AnalyticsTracker.INSTANCE.setupAnalytics(this);

    }

    //endregion

    //region PUBLIC CLASS METHODS ------------------------------------------------------------------

    // LeakCanary
    public static RefWatcher getRefWatcher(Context context) {
        if (BuildConfig.DEBUG) {
            BikeFitApplication application = (BikeFitApplication) context.getApplicationContext();
            return application.refWatcher;
        } else {
            return null;
        }
    }

    //endregion

}
