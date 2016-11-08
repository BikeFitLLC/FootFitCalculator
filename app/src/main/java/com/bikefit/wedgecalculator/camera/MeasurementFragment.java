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

import com.bikefit.wedgecalculator.BikeFitApplication;
import com.bikefit.wedgecalculator.R;
import com.bikefit.wedgecalculator.view.MeasureWidget;
import com.squareup.leakcanary.RefWatcher;

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

    //endregion

    //region CLASS VARIABLES -----------------------------------------------------------------------

    private Unbinder mViewUnbinder;
    private MeasurementInstructionsDialogFragment mInstructionsDialog;
    private boolean mDialogDisplayed = false;

    private float mAngle;

    private MeasureWidget.FootSide mFootSide = MeasureWidget.FootSide.LEFT;

    //endregion

    //region CONSTRUCTOR ---------------------------------------------------------------------------

    public static MeasurementFragment newInstance(MeasureWidget.FootSide footSide, String file_path) {

        Bundle args = new Bundle();
        args.putString(FILE_PATH_KEY, file_path);
        args.putSerializable(MeasureWidget.FOOTSIDE_KEY, footSide);

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
            mFootSide = (MeasureWidget.FootSide) args.getSerializable(MeasureWidget.FOOTSIDE_KEY);

            LayoutListener layoutListener = new LayoutListener(filePath);
            mFootImage.getViewTreeObserver().addOnGlobalLayoutListener(layoutListener);
        } else {
            mFootSide = MeasureWidget.FootSide.LEFT;
        }

        if (savedInstanceState != null) {
            mDialogDisplayed = savedInstanceState.getBoolean(DIALOG_DISPLAYED_KEY, false);
        }
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
    //endregion

    //region PRIVATE METHODS -----------------------------------------------------------------------

    private void showDialog() {
        if (!mDialogDisplayed) {
            mInstructionsDialog = MeasurementInstructionsDialogFragment.newInstance();
            mInstructionsDialog.setTargetFragment(this, 1);
            mInstructionsDialog.show(getFragmentManager(), null);
            mDialogDisplayed = true;
        }
    }

    private void setAngle(float angle) {
        Log.d(this.getClass().getSimpleName(), "ANGLE CHANGE: " + angle);
        mAngle = angle;
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
