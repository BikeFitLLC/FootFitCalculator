package com.bikefit.wedgecalculator.model;

import android.support.test.InstrumentationRegistry;

import com.bikefit.wedgecalculator.BikeFitApplication;
import com.bikefit.wedgecalculator.settings.Settings;
import com.bikefit.wedgecalculator.view.FootSide;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

/**
 * Created by johnny5 on 11/9/16.
 */
public class MeasureModelUITest {

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
    public void getAngle() throws Exception {
        // GIVEN I have set an Angle for the foot
        Float setValue = 1.1f;
        Settings.setFootAngle(FootSide.LEFT, setValue);

        // WHEN I request the Angle
        Float retrievedValue = MeasureModel.getAngle(FootSide.LEFT);

        // THEN I get the desired result
        assertEquals(setValue, retrievedValue);
    }

    @Test
    public void getWedgeCount() throws Exception {
        // GIVEN I have set a wedge count for the foot
        Integer setValue = 3;
        Settings.setWedgeCount(FootSide.LEFT, setValue);

        // WHEN I request the wedge count
        Integer retrievedValue = MeasureModel.getWedgeCount(FootSide.LEFT);

        // THEN I get the desired result
        assertEquals(setValue, retrievedValue);
    }

    @Test
    public void setFootData() throws Exception {
        // GIVEN I have an angle and wedge count
        Float setAngle = 1.1f;
        Integer setWedgeCount = 3;

        // WHEN I send the measurements to setFootData()
        MeasureModel.setFootData(FootSide.LEFT, setAngle, setWedgeCount);

        // THEN I get the desired values back when requested
        Float getAngle = MeasureModel.getAngle(FootSide.LEFT);
        Integer getWedgeCount = MeasureModel.getWedgeCount(FootSide.LEFT);

        assertEquals(setAngle, getAngle);
        assertEquals(setWedgeCount, getWedgeCount);
    }

    @Test
    public void setFootDataNULL() throws Exception {
        // GIVEN I have an angle and wedge count that are null
        // WHEN I send the measurements to setFootData()
        MeasureModel.setFootData(FootSide.LEFT, null, null);

        // THEN I get the desired values back when requested
        Float getAngle = MeasureModel.getAngle(FootSide.LEFT);
        Integer getWedgeCount = MeasureModel.getWedgeCount(FootSide.LEFT);

        assertNull(getAngle);
        assertNull(getWedgeCount);
    }


    @Test
    public void getWedgeImageLevel() throws Exception {

        // GIVEN an angle
        // WHEN I request the wedge ImageView level for the given angle
        // THEN I get the desired result

        // Test the bounds of each ImageLevel for the level-list
        assertEquals(0, MeasureModel.getWedgeImageLevel(-0.5f));
        assertEquals(0, MeasureModel.getWedgeImageLevel(2.9f));

        assertEquals(1, MeasureModel.getWedgeImageLevel(3f));
        assertEquals(1, MeasureModel.getWedgeImageLevel(3.9f));

        assertEquals(2, MeasureModel.getWedgeImageLevel(4f));
        assertEquals(2, MeasureModel.getWedgeImageLevel(6.9f));

        assertEquals(3, MeasureModel.getWedgeImageLevel(7f));
        assertEquals(3, MeasureModel.getWedgeImageLevel(7.9f));

        assertEquals(4, MeasureModel.getWedgeImageLevel(8f));
        assertEquals(4, MeasureModel.getWedgeImageLevel(12.9f));

        assertEquals(5, MeasureModel.getWedgeImageLevel(13f));
        assertEquals(5, MeasureModel.getWedgeImageLevel(13.9f));

        assertEquals(6, MeasureModel.getWedgeImageLevel(14.1f));
    }

    @Test
    public void calculateWedgeCount() throws Exception {

        // GIVEN an angle
        // WHEN I request the wedge count for the given angle
        // THEN I get the desired wedge count

        // Test the bounds for each level of the wedge count
        assertEquals(0, MeasureModel.calculateWedgeCount(0));

        assertEquals(1, MeasureModel.calculateWedgeCount(-.01f));
        assertEquals(1, MeasureModel.calculateWedgeCount(.01f));
        assertEquals(1, MeasureModel.calculateWedgeCount(6f));

        assertEquals(2, MeasureModel.calculateWedgeCount(6.1f));
        assertEquals(2, MeasureModel.calculateWedgeCount(12f));

        assertEquals(3, MeasureModel.calculateWedgeCount(12.1f));
    }

    //endregion

    //region PRIVATE METHODS -----------------------------------------------------------------------
    //endregion


}