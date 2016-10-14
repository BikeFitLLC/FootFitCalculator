package com.bikefit.wedgecalculator.main;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.bikefit.wedgecalculator.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Activity that represents the main list of steps to achieve a result (a measurement) in app
 */
public class MainMenuActivity extends AppCompatActivity {

    //region CLASS VARIABLES -----------------------------------------------------------------------

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    //endregion

    //region CONSTRUCTOR ---------------------------------------------------------------------------
    //endregion

    //region LIFECYCLE METHODS ---------------------------------------------------------------------
    private View.OnClickListener toolbarBackButtonListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            onBackPressed();
        }
    };

    //endregion

    //region ACCESSORS -----------------------------------------------------------------------------
    //endregion

    //region PUBLIC CLASS METHODS ------------------------------------------------------------------

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_menu_activity);
        ButterKnife.bind(this);

        toolbar.setNavigationOnClickListener(toolbarBackButtonListener);

        if (savedInstanceState == null) {
            MainMenuFragment fragment = MainMenuFragment.newInstance();
            showFragment(fragment, false);
        }
    }

    //endregion

    //region PRIVATE METHODS -----------------------------------------------------------------------
    //endregion

    //region LISTENERS -----------------------------------------------------------------------------

    public void showFragment(Fragment fragment, boolean addToBackstack) {
        FragmentTransaction fragmentTransaction = this.getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.main_menu_activity_fragment, fragment);
        if (addToBackstack) {
            fragmentTransaction.addToBackStack(null);
        }
        fragmentTransaction.commit();
    }

    //endregion

}
