package com.bikefit.wedgecalculator;

import android.app.Application;
import android.support.v7.app.AppCompatDelegate;

import com.bikefit.wedgecalculator.settings.AnalyticsTracker;

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

        AnalyticsTracker.INSTANCE.setupAnalytics(this);
    }

    //endregion

    //region PUBLIC CLASS METHODS ------------------------------------------------------------------
    //endregion

}
