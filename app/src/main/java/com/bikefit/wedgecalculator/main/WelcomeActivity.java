package com.bikefit.wedgecalculator.main;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.bikefit.wedgecalculator.R;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class WelcomeActivity extends AppCompatActivity {

    public static final String TAG = "WelcomeActivity";

    // region LIFECYCLE METHODS --------------------------------------------------------------------

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        ButterKnife.bind(this);
    }

    // endregion

    // region WIDGETS ------------------------------------------------------------------------------


    // endregion

    // region LISTENER METHODS ---------------------------------------------------------------------

    @OnClick(R.id.activity_welcome_more_button)
    public void onMoreInformationButton() {
        startActivity(new Intent(WelcomeActivity.this, WedgingBenefitsActivity.class));
    }

    @OnClick(R.id.activity_welcome_orientation_button)
    public void onOrientationVideoButton() {
        Log.i(TAG, "orientation video button pressed");
    }

    @OnClick(R.id.activity_welcome_start_button)
    public void onStartButton() {
        Log.i(TAG, "Start button pressed");
    }

    // endregion


}
