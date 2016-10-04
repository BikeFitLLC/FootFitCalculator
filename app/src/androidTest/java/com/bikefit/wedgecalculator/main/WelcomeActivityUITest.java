package com.bikefit.wedgecalculator.main;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.bikefit.wedgecalculator.R;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

/**
 * UI Test for the WelcomeActivity
 */
@RunWith(AndroidJUnit4.class)
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


    @Test
    public void testOptionButtonsAppear() {
        // WHEN the activity starts
        mActivityRule.launchActivity(null);

        // THEN the 3 options appear as buttons
        onView(withId(R.id.activity_welcome_more_button)).check(matches(isDisplayed()));
        onView(withId(R.id.activity_welcome_orientation_button)).check(matches(isDisplayed()));
        onView(withId(R.id.activity_welcome_start_button)).check(matches(isDisplayed()));

    }


    //endregion

    //region PRIVATE METHODS -----------------------------------------------------------------------
    //endregion

}