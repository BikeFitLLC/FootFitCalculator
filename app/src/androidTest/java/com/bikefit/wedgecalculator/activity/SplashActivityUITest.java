package com.bikefit.wedgecalculator.activity;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

@RunWith(AndroidJUnit4.class)
public class SplashActivityUITest {

    //region CLASS UNDER TEST ----------------------------------------------------------------------

    @Rule
    public ActivityTestRule<SplashActivity> mActivityRule = new ActivityTestRule<>(
            SplashActivity.class, false, false);

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

    @Test
    public void testHelloWorldText() {
        // WHEN the activity starts
        mActivityRule.launchActivity(null);

        // THEN the hello world text is displayed
        onView(withText("Bikefit")).check(matches(isDisplayed()));
    }

    //endregion

    //region PRIVATE METHODS -----------------------------------------------------------------------
    //endregion

}
