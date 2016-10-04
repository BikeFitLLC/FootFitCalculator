package com.bikefit.wedgecalculator.settings;

import android.content.Context;
import android.content.SharedPreferences;

import com.bikefit.wedgecalculator.BikeFitApplication;

/**
 * Class that encapsulates the default application settings
 */
public final class Settings {

    private static final String SETTINGS = "SETTINGS";
    private static final String EXAMPLE_PREF = "EXAMPLE_PREF";

    private Settings() {
    }

    public static String getExamplePref() {
        SharedPreferences preferences = BikeFitApplication.getInstance().getSharedPreferences(SETTINGS, Context.MODE_PRIVATE);
        return preferences.getString(EXAMPLE_PREF, "");
    }

    public static void setExamplePref(String test) {
        SharedPreferences preferences = BikeFitApplication.getInstance().getSharedPreferences(SETTINGS, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(EXAMPLE_PREF, test);
    }

}
