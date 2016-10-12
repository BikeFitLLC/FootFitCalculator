package com.bikefit.wedgecalculator.camera;

import android.app.Fragment;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;

import com.bikefit.wedgecalculator.R;

import java.io.IOException;

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
        super.onDestroyView();
        viewUnbinder.unbind();
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

            setupImageView(filePath, width, height, footImage);
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


    /**
     * Loads the given image located in the filePath into the given ImageView, scaled to fit within the width and height area
     * of the imageView
     *
     * @param filePath  Path to the image file to load
     * @param width     The width of the ImageView (use getViewTreeObserver on ImageView)
     * @param height    The height of the ImageView (use getViewTreeObserver on ImageView)
     * @param imageView The ImageView to load the bitmap into
     */
    private void setupImageView(final String filePath, final int width, final int height, final ImageView imageView) {

        // Do all bitmap calculations on a simple background thread
        new AsyncTask<Void, Void, Boolean>() {

            Bitmap myBitmap = null;

            @Override
            protected Boolean doInBackground(Void... params) {

                //Determine if image needs to be rotated by looking at Exif information
                int orientation = ExifInterface.ORIENTATION_NORMAL;

                try {
                    ExifInterface exif = new ExifInterface(filePath);
                    orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
                    Log.i(this.getClass().getSimpleName(), "orientation: " + orientation);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                // decode the bitmap with scaling
                myBitmap = decodeSampledBitmap(filePath, width, height);

                // rotate the bitmap if necessary
                if (orientation != ExifInterface.ORIENTATION_NORMAL) {

                    Matrix matrix = new Matrix();
                    switch (orientation) {
                        case ExifInterface.ORIENTATION_ROTATE_90:
                            matrix.postRotate(90);
                            break;
                        case ExifInterface.ORIENTATION_ROTATE_180:
                            matrix.postRotate(180);
                            break;
                        case ExifInterface.ORIENTATION_ROTATE_270:
                            matrix.postRotate(270);
                            break;
                    }

                    myBitmap = Bitmap.createBitmap(myBitmap, 0, 0, myBitmap.getWidth(), myBitmap.getHeight(), matrix, true); // rotating bitmap
                }

                if (myBitmap != null) {
                    return true;
                }

                return false;
            }

            @Override
            protected void onPostExecute(Boolean result) {
                if (result) {
                    imageView.setImageBitmap(myBitmap);
                }
            }
        }.execute();
    }


    /**
     * Determine a sample size based on options derived from the BitmapFactory (using inJustDecodeBounds)
     *
     * @param options   Options derived from a BitmapFactory call on bitmap
     * @param reqWidth  desired width of image after scaling
     * @param reqHeight desired height of image after scaling
     * @return a sample size to use for the BitmapFactory
     */
    private static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {

        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {
            // Calculate ratios of height and width to requested height and width
            final int heightRatio = Math.round((float) height / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);

            // Choose the smallest ratio as inSampleSize value, this will guarantee
            // a final image with both dimensions larger than or equal to the
            // requested height and width.
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }

        return inSampleSize;
    }

    /**
     * decode the bitmap to determine it's full height / width
     *
     * @param filePath  Path to the file
     * @param reqWidth  desired max width
     * @param reqHeight desired max height
     * @return a scaled bitmap using the desired width / height
     */
    private static Bitmap decodeSampledBitmap(String filePath, int reqWidth, int reqHeight) {

        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filePath, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(filePath, options);
    }


    //endregion

    //region OBSERVERS -----------------------------------------------------------------------------
    //endregion

    //region INNER CLASSES -------------------------------------------------------------------------
    //endregion

}
