package com.bikefit.wedgecalculator.view;

import android.support.test.InstrumentationRegistry;

import com.bikefit.wedgecalculator.BikeFitApplication;
import com.bikefit.wedgecalculator.R;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Tests for different states of the FootSide enum
 */
public class FootSideUITest {

    //region CLASS UNDER TEST ----------------------------------------------------------------------
    //endregion

    //region MOCKS ---------------------------------------------------------------------------------
    //endregion

    //region NON-MOCKS -----------------------------------------------------------------------------

    private BikeFitApplication mApplication;

    //endregion

    //region SETUP ---------------------------------------------------------------------------------

    @Before
    public void setUp() throws Exception {
        mApplication = (BikeFitApplication) InstrumentationRegistry.getTargetContext().getApplicationContext();
    }


    //endregion

    //region TESTS ---------------------------------------------------------------------------------

    @Test
    public void getFootLevelId() throws Exception {
        // GIVEN an enum for the foot
        // WHEN I REQUEST THE Level ID (for drawable resource)
        // THEN I get the expected result

        int left = 0;
        int right = 1;

        // test both left & right
        assertEquals(left, FootSide.LEFT.getFootLevelId());
        assertEquals(right, FootSide.RIGHT.getFootLevelId());
    }

    @Test
    public void getFootLevelLabel() throws Exception {
        // GIVEN an enum for the foot
        // WHEN I request the enum's lable
        // THEN I get the expected result

        String left = mApplication.getResources().getString(R.string.footSide_left);
        String right = mApplication.getResources().getString(R.string.footSide_right);

        // test both left & right
        assertEquals(left, FootSide.LEFT.getLabel());
        assertEquals(right, FootSide.RIGHT.getLabel());
    }

    //endregion

    //region PRIVATE METHODS -----------------------------------------------------------------------
    //endregion

}