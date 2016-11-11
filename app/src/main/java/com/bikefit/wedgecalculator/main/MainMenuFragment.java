package com.bikefit.wedgecalculator.main;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.bikefit.wedgecalculator.R;
import com.bikefit.wedgecalculator.measure.CameraInstructionsFragment;
import com.bikefit.wedgecalculator.measure.MeasurementSummaryFragment;
import com.bikefit.wedgecalculator.measure.model.FootSide;
import com.bikefit.wedgecalculator.measure.model.MeasureModel;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;


/**
 * Fragment that contains layout of the main menu of application.
 */
public class MainMenuFragment extends Fragment {

    //region CLASS VARIABLES -----------------------------------------------------------------------

    private Unbinder mViewUnBinder;

    //endregion

    //region INJECTED VIEWS ------------------------------------------------------------------------

    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    @BindView(R.id.main_menu_fragment_get_results_button)
    Button mResultsButton;

    @BindView(R.id.main_menu_fragment_what_you_need_button)
    Button mWhatYouNeedButton;

    @BindView(R.id.main_menu_fragment_get_in_position_button)
    Button mGetInPositionButton;

    @BindView(R.id.main_menu_fragment_start_button)
    Button mStartButton;


    //endregion

    //region CONSTRUCTOR ---------------------------------------------------------------------------

    public static MainMenuFragment newInstance() {
        MainMenuFragment fragment = new MainMenuFragment();
        return fragment;
    }

    //endregion

    //region LIFECYCLE METHODS ---------------------------------------------------------------------

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.main_menu_fragment, container, false);
        mViewUnBinder = ButterKnife.bind(this, view);

        mToolbar.setTitle(getResources().getString(R.string.main_menu_fragment_title));

        mWhatYouNeedButton.setEnabled(false);
        mGetInPositionButton.setEnabled(false);
        mResultsButton.setEnabled(MeasureModel.areBothFeetMeasured());

        mStartButton.setEnabled(false);

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mViewUnBinder.unbind();
    }

    //endregion

    //region ACCESSORS -----------------------------------------------------------------------------
    //endregion

    //region PUBLIC CLASS METHODS ------------------------------------------------------------------

    @OnClick(R.id.main_menu_fragment_orientation_video_button)
    public void onOrientationVideoButton() {

        OrientationVideoFragment fragment = new OrientationVideoFragment().newInstance("");
        ((MainMenuActivity) getActivity()).showFragment(fragment, true);

    }

    @OnClick(R.id.main_menu_fragment_measure_your_feet_button)
    public void onMeasureLeftFootButton() {

        CameraInstructionsFragment fragment = new CameraInstructionsFragment().newInstance(FootSide.LEFT);
        ((MainMenuActivity) getActivity()).showFragment(fragment, true);

    }

    //endregion

    //region PRIVATE METHODS -----------------------------------------------------------------------
    //endregion

    //region LISTENERS -----------------------------------------------------------------------------

    @OnClick(R.id.toolbar)
    public void onToolbarBackPressed() {
        getActivity().onBackPressed();
    }

    @OnClick(R.id.main_menu_fragment_get_results_button)
    public void onResultsClicked() {
        MeasurementSummaryFragment fragment = MeasurementSummaryFragment.newInstance();
        ((MainMenuActivity) getActivity()).showFragment(fragment, true);
    }

    //endregion


}
