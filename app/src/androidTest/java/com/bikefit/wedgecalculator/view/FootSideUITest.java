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
    public void getFootLevelId_LEFT() throws Exception {
        // GIVEN an enum for the LEFT FootSide
        FootSide leftFoot = FootSide.LEFT;

        // WHEN I REQUEST THE Level ID (for drawable resource)
        int result = leftFoot.getFootLevelId();

        // THEN I get the desired result
        assertEquals(result, 0);
    }

    @Test
    public void getFootLevelId_RIGHT() throws Exception {
        // GIVEN an enum for the LEFT FootSide
        FootSide rightFoot = FootSide.RIGHT;

        // WHEN I REQUEST THE Level ID (for drawable resource)
        int result = rightFoot.getFootLevelId();

        // THEN I get the desired result
        assertEquals(result, 1);
    }

    @Test
    public void getFootLevelLabel_LEFT() throws Exception {

        // GIVEN an enum for the LEFT FootSide
        FootSide leftFoot = FootSide.LEFT;

        // WHEN I REQUEST THE enums label
        String name = leftFoot.getLabel();

        // THEN I get the expected result
        String expectedString = mApplication.getResources().getString(R.string.footSide_left);

        assertEquals(name, expectedString);
    }

    @Test
    public void getFootLevelLabel_RIGHT() throws Exception {

        // GIVEN an enum for the LEFT FootSide
        FootSide leftFoot = FootSide.LEFT;

        // WHEN I REQUEST THE enums label
        String name = leftFoot.getLabel();

        // THEN I get the expected result
        String expectedString = mApplication.getResources().getString(R.string.footSide_left);

        assertEquals(name, expectedString);
    }
/*

    @Test
    public void getWedgeLevel_NEGATIVE() throws Exception {
        float angle = -2.9f;

        // GIVEN an FootSide enum
        FootSide foot = FootSide.LEFT;

        // WHEN I request the wedge level for the given Angle
        int wedgeLevel = foot.getWedgeLevel(angle);

        // THEN I get the desired result
        assertEquals(wedgeLevel, 0);
    }


    @Test
    public void getWedgeLevel_ZERO() throws Exception {
        float angle = 2.9f;

        // GIVEN an FootSide enum
        FootSide foot = FootSide.LEFT;

        // WHEN I request the wedge level for the given Angle
        int wedgeLevel = foot.getWedgeLevel(angle);

        // THEN I get the desired result
        assertEquals(wedgeLevel, 0);
    }

    @Test
    public void getWedgeLevel_ONE_low() throws Exception {
        float angle = 3f;

        // GIVEN an FootSide enum
        FootSide foot = FootSide.LEFT;

        // WHEN I request the wedge level for the given Angle
        int wedgeLevel = foot.getWedgeLevel(angle);

        // THEN I get the desired result
        assertEquals(wedgeLevel, 1);
    }

    @Test
    public void getWedgeLevel_ONE_high() throws Exception {
        float angle = 3.9f;

        // GIVEN an FootSide enum
        FootSide foot = FootSide.LEFT;

        // WHEN I request the wedge level for the given Angle
        int wedgeLevel = foot.getWedgeLevel(angle);

        // THEN I get the desired result
        assertEquals(wedgeLevel, 1);
    }

    @Test
    public void getWedgeLevel_TWO_low() throws Exception {
        float angle = 4f;

        // GIVEN an FootSide enum
        FootSide foot = FootSide.LEFT;

        // WHEN I request the wedge level for the given Angle
        int wedgeLevel = foot.getWedgeLevel(angle);

        // THEN I get the desired result
        assertEquals(wedgeLevel, 2);
    }

    @Test
    public void getWedgeLevel_TWO_high() throws Exception {
        float angle = 6.9f;

        // GIVEN an FootSide enum
        FootSide foot = FootSide.LEFT;

        // WHEN I request the wedge level for the given Angle
        int wedgeLevel = foot.getWedgeLevel(angle);

        // THEN I get the desired result
        assertEquals(wedgeLevel, 2);
    }

    @Test
    public void getWedgeLevel_THREE_low() throws Exception {
        float angle = 7f;

        // GIVEN an FootSide enum
        FootSide foot = FootSide.LEFT;

        // WHEN I request the wedge level for the given Angle
        int wedgeLevel = foot.getWedgeLevel(angle);

        // THEN I get the desired result
        assertEquals(wedgeLevel, 3);
    }

    @Test
    public void getWedgeLevel_THREE_high() throws Exception {
        float angle = 7.9f;

        // GIVEN an FootSide enum
        FootSide foot = FootSide.LEFT;

        // WHEN I request the wedge level for the given Angle
        int wedgeLevel = foot.getWedgeLevel(angle);

        // THEN I get the desired result
        assertEquals(wedgeLevel, 3);
    }

    @Test
    public void getWedgeLevel_FOUR_low() throws Exception {
        float angle = 8f;

        // GIVEN an FootSide enum
        FootSide foot = FootSide.LEFT;

        // WHEN I request the wedge level for the given Angle
        int wedgeLevel = foot.getWedgeLevel(angle);

        // THEN I get the desired result
        assertEquals(wedgeLevel, 4);
    }

    @Test
    public void getWedgeLevel_FOUR_high() throws Exception {
        float angle = 12.9f;

        // GIVEN an FootSide enum
        FootSide foot = FootSide.LEFT;

        // WHEN I request the wedge level for the given Angle
        int wedgeLevel = foot.getWedgeLevel(angle);

        // THEN I get the desired result
        assertEquals(wedgeLevel, 4);
    }
    
    @Test
    public void getWedgeLevel_FIVE_low() throws Exception {
        float angle = 13f;

        // GIVEN an FootSide enum
        FootSide foot = FootSide.LEFT;

        // WHEN I request the wedge level for the given Angle
        int wedgeLevel = foot.getWedgeLevel(angle);

        // THEN I get the desired result
        assertEquals(wedgeLevel, 5);
    }

    @Test
    public void getWedgeLevel_FIVE_high() throws Exception {
        float angle = 13.9f;

        // GIVEN an FootSide enum
        FootSide foot = FootSide.LEFT;

        // WHEN I request the wedge level for the given Angle
        int wedgeLevel = foot.getWedgeLevel(angle);

        // THEN I get the desired result
        assertEquals(wedgeLevel, 5);
    }


    @Test
    public void getWedgeLevel_SIX() throws Exception {
        float angle = 14.1f;

        // GIVEN an FootSide enum
        FootSide foot = FootSide.LEFT;

        // WHEN I request the wedge level for the given Angle
        int wedgeLevel = foot.getWedgeLevel(angle);

        // THEN I get the desired result
        assertEquals(wedgeLevel, 6);
    }
*/

    //endregion

    //region PRIVATE METHODS -----------------------------------------------------------------------
    //endregion

}