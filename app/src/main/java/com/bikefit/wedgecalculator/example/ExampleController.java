package com.bikefit.wedgecalculator.example;

import android.content.res.Resources;

import com.bikefit.wedgecalculator.R;

/**
 * Example controller that takes in a dependency and can be tested.
 */
public class ExampleController {

    //region INJECTED CLASSES ----------------------------------------------------------------------

    private final Resources mResources;

    //endregion

    //region CLASS VARIABLES -----------------------------------------------------------------------
    //endregion

    //region CONSTRUCTOR ---------------------------------------------------------------------------

    public ExampleController(Resources resources) {
        mResources = resources;
    }

    //endregion

    //region PUBLIC CLASS METHODS ------------------------------------------------------------------

    /**
     * Example method that utilizes an injected dependency that can be tested.
     *
     * @return the app name.
     */
    public String getAppName() {
        return mResources.getString(R.string.app_name);
    }

    //endregion

    //region PRIVATE METHODS -----------------------------------------------------------------------
    //endregion
}
