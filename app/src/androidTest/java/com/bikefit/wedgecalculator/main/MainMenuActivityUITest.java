package com.bikefit.wedgecalculator.main;

import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.action.ViewActions;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.bikefit.wedgecalculator.R;

import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

/**
 * UI Test for the Main Menu Activity, which has some elements in the MainMenuFragment
 */
@RunWith(AndroidJUnit4.class)
public class MainMenuActivityUITest {

    //region CLASS UNDER TEST ----------------------------------------------------------------------

    @Rule
    public ActivityTestRule<MainMenuActivity> mActivityRule = new ActivityTestRule<>(MainMenuActivity.class, false, false);

    //endregion

    //region MOCKS ---------------------------------------------------------------------------------
    //endregion

    //region NON-MOCKS -----------------------------------------------------------------------------
    //endregion

    //region SETUP ---------------------------------------------------------------------------------
    //endregion

    //region TESTS ---------------------------------------------------------------------------------

    @Test
    public void testOptionButtonsAppear() {
        // WHEN the activity starts
        mActivityRule.launchActivity(null);

        // THEN the 7 options appear as buttons
        onView(withId(R.id.main_menu_fragment_orientation_video_button)).check(matches(isDisplayed()));
        onView(withId(R.id.main_menu_fragment_what_you_need_button)).check(matches(isDisplayed()));
        onView(withId(R.id.main_menu_fragment_get_in_position_button)).check(matches(isDisplayed()));
        onView(withId(R.id.main_menu_fragment_measure_your_feet_button)).check(matches(isDisplayed()));
        onView(withId(R.id.main_menu_fragment_get_results_button)).perform(ViewActions.scrollTo()).check(matches(isDisplayed()));

        onView(withId(R.id.main_menu_fragment_start_button)).check(matches(isDisplayed()));
    }

    @Test
    public void testOrientationVideoButton() {

        //WHEN the activity starts
        mActivityRule.launchActivity(null);

        //THEN check the orientation video button appears
        onView(withId(R.id.main_menu_fragment_orientation_video_button)).check(matches(isDisplayed()));

        //WHEN the button is clicked
        onView(withId(R.id.main_menu_fragment_orientation_video_button)).perform(click());

        //FIRST wait for screen to load
        InstrumentationRegistry.getInstrumentation().waitForIdleSync();

        //THEN check for test on the resulting screen
        onView(withId(R.id.toolbar)).check(matches(isDisplayed()));

    }

    @Ignore
    @Test
    public void testWhatYouNeedButton() {

        //WHEN the activity starts
        mActivityRule.launchActivity(null);

        //THEN check the orientation video button appears
        onView(withId(R.id.main_menu_fragment_what_you_need_button)).check(matches(isDisplayed()));

        //WHEN the button is clicked
        onView(withId(R.id.main_menu_fragment_what_you_need_button)).perform(click());

        //FIRST wait for screen to load
        InstrumentationRegistry.getInstrumentation().waitForIdleSync();

        //THEN check for test on the resulting screen
        onView(withId(R.id.toolbar)).check(matches(isDisplayed()));

    }

    @Ignore
    @Test
    public void testGetInPositionButton() {

        //WHEN the activity starts
        mActivityRule.launchActivity(null);

        //THEN check the orientation video button appears
        onView(withId(R.id.main_menu_fragment_get_in_position_button)).check(matches(isDisplayed()));

        //WHEN the button is clicked
        onView(withId(R.id.main_menu_fragment_get_in_position_button)).perform(click());

        //FIRST wait for screen to load
        InstrumentationRegistry.getInstrumentation().waitForIdleSync();

        //THEN check for test on the resulting screen
        onView(withId(R.id.toolbar)).check(matches(isDisplayed()));

    }

    @Ignore
    @Test
    public void testMeasureYourFeetButton() {

        //WHEN the activity starts
        mActivityRule.launchActivity(null);

        //THEN check the orientation video button appears
        onView(withId(R.id.main_menu_fragment_measure_your_feet_button)).check(matches(isDisplayed()));

        //WHEN the button is clicked
        onView(withId(R.id.main_menu_fragment_measure_your_feet_button)).perform(click());

        //FIRST wait for screen to load
        InstrumentationRegistry.getInstrumentation().waitForIdleSync();

        //THEN check for test on the resulting screen
        onView(withId(R.id.toolbar)).check(matches(isDisplayed()));

    }

    @Ignore
    @Test
    public void testMeasureGetResultsButton() {

        //WHEN the activity starts
        mActivityRule.launchActivity(null);

        //THEN check the orientation video button appears
        onView(withId(R.id.main_menu_fragment_get_results_button)).check(matches(isDisplayed()));

        //WHEN the button is clicked
        onView(withId(R.id.main_menu_fragment_get_results_button)).perform(click());

        //FIRST wait for screen to load
        InstrumentationRegistry.getInstrumentation().waitForIdleSync();

        //THEN check for test on the resulting screen
        onView(withId(R.id.toolbar)).check(matches(isDisplayed()));

    }

    @Ignore
    @Test
    public void testStartButton() {

        //WHEN the activity starts
        mActivityRule.launchActivity(null);

        //THEN check the orientation video button appears
        onView(withId(R.id.main_menu_fragment_start_button)).check(matches(isDisplayed()));

        //WHEN the button is clicked
        onView(withId(R.id.main_menu_fragment_start_button)).perform(click());

        //FIRST wait for screen to load
        InstrumentationRegistry.getInstrumentation().waitForIdleSync();

        //THEN check for test on the resulting screen
        onView(withId(R.id.toolbar)).check(matches(isDisplayed()));

    }

    //endregion

    //region PRIVATE METHODS -----------------------------------------------------------------------
    //endregion
}