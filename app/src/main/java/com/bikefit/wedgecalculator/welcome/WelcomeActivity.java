package com.bikefit.wedgecalculator.welcome;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.bikefit.wedgecalculator.R;
import com.bikefit.wedgecalculator.main.MainMenuActivity;
import com.bikefit.wedgecalculator.main.OrientationVideoActivity;
import com.bikefit.wedgecalculator.settings.AnalyticsTracker;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * The main entry point for the app, after the Splash screen has been displayed.
 */
public class WelcomeActivity extends AppCompatActivity {

    // region LIFECYCLE METHODS --------------------------------------------------------------------

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.welcome_activity);
        ButterKnife.bind(this);

        AnalyticsTracker.INSTANCE.sendAnalyticsScreen(getString(R.string.welcome_activity_title));
    }

    // endregion

    // region WIDGETS ------------------------------------------------------------------------------
    // endregion

    // region LISTENER METHODS ---------------------------------------------------------------------

    @OnClick(R.id.activity_welcome_orientation_button)
    public void onOrientationVideoButton() {
        startActivity(new Intent(WelcomeActivity.this, OrientationVideoActivity.class));
    }

    @OnClick(R.id.activity_welcome_start_button)
    public void onStartButton() {
        startActivity(new Intent(WelcomeActivity.this, MainMenuActivity.class));
    }

    // endregion

}
