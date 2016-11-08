package com.bikefit.wedgecalculator.camera;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.TextView;

import com.bikefit.wedgecalculator.BikeFitApplication;
import com.bikefit.wedgecalculator.R;
import com.bikefit.wedgecalculator.view.FootSide;
import com.bikefit.wedgecalculator.view.MeasureWidget;
import com.squareup.leakcanary.RefWatcher;

import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class MeasurementFragment extends Fragment {

    //region STATIC LOCAL CONSTANTS ----------------------------------------------------------------

    private static final String DIALOG_DISPLAYED_KEY = "DIALOG_DISPLAYED_KEY";
    private static final String FILE_PATH_KEY = "FILE_PATH_KEY";

    //endregion

    //region INJECTED VIEWS ------------------------------------------------------------------------

    @BindView(R.id.measurement_fragment_foot_image)
    ImageView mFootImage;

    @BindView(R.id.measurement_fragment_measure_widget)
    MeasureWidget mMeasureWidget;

    @BindView(R.id.measurement_fragment_wedge_graphic)
    ImageView mWedgeGraphic;

    @BindView(R.id.measurement_fragment_foot_display_angle)
    TextView mAngleDisplay;

    //endregion

    //region CLASS VARIABLES -----------------------------------------------------------------------

    private FootSide mFootSide = FootSide.LEFT;
    private MeasurementInstructionsDialogFragment mInstructionsDialog;
    private Unbinder mViewUnbinder;
    private boolean mDialogDisplayed = false;

    private float mAngle;

    //endregion

    //region CONSTRUCTOR ---------------------------------------------------------------------------

    public static MeasurementFragment newInstance(FootSide footSide, String file_path) {

        Bundle args = new Bundle();
        args.putString(FILE_PATH_KEY, file_path);
        args.putSerializable(FootSide.FOOTSIDE_KEY, footSide);

        MeasurementFragment fragment = new MeasurementFragment();
        fragment.setArguments(args);

        return fragment;
    }

    //endregion

    //region LIFECYCLE METHODS ---------------------------------------------------------------------

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.measurement_fragment, container, false);
        mViewUnbinder = ButterKnife.bind(this, view);

        mMeasureWidget.setFootSide(mFootSide);
        mMeasureWidget.setAngleListener(mAngleListener);

        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Bundle args = getArguments();
        if (args != null) {
            String filePath = args.getString(FILE_PATH_KEY);
            mFootSide = (FootSide) args.getSerializable(FootSide.FOOTSIDE_KEY);

            LayoutListener layoutListener = new LayoutListener(filePath);
            mFootImage.getViewTreeObserver().addOnGlobalLayoutListener(layoutListener);
        } else {
            mFootSide = FootSide.LEFT;
        }

        if (savedInstanceState != null) {
            mDialogDisplayed = savedInstanceState.getBoolean(DIALOG_DISPLAYED_KEY, false);
        }

        setDebugMode(false);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(DIALOG_DISPLAYED_KEY, mDialogDisplayed);
    }

    @Override
    public void onDestroyView() {
        mFootImage.setImageBitmap(null);
        mFootImage = null;
        mInstructionsDialog = null;

        super.onDestroyView();
        mViewUnbinder.unbind();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        RefWatcher refWatcher = BikeFitApplication.getRefWatcher(getActivity());
        refWatcher.watch(this);
    }

    //endregion

    //region PUBLIC CLASS METHODS ------------------------------------------------------------------

    public float getAngle() {
        return mAngle;
    }

    //endregion

    //region PRIVATE METHODS -----------------------------------------------------------------------

    private void setDebugMode(boolean debug) {

        if (debug) {
            mMeasureWidget.setDebugMode(true);
            mAngleDisplay.setVisibility(View.VISIBLE);
        } else {
            mMeasureWidget.setDebugMode(false);
            mAngleDisplay.setVisibility(View.GONE);
        }

    }

    private void showDialog() {
        if (!mDialogDisplayed) {
            mInstructionsDialog = MeasurementInstructionsDialogFragment.newInstance();
            mInstructionsDialog.setTargetFragment(this, 1);
            mInstructionsDialog.show(getFragmentManager(), null);
            mDialogDisplayed = true;
        }
    }

    private void setAngle(float angle) {
        mAngle = angle;
        updateWedgeLevelDisplay(mAngle);
        mAngleDisplay.setText(String.format(Locale.US, "%.2f", mAngle));
        Log.d(this.getClass().getSimpleName(), "ANGLE CHANGE: " + angle);
    }

    private void updateWedgeLevelDisplay(float angle) {
        int wedgeLevel = FootSide.getWedgeLevel(angle);
        Log.d(this.getClass().getSimpleName(), "wedge level: " + wedgeLevel);
        mWedgeGraphic.setImageLevel(wedgeLevel);
    }

    //endregion

    // region listeners ----

    MeasureWidget.AngleListener mAngleListener = new MeasureWidget.AngleListener() {
        @Override
        public void onAngleUpdate(float angle) {
            setAngle(angle);
        }
    };

    // endregion


    //region INNER CLASSES -------------------------------------------------------------------------

    private class LayoutListener implements ViewTreeObserver.OnGlobalLayoutListener {

        final String filePath;

        public LayoutListener(String filePath) {
            this.filePath = filePath;
        }

        @Override
        public void onGlobalLayout() {
            getView().getViewTreeObserver().removeOnGlobalLayoutListener(this);

            int width = mFootImage.getWidth();
            int height = mFootImage.getHeight();

            BitmapWorkerTask task = new BitmapWorkerTask(filePath, width, height, mFootImage);
            task.execute();

            showDialog();
        }
    }

    //endregion

}
