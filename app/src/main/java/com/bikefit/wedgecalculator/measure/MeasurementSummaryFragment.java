package com.bikefit.wedgecalculator.measure;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bikefit.wedgecalculator.BikeFitApplication;
import com.bikefit.wedgecalculator.R;
import com.bikefit.wedgecalculator.main.MainMenuActivity;
import com.bikefit.wedgecalculator.measure.model.FootSide;
import com.bikefit.wedgecalculator.measure.model.MeasureModel;
import com.bikefit.wedgecalculator.settings.AnalyticsTracker;
import com.bikefit.wedgecalculator.settings.InternetUtil;
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

    @BindView(R.id.measurement_summary_fragment_professional_button)
    Button mProfessionalButton;

    @BindView(R.id.measurement_summary_fragment_purchase_button)
    Button mPurchaseButton;

    @BindView(R.id.measurement_summary_fragment_ok_button)
    Button mOkButton;


    //endregion

    //region PUBLIC INTERFACES ---------------------------------------------------------------------
    //endregion

    //region STATIC LOCAL CONSTANTS ----------------------------------------------------------------

    //endregion

    //region CLASS VARIABLES -----------------------------------------------------------------------

    private Unbinder mViewUnBinder;

    private Float mLeftAngle;
    private Integer mLeftWedgeCount;
    private Float mRightAngle;
    private Integer mRightWedgeCount;

    InternetUtil mInternetUtil = new InternetUtil();

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

        //grab the MeasureModel items locally
        mLeftAngle = MeasureModel.getAngle(FootSide.LEFT);
        mLeftWedgeCount = MeasureModel.getWedgeCount(FootSide.LEFT);

        mRightAngle = MeasureModel.getAngle(FootSide.RIGHT);
        mRightWedgeCount = MeasureModel.getWedgeCount(FootSide.RIGHT);

        mToolbar.setTitle(getPageTitle(mLeftAngle, mRightAngle));
        mToolbar.setNavigationOnClickListener(mNavigationListener);
        AnalyticsTracker.INSTANCE.sendAnalyticsScreen(getPageTitle(mLeftAngle, mRightAngle));

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        refreshViewState();
    }

    @Override
    public void onDestroyView() {
        mToolbar = null;
        mLeftAngle = null;
        mLeftWedgeCount = null;
        mRightAngle = null;
        mRightWedgeCount = null;
        mInternetUtil = null;

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

    View.OnClickListener mNavigationListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            if (mLeftAngle != null && mRightAngle != null) {
                startActivity(new Intent(getContext(), MainMenuActivity.class));
                getActivity().finish();
            } else {
                getActivity().onBackPressed();
            }
        }
    };

    @OnClick(R.id.measurement_summary_fragment_ok_button)
    public void onOkButton() {
        FootSide newFoot = (mLeftAngle == null) ? FootSide.LEFT : FootSide.RIGHT;

        //Measure the next foot
        CameraInstructionsFragment fragment = CameraInstructionsFragment.newInstance(newFoot);
        ((MainMenuActivity) getActivity()).showFragment(fragment, true);
    }

    @OnClick(R.id.measurement_summary_fragment_professional_button)
    public void onProfessionalButton() {
        String url = getString(R.string.measurement_summary_fragment_professional_fitting_url);
        if (mInternetUtil.checkInternet()) {
            mInternetUtil.openExternalWebPage(url);
        }
    }

    @OnClick(R.id.measurement_summary_fragment_purchase_button)
    public void onPurchaseButton() {
        CleatSelectionFragment fragment = CleatSelectionFragment.newInstance();
        ((MainMenuActivity) getActivity()).showFragment(fragment, true);
    }

    //endregion

    //region ACCESSORS -----------------------------------------------------------------------------
    //endregion

    //region TEST ACCESSORS ------------------------------------------------------------------------
    //endregion

    //region PUBLIC CLASS METHODS ------------------------------------------------------------------
    //endregion

    //region PRIVATE METHODS -----------------------------------------------------------------------

    private String getPageTitle(Float leftAngle, Float rightAngle) {
        String title;
        if (leftAngle != null && rightAngle != null) {
            title = getString(R.string.measurement_summary_fragment_title_label);
        } else if (leftAngle == null && rightAngle == null) {
            title = getString(R.string.measurement_summary_fragment_title_nofeet_label);
        } else {
            String foot = leftAngle != null ? FootSide.LEFT.getLabel() : FootSide.RIGHT.getLabel();
            title = getString(R.string.measurement_summary_fragment_title_onefoot_label, foot);
        }
        return title;
    }

    private void refreshViewState() {

        //Setup the Left Angle views
        if (mLeftAngle == null) {
            setFootNoMeasurement(mLeftFootNotMeasuredLabel, mLeftFootAngleLabel, mLeftFootWedgeGraphic, mLeftFootHeaderLabel);
        } else {
            int imageLevel = MeasureModel.getWedgeImageLevel(mLeftAngle);
            setFootWithMeasurement(mLeftFootNotMeasuredLabel, mLeftFootAngleLabel, mLeftFootWedgeGraphic, mLeftFootHeaderLabel, mLeftAngle, imageLevel);
        }

        //Setup the Right Angle views
        if (mRightAngle == null) {
            setFootNoMeasurement(mRightFootNotMeasuredLabel, mRightFootAngleLabel, mRightFootWedgeGraphic, mRightFootHeaderLabel);
        } else {
            int imageLevel = MeasureModel.getWedgeImageLevel(mRightAngle);
            setFootWithMeasurement(mRightFootNotMeasuredLabel, mRightFootAngleLabel, mRightFootWedgeGraphic, mRightFootHeaderLabel, mRightAngle, imageLevel);
        }

        setInstructionTextAndButtons(mLeftAngle, mRightAngle, mLeftWedgeCount, mRightWedgeCount);
    }

    private void setInstructionTextAndButtons(Float leftAngle, Float rightAngle, Integer leftWedgeCount, Integer rightWedgeCount) {

        if (leftAngle != null && rightAngle != null) {
            int totalWedgeCount = leftWedgeCount + rightWedgeCount;
            mInstructionText.setText(getString(R.string.measurement_summary_fragment_complete_instruction_text, totalWedgeCount));

            mOkButton.setVisibility(View.GONE);
            mProfessionalButton.setVisibility(View.VISIBLE);
            mPurchaseButton.setVisibility(View.VISIBLE);

        } else if (leftAngle == null && rightAngle == null) {
            // If we are here there have been no measurements, display message to user and make them back out of screen (i.e. no buttons)
            mInstructionText.setText(getString(R.string.measurement_summary_fragment_nofeet_measured_instruction_label));

            mOkButton.setVisibility(View.GONE);
            mProfessionalButton.setVisibility(View.GONE);
            mPurchaseButton.setVisibility(View.GONE);

        } else {
            String finishedSide;
            String todoSide;

            if (mRightAngle != null) {
                finishedSide = FootSide.RIGHT.getLabel();
                todoSide = FootSide.LEFT.getLabel();
            } else {
                finishedSide = FootSide.LEFT.getLabel();
                todoSide = FootSide.RIGHT.getLabel();
            }

            mInstructionText.setText(getString(R.string.measurement_summary_fragment_incomplete_instruction_text, finishedSide, todoSide));

            mOkButton.setVisibility(View.VISIBLE);
            mProfessionalButton.setVisibility(View.GONE);
            mPurchaseButton.setVisibility(View.GONE);
        }
    }

    private void setFootWithMeasurement(View notMeasuredLabel, TextView angleLabel, ImageView wedgeGraphic, TextView headerLabel, float angle, int wedgeImageLevel) {

        int enabledColor = ContextCompat.getColor(getContext(), R.color.enabledText);

        // header label
        headerLabel.setTextColor(enabledColor);

        // middle graphic/label (frameLayout)
        notMeasuredLabel.setVisibility(View.INVISIBLE);
        wedgeGraphic.setVisibility(View.VISIBLE);
        wedgeGraphic.setImageLevel(wedgeImageLevel);

        // angle label
        angleLabel.setTextColor(enabledColor);
        angleLabel.setText(getString(R.string.measurement_summary_fragment_angle_label, angle));
    }

    private void setFootNoMeasurement(TextView notMeasuredLabel, TextView angleLabel, View wedgeGraphic, TextView headerLabel) {

        int disabledColor = ContextCompat.getColor(getContext(), R.color.disabledText);

        // header label
        headerLabel.setTextColor(disabledColor);

        // middle graphic/label (frameLayout)
        notMeasuredLabel.setVisibility(View.VISIBLE);
        notMeasuredLabel.setTextColor(disabledColor);
        wedgeGraphic.setVisibility(View.INVISIBLE);

        // angle label
        angleLabel.setTextColor(disabledColor);
        angleLabel.setText(getString(R.string.measurement_summary_fragment_unknown_label));
    }

    //endregion

    //region OBSERVERS -----------------------------------------------------------------------------
    //endregion

    //region INNER CLASSES -------------------------------------------------------------------------
    //endregion

}
