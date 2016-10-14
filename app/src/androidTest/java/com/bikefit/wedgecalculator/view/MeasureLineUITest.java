package com.bikefit.wedgecalculator.view;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.bikefit.wedgecalculator.R;
import com.bikefit.wedgecalculator.test.TestMeasureLineActivity;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

/**
 * Test for the Measure Line custom view
 */
@RunWith(AndroidJUnit4.class)
public class MeasureLineUITest {

    //region CLASS UNDER TEST ----------------------------------------------------------------------

    @Rule
    public ActivityTestRule<TestMeasureLineActivity> mActivityRule = new ActivityTestRule<TestMeasureLineActivity>(TestMeasureLineActivity.class, false, false);

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
    public void testViewDisplayed() throws Exception {
        mActivityRule.launchActivity(null);
        onView(withId(R.id.measurement_fragment_draw_line)).check(matches(isDisplayed()));
    }


    //endregion

    //region PRIVATE METHODS -----------------------------------------------------------------------
    //endregion


}