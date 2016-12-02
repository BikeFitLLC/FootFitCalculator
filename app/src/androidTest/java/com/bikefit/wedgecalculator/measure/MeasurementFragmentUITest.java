package com.bikefit.wedgecalculator.measure;

import android.support.test.InstrumentationRegistry;
import android.support.test.rule.ActivityTestRule;

import com.bikefit.wedgecalculator.R;
import com.bikefit.wedgecalculator.measure.model.FootSide;
import com.bikefit.wedgecalculator.settings.AnalyticsTracker;
import com.bikefit.wedgecalculator.test.TestFragmentActivity;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.MockitoAnnotations;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

/**
 * Tests for the MeasureFragment UI
 */
public class MeasurementFragmentUITest {

    //region CLASS UNDER TEST ----------------------------------------------------------------------

    @Rule
    public ActivityTestRule<TestFragmentActivity> mActivityRule = new ActivityTestRule<>(TestFragmentActivity.class, false, false);

    //endregion

    //region MOCKS ---------------------------------------------------------------------------------
    //endregion

    //region NON-MOCKS -----------------------------------------------------------------------------
    //endregion

    //region SETUP ---------------------------------------------------------------------------------

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        AnalyticsTracker.INSTANCE.setDryRun(true);
    }
    //endregion

    //region TESTS ---------------------------------------------------------------------------------

    @Test
    public void testTitlebarLeftFoot() {
        //GIVEN a measurement for the left foot has been completed (we don't care about the image itself)
        TestFragmentActivity activity = mActivityRule.launchActivity(null);
        final MeasurementFragment fragment = MeasurementFragment.newInstance(FootSide.LEFT, "");
        fragment.setDialogDisplayed(true);

        // WHEN the page loads
        activity.transactToFragment(fragment);
        InstrumentationRegistry.getInstrumentation().waitForIdleSync();

        // THEN I see the titlebar
        onView(withId(R.id.toolbar)).check(matches(isDisplayed()));

        // THEN I see the expected text in titlebar
        String expectedTitle = activity.getResources().getString(R.string.measurement_fragment_title_text, FootSide.LEFT.getLabel());
        onView(withText(expectedTitle)).check(matches(isDisplayed()));
    }

    @Test
    public void testTitlebarRightFoot() {
        //GIVEN a measurement for the right foot has been completed (we don't care about the image itself)
        TestFragmentActivity activity = mActivityRule.launchActivity(null);
        final MeasurementFragment fragment = MeasurementFragment.newInstance(FootSide.RIGHT, "");
        fragment.setDialogDisplayed(true);

        // WHEN the page loads
        activity.transactToFragment(fragment);
        InstrumentationRegistry.getInstrumentation().waitForIdleSync();

        // THEN I see the titlebar
        onView(withId(R.id.toolbar)).check(matches(isDisplayed()));

        // THEN I see the expected text in titlebar
        String expectedTitle = activity.getResources().getString(R.string.measurement_fragment_title_text, FootSide.RIGHT.getLabel());
        onView(withText(expectedTitle)).check(matches(isDisplayed()));
    }

    //endregion

    //region PRIVATE METHODS -----------------------------------------------------------------------
    //endregion

}