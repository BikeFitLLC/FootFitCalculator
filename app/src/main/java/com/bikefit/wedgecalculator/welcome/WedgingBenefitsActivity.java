package com.bikefit.wedgecalculator.welcome;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.bikefit.wedgecalculator.R;
import com.bikefit.wedgecalculator.main.MainMenuActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Activity for the Wedging Benefits screen
 * This is a navigation endpoint, so it's just an activity
 */
public class WedgingBenefitsActivity extends AppCompatActivity {

    //region CLASS VARIABLES -----------------------------------------------------------------------

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    // endregion

    // region LIFECYCLE METHODS --------------------------------------------------------------------

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wedging_benefits_activity);
        ButterKnife.bind(this);

        toolbar.setNavigationOnClickListener(toolbarBackButtonListener);
    }

    // endregion

    // region LISTENER METHODS ---------------------------------------------------------------------

    @OnClick(R.id.activity_wedging_benefits_start_button)
    public void onGetStartedButton() {
        startActivity(new Intent(WedgingBenefitsActivity.this, MainMenuActivity.class));
        finish();
    }

    // endregion

    //region INNER CLASSES -------------------------------------------------------------------------

    private View.OnClickListener toolbarBackButtonListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            onBackPressed();
        }
    };

    //endregion
}
