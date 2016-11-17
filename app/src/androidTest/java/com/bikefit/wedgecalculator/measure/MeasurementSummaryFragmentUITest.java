package com.bikefit.wedgecalculator.measure;

import android.support.annotation.ColorInt;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.matcher.BoundedMatcher;
import android.support.test.rule.ActivityTestRule;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.TextView;

import com.bikefit.wedgecalculator.R;
import com.bikefit.wedgecalculator.measure.model.FootSide;
import com.bikefit.wedgecalculator.measure.model.MeasureModel;
import com.bikefit.wedgecalculator.settings.AnalyticsTracker;
import com.bikefit.wedgecalculator.test.TestFragmentActivity;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.MockitoAnnotations;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.not;

/**
 * Test the various states of the Measurement Summary view
 */

public class MeasurementSummaryFragmentUITest {

    //region CLASS UNDER TEST ----------------------------------------------------------------------

    @Rule
    public ActivityTestRule<TestFragmentActivity> mActivityRule = new ActivityTestRule<>(TestFragmentActivity.class, false, false);

    //endregion

    //region MOCKS ---------------------------------------------------------------------------------
    //endregion

    //region NON-MOCKS -----------------------------------------------------------------------------

    //MeasureModel

    //endregion

    //region SETUP ---------------------------------------------------------------------------------

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

