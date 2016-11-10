package com.bikefit.wedgecalculator.model;

import com.bikefit.wedgecalculator.settings.Settings;
import com.bikefit.wedgecalculator.view.FootSide;

/**
 * A simple model for storing values (angle & wedge count) from measuring the Right and Left foot
 */
public class MeasureModel {

    //region ACCESSORS -----------------------------------------------------------------------------

    public static Float getAngle(FootSide footSide) {
        return Settings.getFootAngle(footSide);
    }

    public static Integer getWedgeCount(FootSide footSide) {
        return Settings.getWedgeCount(footSide);
    }

    public static void setFootData(FootSide footSide, Float angle, Integer wedgeCount) {
        Settings.setFootAngle(footSide, angle);
        Settings.setWedgeCount(footSide, wedgeCount);
    }

    //endregion

    //region PUBLIC CLASS METHODS ------------------------------------------------------------------

    /**
     * Map the given angle to a drawable level-list level, for use with the drawable ImageView
     *
     * @param angle The angle given from the measurement
     * @return The ImageView (drawable) level-list level
     */
    public static int getWedgeImageLevel(float angle) {

        angle = Math.abs(angle);
        int wedgeLevel;

        if (angle < 3) {
            wedgeLevel = 0;
        } else if (angle >= 3 && angle < 4) {
            wedgeLevel = 1;
        } else if (angle >= 4 && angle < 7) {
            wedgeLevel = 2;
        } else if (angle >= 7 && angle < 8) {
            wedgeLevel = 3;
        } else if (angle >= 8 && angle < 13) {
            wedgeLevel = 4;
        } else if (angle >= 13 && angle < 14) {
            wedgeLevel = 5;
        } else {
            wedgeLevel = 6;
        }

        return wedgeLevel;
    }

    /**
     * Map the given angle to the number of wedges needed
     *
     * @param angle the angle given from the measurement
     * @return The number of wedges required (max 3)
     */
    public static int calculateWedgeCount(float angle) {

        angle = Math.abs(angle);
        int wedgeCount;

        if (angle > 0 && angle <= 6) {
            wedgeCount = 1;
        } else if (angle > 6 && angle <= 12) {
            wedgeCount = 2;
        } else if (angle > 12) {
            wedgeCount = 3;
        } else {
            wedgeCount = 0;
        }

        return wedgeCount;
    }

    //endregion

    //region PRIVATE METHODS -----------------------------------------------------------------------
    //endregion

    //region OBSERVERS -----------------------------------------------------------------------------
    //endregion

    //region INNER CLASSES -------------------------------------------------------------------------
    //endregion

}
