package com.bikefit.wedgecalculator.welcome;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.bikefit.wedgecalculator.R;
import com.bikefit.wedgecalculator.main.MainMenuActivity;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Activity for the Wedging Benefits screen
 * This is a navigation endpoint, so it's just an activity
 */
public class WedgingBenefitsActivity extends AppCompatActivity {

    // region LIFECYCLE METHODS --------------------------------------------------------------------

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wedging_benefits);
        ButterKnife.bind(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar supportActionBar = getSupportActionBar();
        if (supportActionBar != null) {
            supportActionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    // endregion

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            super.onBackPressed();
        }
        return true;
    }

    // region LISTENER METHODS ---------------------------------------------------------------------

    @OnClick(R.id.activity_wedging_benefits_start_button)
    public void onGetStartedButton() {
        startActivity(new Intent(WedgingBenefitsActivity.this, MainMenuActivity.class));
        finish();
    }

    // endregion
}
