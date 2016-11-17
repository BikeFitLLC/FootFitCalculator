package com.bikefit.wedgecalculator.main;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.bikefit.wedgecalculator.R;
import com.bikefit.wedgecalculator.settings.AnalyticsTracker;
import com.bikefit.wedgecalculator.startup.SplashActivity;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

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
        AnalyticsTracker.INSTANCE.setAnalyticsEnabled(false);
    }

    //endregion

    //region TESTS ---------------------------------------------------------------------------------

    @Test
    public void splashScreenDisplayed() {
        // WHEN the activity starts
        mActivityRule.launchActivity(null);

        // THEN the root layout is displayed
        onView(withId(R.id.splash_activity_rootLayout)).check(matches(isDisplayed()));
    }

    //endregion

    //region PRIVATE METHODS -----------------------------------------------------------------------
    //endregion

}
