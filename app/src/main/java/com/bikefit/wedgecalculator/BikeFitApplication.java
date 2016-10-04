package com.bikefit.wedgecalculator;

import android.app.Application;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

/**
 * Application class for the app.
 */
public class BikeFitApplication extends Application {
    private static BikeFitApplication instance;

    public static BikeFitApplication getInstance() {
        return instance;
    }

    public static String getAppVersion() {
        String appVersion = "";

        try {
            PackageManager pm = instance.getPackageManager();
            PackageInfo packageInfo = pm.getPackageInfo(instance.getPackageName(), 0);
            appVersion = packageInfo.versionName;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return appVersion;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        instance = this;
    }

}
