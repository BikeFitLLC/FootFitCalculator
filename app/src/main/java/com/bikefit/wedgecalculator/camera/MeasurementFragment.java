package com.bikefit.wedgecalculator.camera;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;

import com.bikefit.wedgecalculator.BikeFitApplication;
import com.bikefit.wedgecalculator.R;
import com.squareup.leakcanary.RefWatcher;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class MeasurementFragment extends Fragment {

    //region STATIC LOCAL CONSTANTS ----------------------------------------------------------------

    public static final String FILE_PATH = "FILE_PATH";

    //endregion

    //region INJECTED VIEWS ------------------------------------------------------------------------

    @BindView(R.id.measurement_fragment_foot_image)
    ImageView footImage;

    //endregion

    //region CLASS VARIABLES -----------------------------------------------------------------------

    private Unbinder viewUnbinder;

    //endregion

    //region CONSTRUCTOR ---------------------------------------------------------------------------

    public static MeasurementFragment newInstance(String file_path) {

        Bundle args = new Bundle();
        args.putString(FILE_PATH, file_path);

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
        viewUnbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Bundle args = getArguments();
        if (args != null) {
            String filePath = args.getString(FILE_PATH);

            LayoutListener layoutListener = new LayoutListener(filePath);
            footImage.getViewTreeObserver().addOnGlobalLayoutListener(layoutListener);
        }

    }

    @Override
    public void onDestroyView() {
        footImage.setImageBitmap(null);
        footImage = null;

        super.onDestroyView();
        viewUnbinder.unbind();
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

    private class LayoutListener implements ViewTreeObserver.OnGlobalLayoutListener {

        String filePath;

        public LayoutListener(String filePath) {
            this.filePath = filePath;
        }

        @Override
        public void onGlobalLayout() {
            getView().getViewTreeObserver().removeOnGlobalLayoutListener(this);

            int width = footImage.getWidth();
            int height = footImage.getHeight();

            BitmapWorkerTask task = new BitmapWorkerTask(filePath, width, height, footImage);
            task.execute();
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

}
