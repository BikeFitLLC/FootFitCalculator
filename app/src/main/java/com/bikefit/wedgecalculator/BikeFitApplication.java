package com.bikefit.wedgecalculator;

import android.app.Application;
import android.support.v7.app.AppCompatDelegate;

/**
 * Application class for the app.
 */
public class BikeFitApplication extends Application {
    private static BikeFitApplication instance;

    static
    {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
    }

    public static BikeFitApplication getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        instance = this;
    }

}
