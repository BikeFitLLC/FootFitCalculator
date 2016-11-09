package com.bikefit.wedgecalculator.camera;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bikefit.wedgecalculator.BikeFitApplication;
import com.bikefit.wedgecalculator.R;
import com.squareup.leakcanary.RefWatcher;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Show summary of a measurement: left and/or right
 */
public class MeasurementSummaryFragment extends Fragment {

    //region INJECTED CLASSES ----------------------------------------------------------------------
    //endregion

    //region INJECTED VIEWS ------------------------------------------------------------------------

    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    // start: Left Foot Views
    @BindView(R.id.measurement_summary_fragment_left_header)
    TextView mLeftFootHeaderLabel;

    @BindView(R.id.measurement_summary_fragment_left_not_measured)
    TextView mLeftFootNotMeasuredLabel;

    @BindView(R.id.measurement_summary_fragment_left_wedge_graphic)
    ImageView mLeftFootWedgeGraphic;

    @BindView(R.id.measurement_summary_fragment_left_angle)
    TextView mLeftFootAngleLabel;
    // end: Left Foot Views

    // start: Right Foot Views
    @BindView(R.id.measurement_summary_fragment_right_header)
    TextView mRightFootHeaderLabel;

    @BindView(R.id.measurement_summary_fragment_right_not_measured)
    TextView mRightFootNotMeasuredLabel;

    @BindView(R.id.measurement_summary_fragment_right_wedge_graphic)
    ImageView mRightFootWedgeGraphic;

    @BindView(R.id.measurement_summary_fragment_right_angle)
    TextView mRightFootAngleLabel;
    // end: Right Foot Views

    @BindView(R.id.measurement_summary_fragment_instruction_text)
    TextView mInstructionText;

    @BindView(R.id.measurement_summary_fragment_full_fitting_button)
    Button mFullFittingButton;

    @BindView(R.id.measurement_summary_fragment_professional_button)
    Button mProfessionalButton;

    @BindView(R.id.measurement_summary_fragment_ok_button)
    Button mMainButton;


    //endregion

    //region PUBLIC INTERFACES ---------------------------------------------------------------------
    //endregion

    //region STATIC LOCAL CONSTANTS ----------------------------------------------------------------
    //endregion

    //region CLASS VARIABLES -----------------------------------------------------------------------

    private Unbinder mViewUnBinder;

    private Float mLeftAngle = null;
    private Integer mLeftWedgeCount = 3;

    private Float mRightAngle = 14f;
    private Integer mRightWedgeCount = 3;

    //endregion

    //region CONSTRUCTOR ---------------------------------------------------------------------------

    public static MeasurementSummaryFragment newInstance() {
        Bundle args = new Bundle();

        MeasurementSummaryFragment fragment = new MeasurementSummaryFragment();
        fragment.setArguments(args);

        return fragment;
    }

    //endregion