        AnalyticsTracker.INSTANCE.setAnalyticsEnabled(false);
    }

    //endregion

    //region TESTS ---------------------------------------------------------------------------------

    @Test
    public void leftFootOnlyMeasured() {
        //test one foot measured using LEFT foot
        verifyOneFootMeasured(FootSide.LEFT);
    }

    @Test
    public void rightFootOnlyMeasured() {
        //test one foot measured using RIGHT foot
        verifyOneFootMeasured(FootSide.RIGHT);
    }

    @Test
    public void bothFeetMeasured() {
        // GIVEN I have measured ONLY ONE FOOT
        MeasureModel.setFootData(FootSide.LEFT, 5.0f, 2);
        MeasureModel.setFootData(FootSide.RIGHT, 10.0f, 2);

        int totalWedges = MeasureModel.getWedgeCount(FootSide.LEFT) + MeasureModel.getWedgeCount(FootSide.RIGHT);

        // WHEN the page loads
        TestFragmentActivity activity = getTestActivity();
        InstrumentationRegistry.getInstrumentation().waitForIdleSync();

        // THEN I get the desired results:

        // THEN I get the expected title
        String expectedTitle = activity.getResources().getString(R.string.measurement_summary_fragment_title_label);
        onView(withText(expectedTitle)).check(matches(isDisplayed()));

        // THEN the active header section is enabled
        verifyFootHeaderSection(FootSide.LEFT, true, activity);

        // THEN the inactive section is enabled
        verifyFootHeaderSection(FootSide.RIGHT, true, activity);

        // THEN the Instructions text matches the expected value
        String expectedText = activity.getResources().getString(R.string.measurement_summary_fragment_complete_instruction_text, totalWedges);
        onView(withText(expectedText)).check(matches(isDisplayed()));

        // THEN verify the 3 buttons are displayed
        verifyBothFeetButtons();
    }

    @Test
    public void noFeetMeasured() {
        // GIVEN I have measured NO FEET
        MeasureModel.setFootData(FootSide.LEFT, null, null);
        MeasureModel.setFootData(FootSide.RIGHT, null, null);

        // WHEN the page loads
        TestFragmentActivity activity = getTestActivity();

        InstrumentationRegistry.getInstrumentation().waitForIdleSync();

        // THEN I get the desired results:

        // THEN I get the expected title
        String expectedTitle = activity.getResources().getString(R.string.measurement_summary_fragment_title_nofeet_label);
        onView(withText(expectedTitle)).check(matches(isDisplayed()));

        // THEN the active header section is enabled
        verifyFootHeaderSection(FootSide.LEFT, false, activity);

        // THEN the inactive section is enabled
        verifyFootHeaderSection(FootSide.RIGHT, false, activity);

        // THEN the Instructions text matches the expected value
        String expectedText = activity.getResources().getString(R.string.measurement_summary_fragment_nofeet_measured_instruction_label);
        onView(withText(expectedText)).check(matches(isDisplayed()));

        // THEN verify no buttons are displayed
        verifyNoFeetButtons();
    }


    //endregion

    //region PRIVATE METHODS -----------------------------------------------------------------------


    private TestFragmentActivity getTestActivity() {
        TestFragmentActivity activity = mActivityRule.launchActivity(null);
        final MeasurementSummaryFragment fragment = MeasurementSummaryFragment.newInstance();
        activity.transactToFragment(fragment);
        return activity;
    }

    /**
     * VERIFY that one foot has been measured.  The foot passed in will be the assumed measured foot
     *
     * @param footSide The foot that has a measurement (Right/Left)
     */
    private void verifyOneFootMeasured(FootSide footSide) {

        FootSide activeFoot, inactiveFoot;

        if (footSide == FootSide.LEFT) {
            activeFoot = FootSide.LEFT;
            inactiveFoot = FootSide.RIGHT;
        } else {
            activeFoot = FootSide.RIGHT;
            inactiveFoot = FootSide.LEFT;
        }

        // GIVEN I have measured only one foot
        MeasureModel.setFootData(activeFoot, 11.0f, 2);
        MeasureModel.setFootData(inactiveFoot, null, null);

        // WHEN the page loads
        TestFragmentActivity activity = getTestActivity();

        InstrumentationRegistry.getInstrumentation().waitForIdleSync();

        // THEN I get the desired results:

        // THEN I get the expected title
        String expectedTitle = activity.getResources().getString(R.string.measurement_summary_fragment_title_onefoot_label, activeFoot.getLabel());
        onView(withText(expectedTitle)).check(matches(isDisplayed()));

        // THEN the active header section is enabled
        verifyFootHeaderSection(activeFoot, true, activity);

        // THEN the inactive section is disabled
        verifyFootHeaderSection(inactiveFoot, false, activity);

        // THEN the Instructions text matches the expected value
        String expectedText = activity.getResources().getString(R.string.measurement_summary_fragment_incomplete_instruction_text, activeFoot.getLabel(), inactiveFoot.getLabel());
        onView(withText(expectedText)).check(matches(isDisplayed()));

        // THEN verify only one button is displayed
        verifyOneFootButtons();
    }

    /**
     * Call verifyHeaderSection with the the proper resource id's for Right or Left foot
     *
     * @param footSide The FootSide to check (Right/Left)
     * @param enabled  Whether that section is enabled or disabled
     * @param activity The activity object, used to read a couple color resources
     */
    private void verifyFootHeaderSection(FootSide footSide, boolean enabled, TestFragmentActivity activity) {

        if (footSide == FootSide.LEFT) {
            verifyHeaderSection(enabled,
                    R.id.measurement_summary_fragment_left_header,
                    R.id.measurement_summary_fragment_left_wedge_graphic,
                    R.id.measurement_summary_fragment_left_not_measured,
                    R.id.measurement_summary_fragment_left_angle,
                    ContextCompat.getColor(activity.getApplicationContext(), R.color.enabledText),
                    ContextCompat.getColor(activity.getApplicationContext(), R.color.disabledText));
        } else {
            verifyHeaderSection(enabled,
                    R.id.measurement_summary_fragment_right_header,
                    R.id.measurement_summary_fragment_right_wedge_graphic,
                    R.id.measurement_summary_fragment_right_not_measured,
                    R.id.measurement_summary_fragment_right_angle,
                    ContextCompat.getColor(activity.getApplicationContext(), R.color.enabledText),
                    ContextCompat.getColor(activity.getApplicationContext(), R.color.disabledText));
        }
    }

    /**
     * Verify a Header Section for the given resource id's
     *
     * @param enabled           Whether or not this section is enabled or disabled
     * @param headerId          The resource id for the Header label
     * @param wedgeId           The resource id for the Wedge widget
     * @param notMeasuredId     The resource id for the not-measured label
     * @param angleLabelId      The resource id for the angle label
     * @param enabledTextColor  The resource id for the color for enabled text
     * @param disabledTextColor The resource id for the color for disabled text
     */
    private void verifyHeaderSection(boolean enabled, int headerId, int wedgeId, int notMeasuredId, int angleLabelId, @ColorInt int enabledTextColor, @ColorInt int disabledTextColor) {

        int textColor = enabled ? enabledTextColor : disabledTextColor;

        // header label is displayed with correct color
        onView(withId(headerId)).check(matches(isDisplayed()));
        onView(withId(headerId)).check(matches(withTextColor(textColor)));

        // Enabled = display widget, hide not-measured label; False = opposite
        if (enabled) {
            // THEN angle Widget is displayed
            onView(withId(wedgeId)).check(matches(isDisplayed()));

            // THEN Not-Measured label is NOT displayed
            onView(withId(notMeasuredId)).check(matches(not(isDisplayed())));
        } else {
            // THEN Not-Measured label is displayed
            onView(withId(notMeasuredId)).check(matches(isDisplayed()));
            onView(withId(notMeasuredId)).check(matches(withTextColor(textColor)));

            // THEN Angle Widget is NOT displayed
            onView(withId(wedgeId)).check(matches(not(isDisplayed())));
        }

        // THEN Angle label is displayed with correct color
        onView(withId(angleLabelId)).check(matches(isDisplayed()));
        onView(withId(angleLabelId)).check(matches(withTextColor(textColor)));
    }

    private void verifyOneFootButtons() {
        // THEN OK buttons are displayed
        onView(withId(R.id.measurement_summary_fragment_ok_button)).check(matches(isDisplayed()));

        // THEN other buttons not displayed
        onView(withId(R.id.measurement_summary_fragment_professional_button)).check(matches(not(isDisplayed())));
        onView(withId(R.id.measurement_summary_fragment_purchase_button)).check(matches(not(isDisplayed())));
    }

    private void verifyBothFeetButtons() {
        // THEN 3 buttons are displayed
        onView(withId(R.id.measurement_summary_fragment_professional_button)).check(matches(isDisplayed()));
        onView(withId(R.id.measurement_summary_fragment_purchase_button)).check(matches(isDisplayed()));

        // THEN ok button is not displayed
        onView(withId(R.id.measurement_summary_fragment_ok_button)).check(matches(not(isDisplayed())));
    }

    private void verifyNoFeetButtons() {
        // THEN no buttons not displayed
        onView(withId(R.id.measurement_summary_fragment_ok_button)).check(matches(not(isDisplayed())));
        onView(withId(R.id.measurement_summary_fragment_professional_button)).check(matches(not(isDisplayed())));
        onView(withId(R.id.measurement_summary_fragment_purchase_button)).check(matches(not(isDisplayed())));
    }

    //endregion

    //region STATIC METHODS -----------------------------------------------------------------------

    private static Matcher<View> withTextColor(final @ColorInt int color) {
        //Checks.checkNotNull(color);
        return new BoundedMatcher<View, TextView>(TextView.class) {
            @Override
            public boolean matchesSafely(TextView textView) {
                return color == textView.getCurrentTextColor();
            }

            @Override
            public void describeTo(Description description) {
                description.appendText("with text color: ");
            }
        };
    }

    //endregion

}