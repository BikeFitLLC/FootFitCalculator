package com.bikefit.wedgecalculator.settings;

import android.content.Context;
import android.content.SharedPreferences;

import com.bikefit.wedgecalculator.BikeFitApplication;
import com.bikefit.wedgecalculator.measure.model.FootSide;

/**
 * Class that encapsulates the default application settings
 */
public final class Settings {

    //region CLASS VARIABLES -----------------------------------------------------------------------

    static final String SETTINGS = "SETTINGS";
    static final String EXAMPLE_PREF = "EXAMPLE_PREF";

    static final String LEFT_FOOT_ANGLE_PREFERENCE = "LEFT_FOOT_ANGLE_PREFERENCE";
    static final String LEFT_FOOT_WEDGE_COUNT_PREFERENCE = "LEFT_FOOT_WEDGE_COUNT_PREFERENCE";

    static final String RIGHT_FOOT_ANGLE_PREFERENCE = "RIGHT_FOOT_ANGLE_PREFERENCE";
    static final String RIGHT_FOOT_WEDGE_COUNT_PREFERENCE = "RIGHT_FOOT_WEDGE_COUNT_PREFERENCE";

    //endregion

    //region CONSTRUCTOR ---------------------------------------------------------------------------

    private Settings() {
    }

    //endregion

    //region PRIVATE METHODS -----------------------------------------------------------------------

    private static SharedPreferences getSharedPreferences() {
        return BikeFitApplication.getInstance().getSharedPreferences(SETTINGS, Context.MODE_PRIVATE);
    }

    //endregion

    //region PUBLIC CLASS METHODS ------------------------------------------------------------------

    public static String getExamplePref() {
        return getSharedPreferences().getString(EXAMPLE_PREF, "");
    }

    public static void setExamplePref(String test) {
        SharedPreferences.Editor editor = getSharedPreferences().edit();
        editor.putString(EXAMPLE_PREF, test);
        editor.apply();
    }

    public static void setFootAngle(FootSide footSide, Float angle) {
        SharedPreferences.Editor editor = getSharedPreferences().edit();

        if (angle == null) {
            if (footSide == FootSide.RIGHT) {
                editor.remove(RIGHT_FOOT_ANGLE_PREFERENCE).apply();
            } else {
                editor.remove(LEFT_FOOT_ANGLE_PREFERENCE).apply();
            }
        } else {
            angle = Math.abs(angle);
            if (footSide == FootSide.RIGHT) {
                editor.putFloat(RIGHT_FOOT_ANGLE_PREFERENCE, angle).apply();
            } else {
                editor.putFloat(LEFT_FOOT_ANGLE_PREFERENCE, angle).apply();
            }
        }
    }

    public static Float getFootAngle(FootSide footSide) {
        SharedPreferences preferences = getSharedPreferences();
        float angle;
        if (footSide == FootSide.RIGHT) {
            angle = preferences.getFloat(RIGHT_FOOT_ANGLE_PREFERENCE, -1);
        } else {
            angle = preferences.getFloat(LEFT_FOOT_ANGLE_PREFERENCE, -1);
        }
        return (angle == -1) ? null : angle;
    }

    public static void setWedgeCount(FootSide footSide, Integer wedgeCount) {
        SharedPreferences.Editor editor = getSharedPreferences().edit();

        if (wedgeCount == null) {
            if (footSide == FootSide.RIGHT) {
                editor.remove(RIGHT_FOOT_WEDGE_COUNT_PREFERENCE).apply();
            } else {
                editor.remove(LEFT_FOOT_WEDGE_COUNT_PREFERENCE).apply();
            }
        } else {
            if (footSide == FootSide.RIGHT) {
                editor.putInt(RIGHT_FOOT_WEDGE_COUNT_PREFERENCE, wedgeCount).apply();
            } else {
                editor.putInt(LEFT_FOOT_WEDGE_COUNT_PREFERENCE, wedgeCount).apply();
            }
        }
    }

    public static Integer getWedgeCount(FootSide footSide) {
        SharedPreferences preferences = getSharedPreferences();

        int wedgeCount;
        if (footSide == FootSide.RIGHT) {
            wedgeCount = preferences.getInt(RIGHT_FOOT_WEDGE_COUNT_PREFERENCE, -1);
        } else {
            wedgeCount = preferences.getInt(LEFT_FOOT_WEDGE_COUNT_PREFERENCE, -1);
        }

        return (wedgeCount == -1) ? null : wedgeCount;
    }

    //endregion

}
