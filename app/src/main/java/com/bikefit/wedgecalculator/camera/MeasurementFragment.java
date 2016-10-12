package com.bikefit.wedgecalculator.camera;

import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.os.AsyncTask;
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

import java.lang.ref.WeakReference;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class MeasurementFragment extends Fragment {

    //region STATIC LOCAL CONSTANTS ----------------------------------------------------------------

    public static String FILE_PATH = "FILE_PATH";

    //endregion

    //region INJECTED VIEWS ------------------------------------------------------------------------

    @BindView(R.id.measurement_fragment_foot_image)
    ImageView footImage;

    //endregion

    //region CLASS VARIABLES -----------------------------------------------------------------------

    private Unbinder viewUnbinder;

    //endregion

    //region CONSTRUCTOR ---------------------------------------------------------------------------

    public MeasurementFragment() {

    }

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
    public void onDestroy() {
        super.onDestroy();
        RefWatcher refWatcher = BikeFitApplication.getRefWatcher(getActivity());
        refWatcher.watch(this);
    }

    @Override
    public void onDestroyView() {
        footImage.setImageBitmap(null);
        footImage = null;

        super.onDestroyView();
        viewUnbinder.unbind();
    }

    //endregion

    //region WIDGET --------------------------------------------------------------------------------
    //endregion

    //region LISTENERS -----------------------------------------------------------------------------

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

            //setupImageView(filePath, width, height, footImage);
        }
    }


    //endregion

    //region OBSERVERS -----------------------------------------------------------------------------
    //endregion

    //region INNER CLASSES -------------------------------------------------------------------------

    /**
     * Loads the given image located in the filePath into the given ImageView, scaled to fit within the width and height area
     * of the imageView
     */
    private class BitmapWorkerTask extends AsyncTask<Void, Void, Bitmap> {
        private final WeakReference<ImageView> imageViewReference;
        private final String filePath;
        private final int width;
        private final int height;

        public BitmapWorkerTask(String filePath, int width, final int height, ImageView imageView) {
            // Use a WeakReference to ensure the ImageView can be garbage collected
            imageViewReference = new WeakReference<ImageView>(imageView);
            this.filePath = filePath;
            this.width = width;
            this.height = height;
        }

        // Decode image in background.
        @Override
        protected Bitmap doInBackground(Void... params) {

            // decode the bitmap with scaling
            Bitmap myBitmap = BitmapUtil.decodeBitmap(filePath, width, height);

            //Determine if image needs to be rotated
            if (BitmapUtil.doesBitmapNeedToBeRotated(filePath)) {
                int orientation = BitmapUtil.getBitmapOrientation(filePath);
                Matrix matrix = BitmapUtil.getBitmapOrientationMatrix(orientation);
                myBitmap = Bitmap.createBitmap(myBitmap, 0, 0, myBitmap.getWidth(), myBitmap.getHeight(), matrix, true); // rotating bitmap
            }

            return myBitmap;
        }

        // Once complete, see if ImageView is still around and set bitmap.
        @Override
        protected void onPostExecute(Bitmap bitmap) {
            if (imageViewReference != null && bitmap != null) {
                final ImageView imageView = imageViewReference.get();
                if (imageView != null) {
                    imageView.setImageBitmap(bitmap);
                }
                bitmap = null;
            }
        }

    }

    //endregion

}
