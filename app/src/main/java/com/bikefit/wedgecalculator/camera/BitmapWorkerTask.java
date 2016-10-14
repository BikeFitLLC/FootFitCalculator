package com.bikefit.wedgecalculator.camera;

import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.os.AsyncTask;
import android.widget.ImageView;

import java.lang.ref.WeakReference;


/**
 * Loads the given image located in the filePath into the given ImageView,
 * scaled to fit within the width and height area of the imageView
 */
public class BitmapWorkerTask extends AsyncTask<Void, Void, Bitmap> {
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


    @Override
    protected void onPostExecute(Bitmap bitmap) {

        // Once complete, see if ImageView is still around and set bitmap.
        if (imageViewReference != null && bitmap != null) {
            final ImageView imageView = imageViewReference.get();
            if (imageView != null) {
                imageView.setImageBitmap(bitmap);
            }
            bitmap = null;
        }
    }

}