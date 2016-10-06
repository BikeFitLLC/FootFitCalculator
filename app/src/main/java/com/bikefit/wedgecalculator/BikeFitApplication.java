package com.bikefit.wedgecalculator;

import android.app.Application;

/**
 * Application class for the app.
 */
public class BikeFitApplication extends Application {
    private static BikeFitApplication instance;

    public static BikeFitApplication getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        instance = this;
    }

}
