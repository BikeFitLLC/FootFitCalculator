package com.bikefit.wedgecalculator.camera;

import android.graphics.BitmapFactory;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class BitmapUtilTest {

    //region CLASS UNDER TEST ----------------------------------------------------------------------

    //endregion

    //region MOCKS ---------------------------------------------------------------------------------
    //endregion

    //region NON-MOCKS -----------------------------------------------------------------------------
    //endregion

    //region SETUP ---------------------------------------------------------------------------------
    //endregion

    //region TESTS ---------------------------------------------------------------------------------

    @Test
    public void testCalculateInSampleSize() throws Exception {
        // WHEN I have a large bitmap
        int bitmapWidth = 5000;
        int bitmapHeight = 2000;

        // WHEN I have created a BitmapFacotry.Options object to determine its bounds
        BitmapFactory.Options bitmapFactoryOptions = new BitmapFactory.Options();
        bitmapFactoryOptions.outWidth = bitmapWidth;
        bitmapFactoryOptions.outHeight = bitmapHeight;
        int inSampleSize;


        // WHEN I request a width/height exactly the same as the bitmap's size
        inSampleSize = BitmapUtil.calculateInSampleSize(bitmapFactoryOptions, bitmapWidth, bitmapHeight);
        // THEN I should get a sample size of 1
        assertEquals(1, inSampleSize);

        // WHEN I request a width/height ratio using values lower than the bitmap's size
        inSampleSize = BitmapUtil.calculateInSampleSize(bitmapFactoryOptions, bitmapWidth/2, bitmapHeight/2);
        // THEN I should get a sample size of 2
        assertEquals(2, inSampleSize);

        // WHEN I request a width/height ratio using bad values
        inSampleSize = BitmapUtil.calculateInSampleSize(bitmapFactoryOptions, 0, 0);
        // THEN I should get a sample size of 1
        assertEquals(1, inSampleSize);

        inSampleSize = BitmapUtil.calculateInSampleSize(bitmapFactoryOptions, -2000, bitmapHeight/2);
        // THEN I should get a sample size of 1
        assertEquals(1, inSampleSize);

        // WHEN I request a width/height ratio using values lower than the bitmap's size
        inSampleSize = BitmapUtil.calculateInSampleSize(bitmapFactoryOptions, bitmapWidth*2, bitmapHeight*2);
        // THEN I should get a sample size of 1
        assertEquals(1, inSampleSize);

    }

    //endregion

    //region PRIVATE METHODS -----------------------------------------------------------------------
    //endregion
}