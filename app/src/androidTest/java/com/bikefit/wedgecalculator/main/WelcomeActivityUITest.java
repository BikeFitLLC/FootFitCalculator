package com.bikefit.wedgecalculator.main;

import android.support.test.rule.ActivityTestRule;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;

/**
 * Created by max on 10/3/16.
 */
public class WelcomeActivityUITest {

    //region CLASS UNDER TEST ----------------------------------------------------------------------

    @Rule
    public ActivityTestRule<WelcomeActivity> mActivityRule = new ActivityTestRule<>(
            WelcomeActivity.class, false, false);

    //endregion

    //region MOCKS ---------------------------------------------------------------------------------
    //endregion

    //region NON-MOCKS -----------------------------------------------------------------------------
    //endregion

    //region SETUP ---------------------------------------------------------------------------------


    @Before
    public void setUp() throws Exception {

    }

    @After
    public void tearDown() throws Exception {

    }

    //endregion

    //region TESTS ---------------------------------------------------------------------------------

/*
    @Ignore
    @Test
    public void testHelloWorldText() {
        // WHEN the activity starts
        mActivityRule.launchActivity(null);


        // THEN the hello world text is displayed
        onView(withText("Bikefit")).check(matches(isDisplayed()));
    }
*/

    //endregion

    //region PRIVATE METHODS -----------------------------------------------------------------------
    //endregion

}