package com.bikefit.wedgecalculator.measure;

import android.support.test.InstrumentationRegistry;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.bikefit.wedgecalculator.R;
import com.bikefit.wedgecalculator.main.MainMenuActivity;
import com.bikefit.wedgecalculator.measure.model.FootSide;
import com.bikefit.wedgecalculator.settings.AnalyticsTracker;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

@RunWith(AndroidJUnit4.class)
public class MeasureFeetInstructionsFragmentUITest {

    //region CLASS UNDER TEST ----------------------------------------------------------------------

    @Rule
    public ActivityTestRule<MainMenuActivity> mActivityRule = new ActivityTestRule<>(MainMenuActivity.class, false, false);

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
    public void testButtonsAppear() {

        // GIVEN the Fragment starts
        startActivityWithFragment(FootSide.LEFT);

        // WHEN the screen is displayed

        // THEN the 2 buttons are displayed
        onView(withId(R.id.measure_feet_instructions_fragment_more_button)).check(matches(isDisplayed()));
        onView(withId(R.id.measure_feet_instructions_fragment_snapshot_button)).check(matches(isDisplayed()));
    }

    @Test
    public void testTellMeMoreButton() {

        // GIVEN the Fragment starts
        startActivityWithFragment(FootSide.LEFT);

        // WHEN the screen is displayed

        // THEN check the "Tell me more" button appears
        onView(withId(R.id.measure_feet_instructions_fragment_more_button)).check(matches(isDisplayed()));

        // WHEN the button is clicked
        onView(withId(R.id.measure_feet_instructions_fragment_more_button)).perform(click());

        // FIRST wait for screen to load
        InstrumentationRegistry.getInstrumentation().waitForIdleSync();

        // THEN check for test on the resulting screen
        onView(withId(R.id.measurement_feet_instructions_help_dialog_fragment_button)).check(matches(isDisplayed()));
    }

    @Test
    public void testSnapshotButtonLeft() {
        testFootScreenBehavior(FootSide.LEFT);
    }

    @Test
    public void testSnapshotButtonRight() {
        testFootScreenBehavior(FootSide.RIGHT);
    }

    //endregion

    //region PRIVATE METHODS -----------------------------------------------------------------------

    private MainMenuActivity startActivityWithFragment(FootSide footSide) {
        MainMenuActivity activity = mActivityRule.launchActivity(null);
        final MeasureFeetInstructionsFragment fragment = MeasureFeetInstructionsFragment.newInstance(footSide);
        activity.showFragment(fragment, true);
        return activity;
    }

    private void testFootScreenBehavior(FootSide footSide) {

        // GIVEN the Fragment starts for the given foot
        MainMenuActivity activity = startActivityWithFragment(footSide);

        // THEN check the "Measure left foot" button appears
        onView(withId(R.id.measure_feet_instructions_fragment_snapshot_button)).check(matches(isDisplayed()));

        // THEN I see the expected text in the button
        String expectedButtonText = activity.getResources().getString(R.string.measure_feet_instructions_fragment_snapshot_button_text, footSide.getLabel().toLowerCase());
        onView(withText(expectedButtonText)).check(matches(isDisplayed()));

        // WHEN the button is clicked
        onView(withId(R.id.measure_feet_instructions_fragment_snapshot_button)).perform(click());

        // FIRST wait for screen to load
        InstrumentationRegistry.getInstrumentation().waitForIdleSync();

        // THEN check for test on the resulting screen
        onView(withId(R.id.mcam_fragment_videocapture_camera_instructions)).check(matches(isDisplayed()));
    }

    //endregion

}