    //region LIFECYCLE METHODS ---------------------------------------------------------------------

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.measurement_summary_fragment, container, false);
        mViewUnBinder = ButterKnife.bind(this, view);

        String title = getString(R.string.measurement_summary_fragment_title_label);
        if (mLeftAngle == null || mRightAngle == null) {
            String foot = mLeftAngle != null ? "Left" : "Right";
            title = getString(R.string.measurement_summary_fragment_title_onefoot_label, foot);
        }
        mToolbar.setTitle(title);

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //todo: get stored left & right angles out of preferences

        refreshViewState();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mViewUnBinder.unbind();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        RefWatcher refWatcher = BikeFitApplication.getRefWatcher(getActivity());
        refWatcher.watch(this);
    }

    //endregion


    //region WIDGET --------------------------------------------------------------------------------
    //endregion

    //region LISTENERS -----------------------------------------------------------------------------

    @OnClick(R.id.toolbar)
    public void onToolbarBackPressed() {
        getActivity().onBackPressed();
    }

    //endregion

    //region ACCESSORS -----------------------------------------------------------------------------
    //endregion

    //region TEST ACCESSORS ------------------------------------------------------------------------
    //endregion

    //region PUBLIC CLASS METHODS ------------------------------------------------------------------
    //endregion

    //-- Optional! --
    //region INTERFACE METHODS (InterfaceName) -----------------------------------------------------
    //endregion

    //region PRIVATE METHODS -----------------------------------------------------------------------

    private void refreshViewState() {

        final int disabledWhite = Color.parseColor("#77ffffff");
        final int enabledWhite = Color.WHITE;

        //Setup the Left Angle views
        if (mLeftAngle == null) {
            mLeftFootNotMeasuredLabel.setVisibility(View.VISIBLE);
            mLeftFootWedgeGraphic.setVisibility(View.INVISIBLE);

            mLeftFootHeaderLabel.setTextColor(disabledWhite);
            mLeftFootNotMeasuredLabel.setTextColor(disabledWhite);
            mLeftFootAngleLabel.setTextColor(disabledWhite);

            mLeftFootAngleLabel.setText(getString(R.string.measurement_summary_fragment_unknown_label));
        } else {
            mLeftFootNotMeasuredLabel.setVisibility(View.INVISIBLE);
            mLeftFootWedgeGraphic.setVisibility(View.VISIBLE);

            //todo: clean this up
            mLeftFootWedgeGraphic.setImageLevel(3);

            mLeftFootHeaderLabel.setTextColor(enabledWhite);
            mLeftFootNotMeasuredLabel.setTextColor(enabledWhite);
            mLeftFootAngleLabel.setTextColor(enabledWhite);

            mLeftFootAngleLabel.setText(getString(R.string.measurement_summary_fragment_angle_label, mLeftAngle));
        }

        //Setup the Right Angle views
        if (mRightAngle == null) {
            mRightFootNotMeasuredLabel.setVisibility(View.VISIBLE);
            mRightFootWedgeGraphic.setVisibility(View.INVISIBLE);

            mRightFootHeaderLabel.setTextColor(disabledWhite);
            mRightFootNotMeasuredLabel.setTextColor(disabledWhite);
            mRightFootAngleLabel.setTextColor(disabledWhite);

            mRightFootAngleLabel.setText(getString(R.string.measurement_summary_fragment_unknown_label));
        } else {
            mRightFootNotMeasuredLabel.setVisibility(View.INVISIBLE);
            mRightFootWedgeGraphic.setVisibility(View.VISIBLE);

            //todo: clean this up
            mRightFootWedgeGraphic.setImageLevel(3);

            mRightFootHeaderLabel.setTextColor(enabledWhite);
            mRightFootNotMeasuredLabel.setTextColor(enabledWhite);
            mRightFootAngleLabel.setTextColor(enabledWhite);

            mRightFootAngleLabel.setText(getString(R.string.measurement_summary_fragment_angle_label, mRightAngle));
        }

        //Setup layout views according to information we have
        if (mLeftAngle != null && mRightAngle != null) {

            int totalWedgeCount = mLeftWedgeCount + mRightWedgeCount;
            mInstructionText.setText(getString(R.string.measurement_summary_fragment_complete_instruction_text, totalWedgeCount));

            mFullFittingButton.setVisibility(View.VISIBLE);
            mProfessionalButton.setVisibility(View.VISIBLE);

        } else {

            if (mLeftAngle != null) {
                mInstructionText.setText(getString(R.string.measurement_summary_fragment_incomplete_instruction_text, "Left", "Right"));
            } else {
                mInstructionText.setText(getString(R.string.measurement_summary_fragment_incomplete_instruction_text, "Right", "Left"));
            }

            mFullFittingButton.setVisibility(View.GONE);
            mProfessionalButton.setVisibility(View.GONE);

        }
    }

    //endregion

    //region OBSERVERS -----------------------------------------------------------------------------
    //endregion

    //region INNER CLASSES -------------------------------------------------------------------------
    //endregion

}
