package com.bikefit.wedgecalculator.camera;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;

import java.io.IOException;

/**
 * Bitmap Resizing Utilities for low-memory situations
 */
public class BitmapUtil {

    /**
     * decode the bitmap to determine it's full height / width
     *
     * @param filePath  Path to the file
     * @param reqWidth  desired max width
     * @param reqHeight desired max height
     * @return a scaled bitmap using the desired width / height
     */
    public static Bitmap decodeBitmap(String filePath, int reqWidth, int reqHeight) {

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

    /**
     * Determine a sample size based on options derived from the BitmapFactory (using inJustDecodeBounds)
     *
     * @param options   Options derived from a BitmapFactory call on bitmap
     * @param reqWidth  desired width of image after scaling
     * @param reqHeight desired height of image after scaling
     * @return a sample size to use for the BitmapFactory
     */
    public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {

        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (reqWidth <= 0) {
            reqWidth = width;
        }

        if (reqHeight <= 0) {
            reqHeight = height;
        }

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
     * Determine if image needs to be rotated by looking at Exif information
     * @param filePath path to the bitmap file
     * @return False if = ExifInterface.ORIENTATION_NORMAL; True otherwise
     */
    public static boolean doesBitmapNeedToBeRotated(String filePath) {
        return getBitmapOrientation(filePath) != ExifInterface.ORIENTATION_NORMAL;
    }

    /**
     * Returns the current orientation code for the given bitmap
     * @param filePath path to the bitmap file
     * @return ExifInterface.TAG_ORIENTATION
     */
    public static int getBitmapOrientation(String filePath) {

        int orientation = ExifInterface.ORIENTATION_NORMAL;

        try {
            ExifInterface exif = new ExifInterface(filePath);
            orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return orientation;
    }

    /**
     * Create Matrix with the correct rotation for bitmap to make it normal
     * @param orientation The image's current orientation (from getBitmapOrientation)
     * @return Matrix with the proper postRotate value
     */
    public static Matrix getBitmapOrientationMatrix(int orientation) {

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

        return matrix;
    }

}
