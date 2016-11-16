package com.bikefit.wedgecalculator.measure;

import android.support.test.InstrumentationRegistry;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.bikefit.wedgecalculator.R;
import com.bikefit.wedgecalculator.measure.model.FootSide;
import com.bikefit.wedgecalculator.test.TestFragmentActivity;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

@RunWith(AndroidJUnit4.class)
public class CameraInstructionsFragmentUITest {

    //region CLASS UNDER TEST ----------------------------------------------------------------------

    @Rule
    public ActivityTestRule<TestFragmentActivity> mActivityRule = new ActivityTestRule<>(
            TestFragmentActivity.class, false, false);

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
    public void testButtonsAppear() {

        // WHEN the Activity with Fragment starts
        startActivityWithFragment();

        // WHEN the fragment is displayed

        // THEN the 1 options appear as buttons
        //onView(withId(R.id.camera_instructions_fragment_more_button)).check(matches(isDisplayed()));
        onView(withId(R.id.camera_instructions_fragment_snapshot_button)).check(matches(isDisplayed()));
    }

    @Ignore
    @Test
    public void testTellMeMoreButton() {

        // WHEN the Activity with Fragment starts
        startActivityWithFragment();

        //THEN check the orientation video button appears
        onView(withId(R.id.camera_instructions_fragment_more_button)).check(matches(isDisplayed()));

        //WHEN the button is clicked
        onView(withId(R.id.camera_instructions_fragment_more_button)).perform(click());

        //FIRST wait for screen to load
        InstrumentationRegistry.getInstrumentation().waitForIdleSync();

        //THEN check for test on the resulting screen
        onView(withId(R.id.toolbar)).check(matches(isDisplayed()));

    }

    @Ignore
    @Test
    public void testTakeASnapshotButton() {

        //todo find a way to test the camera opens

        // WHEN the Activity with Fragment starts
        startActivityWithFragment();

        //THEN check the orientation video button appears
        onView(withId(R.id.camera_instructions_fragment_snapshot_button)).check(matches(isDisplayed()));

        //WHEN the button is clicked
        onView(withId(R.id.camera_instructions_fragment_snapshot_button)).perform(click());

        //FIRST wait for screen to load
        InstrumentationRegistry.getInstrumentation().waitForIdleSync();

        //THEN check for test on the resulting screen
        //onView(withText("Center the red line")).check(matches(isDisplayed()));


    }
    //endregion

    //region PRIVATE METHODS -----------------------------------------------------------------------

    private void startActivityWithFragment() {
        TestFragmentActivity activity = mActivityRule.launchActivity(null);
        final CameraInstructionsFragment fragment = CameraInstructionsFragment.newInstance(FootSide.LEFT);
        activity.transactToFragment(fragment);
    }

    //endregion

}