package com.bikefit.wedgecalculator.view;

import com.bikefit.wedgecalculator.BikeFitApplication;
import com.bikefit.wedgecalculator.R;

/**
 * A representation of either a Left or Right foot
 */
public enum FootSide {

    //region ENUM Values -----------------------------------------------------------------------

    LEFT(R.string.footSide_left),       // LEFT ORDINAL SHOULD BE 0 (ZERO)
    RIGHT(R.string.footSide_right);     // RIGHT ORDINAL SHOULD BE 1 (ONE)

    //endregion

    //region PUBLIC INTERFACES ---------------------------------------------------------------------

    public static String FOOTSIDE_KEY = "FOOTSIDE_KEY";

    //endregion

    //region CLASS VARIABLES -----------------------------------------------------------------------

    private final String footSideLabel;

    //endregion

    //region CONSTRUCTOR ---------------------------------------------------------------------------

    FootSide(int sideNameResourceId) {
        this.footSideLabel = BikeFitApplication.getInstance().getResources().getString(sideNameResourceId);
    }

    //endregion

    //region ACCESSORS -----------------------------------------------------------------------------

    public String getLabel() {
        return footSideLabel;
    }

    public int getFootLevelId() {
        return this.ordinal();
    }

    public static int getWedgeLevel(float angle) {

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

    //endregion

}
