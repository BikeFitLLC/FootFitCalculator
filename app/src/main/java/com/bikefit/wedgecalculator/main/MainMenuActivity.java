package com.bikefit.wedgecalculator.main;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import com.bikefit.wedgecalculator.R;

import butterknife.ButterKnife;

/**
 * Activity that represents the main list of steps to achieve a result (a measurement) in app
 */
public class MainMenuActivity extends AppCompatActivity {

    //region CLASS VARIABLES -----------------------------------------------------------------------
    //endregion

    //region CONSTRUCTOR ---------------------------------------------------------------------------
    //endregion

    //region LIFECYCLE METHODS ---------------------------------------------------------------------

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_menu_activity);
        ButterKnife.bind(this);

        if (savedInstanceState == null) {
            showMainMenuFragment();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.enter_from_left, R.anim.exit_to_right);
    }

    //endregion

    //region ACCESSORS -----------------------------------------------------------------------------
    //endregion

    //region PUBLIC CLASS METHODS ------------------------------------------------------------------

    public void showFragment(Fragment fragment, boolean addToBackstack) {
        FragmentTransaction fragmentTransaction = this.getSupportFragmentManager().beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_left, R.anim.exit_to_right);
        fragmentTransaction.replace(R.id.main_menu_activity_fragment, fragment);
        if (addToBackstack) {
            fragmentTransaction.addToBackStack(null);
        }
        fragmentTransaction.commit();
    }

    public void clearBackStack() {
        while (getSupportFragmentManager().getBackStackEntryCount() > 0) {
            getSupportFragmentManager().popBackStack();
            getSupportFragmentManager().executePendingTransactions();
        }
    }

    //endregion

    //region PRIVATE METHODS -----------------------------------------------------------------------

    // Show initial MainMenuFragment, no back stack, and avoid the animation (already animated from previous activity)
    private void showMainMenuFragment() {
        MainMenuFragment fragment = MainMenuFragment.newInstance();
        FragmentTransaction fragmentTransaction = this.getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.main_menu_activity_fragment, fragment);
        fragmentTransaction.commit();
    }

    //endregion

    //region LISTENERS -----------------------------------------------------------------------------
    //endregion

}
