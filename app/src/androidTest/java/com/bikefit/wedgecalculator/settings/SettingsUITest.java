package com.bikefit.wedgecalculator.settings;

import android.support.test.rule.ActivityTestRule;

import com.bikefit.wedgecalculator.example.ExampleActivity;
import com.bikefit.wedgecalculator.measure.model.FootSide;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.MockitoAnnotations;

import static org.junit.Assert.assertEquals;

/**
 * Test any logic in the Settings (shared preferences) class
 */
public class SettingsUITest {

    //region CLASS UNDER TEST ----------------------------------------------------------------------

    @Rule
    public ActivityTestRule<ExampleActivity> mActivityRule = new ActivityTestRule<>(ExampleActivity.class, false, false);

    //endregion

    //region MOCKS ---------------------------------------------------------------------------------
    //endregion

    //region NON-MOCKS -----------------------------------------------------------------------------
    //endregion

    //region SETUP ---------------------------------------------------------------------------------

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
    }

    //endregion

    //region TESTS ---------------------------------------------------------------------------------

    @Test
    public void verifyExamplePref() throws Exception {
        String givenString = "alexander graham bell";

        // GIVEN the Activity starts (I have an application object)
        mActivityRule.launchActivity(null);

        // WHEN I set the Example Pref
        Settings.setExamplePref(givenString);

        // WHEN I get the Example Pref
        String expectedString = Settings.getExamplePref();

        // THEN I get the expected string back
        assertEquals(givenString, expectedString);
    }

    @Test
    public void verifyFootAngle() throws Exception {
        // GIVEN the Activity starts (I have an application object)
        mActivityRule.launchActivity(null);

        // GIVEN a footSide and Angle
        // WHEN I set the angle for the foot
        // THEN I get the same angle value when requested
        assertEquals((Float) 10f, testFootAngle(FootSide.RIGHT, 10f));
        assertEquals((Float) 2f, testFootAngle(FootSide.LEFT, 2f));

        assertEquals(null, testFootAngle(FootSide.RIGHT, null));
        assertEquals(null, testFootAngle(FootSide.LEFT, null));
    }

    @Test
    public void verifyWedgeCount() throws Exception {
        // GIVEN the Activity starts (I have an application object)
        mActivityRule.launchActivity(null);

        // GIVEN a footSide and wedge count
        // WHEN I set the wedge count for the foot
        // THEN I get the same wedge count value when requested

        assertEquals((Integer) 1, testWedgeCountPreference(FootSide.RIGHT, 1));
        assertEquals((Integer) 5, testWedgeCountPreference(FootSide.LEFT, 5));

        assertEquals(null, testWedgeCountPreference(FootSide.RIGHT, null));
        assertEquals(null, testWedgeCountPreference(FootSide.LEFT, null));

        assertEquals(null, testWedgeCountPreference(FootSide.RIGHT, -1));
        assertEquals(null, testWedgeCountPreference(FootSide.LEFT, -1));
    }

    //endregion

    //region PRIVATE METHODS -----------------------------------------------------------------------

    /**
     * Test the angle setting by setting the angle for the foot, then immediately retrieving it
     *
     * @param foot  The FootSide to set (& retrieve)
     * @param angle The Angle to set (can be null)
     * @return The angle retrieved from settings
     */
    private Float testFootAngle(FootSide foot, Float angle) {
        // GIVEN a footSide and Angle
        // WHEN I set the angle for the foot
        Settings.setFootAngle(foot, angle);
        // THEN I get an angle when requested
        return Settings.getFootAngle(foot);
    }

    private Integer testWedgeCountPreference(FootSide foot, Integer wedgeCount) {
        // GIVEN a FootSide and wedge count
        // WHEN I set the wedge count for the foot
        Settings.setWedgeCount(foot, wedgeCount);
        // THEN I get the wedge count when requested
        return Settings.getWedgeCount(foot);
    }
    //endregion

}