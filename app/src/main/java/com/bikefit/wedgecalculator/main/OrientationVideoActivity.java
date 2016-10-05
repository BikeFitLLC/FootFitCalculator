package com.bikefit.wedgecalculator.main;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.bikefit.wedgecalculator.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Activity for Orientation Video, which just loads OrientationVideoFragment
 */
public class OrientationVideoActivity extends AppCompatActivity {

    //region CLASS VARIABLES -----------------------------------------------------------------------

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    //endregion

    //region CONSTRUCTOR ---------------------------------------------------------------------------

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.orientation_video_activity);
        ButterKnife.bind(this);

        toolbar.setTitle("");
        toolbar.setNavigationOnClickListener(toolbarBackButtonListener);

        if (savedInstanceState == null) {
            String url = getResources().getString(R.string.orientation_video_url);
            OrientationVideoFragment fragment = OrientationVideoFragment.newInstance(url);
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.main_menu_activity_fragment, fragment);
            fragmentTransaction.commit();
        }

    }

    //endregion

    //region ACCESSORS -----------------------------------------------------------------------------
    //endregion

    //region PUBLIC CLASS METHODS ------------------------------------------------------------------
    //endregion

    //region PRIVATE METHODS -----------------------------------------------------------------------
    //endregion

    //region INNER CLASSES -------------------------------------------------------------------------

    //endregion

    //region LISTENERS -----------------------------------------------------------------------------

    private View.OnClickListener toolbarBackButtonListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            onBackPressed();
        }
    };

    //endregion

}
