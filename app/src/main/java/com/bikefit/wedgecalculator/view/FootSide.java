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

    //endregion

}
