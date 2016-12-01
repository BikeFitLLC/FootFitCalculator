package com.bikefit.wedgecalculator.main;

import android.app.Instrumentation;
import android.support.test.InstrumentationRegistry;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.bikefit.wedgecalculator.R;
import com.bikefit.wedgecalculator.settings.AnalyticsTracker;
import com.bikefit.wedgecalculator.welcome.WelcomeActivity;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
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
    public ActivityTestRule<WelcomeActivity> mActivityRule = new ActivityTestRule<>(WelcomeActivity.class, false, false);

    //endregion

    //region MOCKS ---------------------------------------------------------------------------------
    //endregion

    //region NON-MOCKS -----------------------------------------------------------------------------
    //endregion

    //region SETUP ---------------------------------------------------------------------------------

    @Before
    public void setUp() throws Exception {
        AnalyticsTracker.INSTANCE.setDryRun(true);
    }

    //endregion

    //region TESTS ---------------------------------------------------------------------------------

    @Test
    public void testButtonsAppear() {
        // WHEN the activity starts
        mActivityRule.launchActivity(null);

        // THEN the 2 options appear as buttons
        onView(withId(R.id.activity_welcome_orientation_button)).check(matches(isDisplayed()));
        onView(withId(R.id.activity_welcome_start_button)).check(matches(isDisplayed()));
    }

    @Test
    public void testLetsGetStartedButton() {

        // WHEN the activity starts
        mActivityRule.launchActivity(null);

        // THEN check the button appears
        onView(withId(R.id.activity_welcome_start_button)).check(matches(isDisplayed()));

        // WHEN the button is clicked
        onView(withId(R.id.activity_welcome_start_button)).perform(click());

        // THEN check for text on resulting activity
        Instrumentation instrumentation = InstrumentationRegistry.getInstrumentation();
        instrumentation.waitForIdleSync();

        onView(withId(R.id.main_menu_fragment_orientation_video_button)).check(matches(isDisplayed()));
    }

    @Test
    public void testWatchOrientationVideoButton() {

        // WHEN the activity starts
        mActivityRule.launchActivity(null);

        // THEN check the button appears
        onView(withId(R.id.activity_welcome_orientation_button)).check(matches(isDisplayed()));

        // WHEN the button is clicked
        onView(withId(R.id.activity_welcome_orientation_button)).perform(click());

        // THEN check for text on resulting activity
        Instrumentation instrumentation = InstrumentationRegistry.getInstrumentation();
        instrumentation.waitForIdleSync();

        onView(withId(R.id.orientation_video_fragment_webview)).check(matches(isDisplayed()));
    }

    //endregion

    //region PRIVATE METHODS -----------------------------------------------------------------------
    //endregion

}