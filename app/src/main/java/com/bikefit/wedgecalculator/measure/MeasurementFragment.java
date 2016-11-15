package com.bikefit.wedgecalculator.measure;

import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.TextView;

import com.afollestad.materialcamera.util.ImageUtil;
import com.bikefit.wedgecalculator.BikeFitApplication;
import com.bikefit.wedgecalculator.R;
import com.bikefit.wedgecalculator.main.MainMenuActivity;
import com.bikefit.wedgecalculator.measure.model.FootSide;
import com.bikefit.wedgecalculator.measure.model.MeasureModel;
import com.bikefit.wedgecalculator.view.MeasureWidget;
import com.squareup.leakcanary.RefWatcher;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Handles the Measurement of the picture for wedges
 */
public class MeasurementFragment extends Fragment {

    //region STATIC LOCAL CONSTANTS ----------------------------------------------------------------

    private static final String DIALOG_DISPLAYED_KEY = "DIALOG_DISPLAYED_KEY";
    private static final String FILE_PATH_KEY = "FILE_PATH_KEY";

    //endregion

    //region INJECTED VIEWS ------------------------------------------------------------------------

    @BindView(R.id.toolbar)
    Toolbar mToolbar;

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
    private Unbinder mViewUnBinder;

    private boolean mDialogDisplayed = false;
    private float mAngle;

    private String mFilePath;
    /**
     * Reference to the bitmap, in case 'onConfigurationChange' event comes, so we do not recreate the bitmap
     */
    private static Bitmap mBitmap;

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
        mViewUnBinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mToolbar.setTitle(getResources().getString(R.string.measurement_fragment_title_text, mFootSide.getLabel()));
        mToolbar.setNavigationOnClickListener(mNavigationListener);

        Bundle args = getArguments();
        if (args != null) {
            mFilePath = args.getString(FILE_PATH_KEY);
            mFootSide = (FootSide) args.getSerializable(FootSide.FOOTSIDE_KEY);

            mFootImage.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
                @Override
                public boolean onPreDraw() {
                    setImageBitmap();
                    mFootImage.getViewTreeObserver().removeOnPreDrawListener(this);
                    return false;
                }
            });

            //LayoutListener layoutListener = new LayoutListener(filePath);
            //mFootImage.getViewTreeObserver().addOnGlobalLayoutListener(layoutListener);

        } else {
            mFootSide = FootSide.LEFT;
        }

        mMeasureWidget.setFootSide(mFootSide);
        mMeasureWidget.setAngleListener(mAngleListener);

        if (savedInstanceState != null) {
            mDialogDisplayed = savedInstanceState.getBoolean(DIALOG_DISPLAYED_KEY, false);
        }

        mMeasureWidget.setDebugMode(false);
        setWedgeGraphic(mFootSide);

        if (!mDialogDisplayed) {
            showDialog();
        }
    }

    /**
     * Sets bitmap to ImageView widget
     */
    private void setImageBitmap() {
        final int width = mFootImage.getMeasuredWidth();
        final int height = mFootImage.getMeasuredHeight();

        // TODO IMPROVE MEMORY USAGE HERE, ESPECIALLY ON LOW-END DEVICES.
        if (mBitmap == null) {
            mBitmap = ImageUtil.getRotatedBitmap(Uri.parse(mFilePath).getPath(), width, height);
        }

        if (mBitmap != null) {
            mFootImage.setImageBitmap(mBitmap);
        }
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(DIALOG_DISPLAYED_KEY, mDialogDisplayed);
    }

    @Override
    public void onDestroyView() {

        if (mBitmap != null && !mBitmap.isRecycled()) {
            try {
                mBitmap.recycle();
                mBitmap = null;
            } catch (Throwable t) {
                t.printStackTrace();
            }
        }

        mFootImage.setImageBitmap(null);
        mFootImage = null;
        mInstructionsDialog = null;
        mToolbar = null;
        mFootSide = null;
        mInstructionsDialog = null;

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

    //region PUBLIC CLASS METHODS ------------------------------------------------------------------

    public float getAngle() {
        return mAngle;
    }

    //endregion

    //region PRIVATE METHODS -----------------------------------------------------------------------

    private void setWedgeGraphic(FootSide footSide) {

        switch (footSide) {
            case LEFT:
                mWedgeGraphic.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.wedge_graphic_left));
                break;
            case RIGHT:
            default:
                mWedgeGraphic.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.wedge_graphic_right));
                break;
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
        mAngleDisplay.setText(getString(R.string.measurement_fragment_angle_display_format, mAngle));
    }

    private void updateWedgeLevelDisplay(float angle) {
        int wedgeLevel = MeasureModel.getWedgeImageLevel(angle);
        mWedgeGraphic.setImageLevel(wedgeLevel);
    }

    //endregion

    //region LISTENERS -----------------------------------------------------------------------------

    private final MeasureWidget.AngleListener mAngleListener = new MeasureWidget.AngleListener() {
        @Override
        public void onAngleUpdate(float angle) {
            setAngle(angle);
        }
    };

    View.OnClickListener mNavigationListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            getActivity().onBackPressed();
        }
    };

    @OnClick(R.id.measurement_fragment_undo_button)
    public void onUndoButtonPressed() {
        getActivity().onBackPressed();
    }


    @OnClick(R.id.measurement_fragment_save_button)
    public void onSaveButtonPressed() {
        //Set foot angle in shared preferences
        MeasureModel.setFootData(mFootSide, mAngle, MeasureModel.calculateWedgeCount(mAngle));

        //Business rule: If writing LEFT data, clear the RIGHT data
        if (mFootSide == FootSide.LEFT) {
            MeasureModel.setFootData(FootSide.RIGHT, null, null);
        }

        MeasurementSummaryFragment fragment = MeasurementSummaryFragment.newInstance();
        ((MainMenuActivity) getActivity()).showFragment(fragment, false);
    }

    //endregion

    //region INNER CLASSES -------------------------------------------------------------------------

/*    private class LayoutListener implements ViewTreeObserver.OnGlobalLayoutListener {

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
    }*/

    //endregion
}
