package com.bikefit.wedgecalculator.test;

import android.os.Bundle;

import com.bikefit.wedgecalculator.measure.MeasurementFragment;
import com.bikefit.wedgecalculator.measure.model.FootSide;

/**
 * Used for testing the Measure Widget custom view without affecting other parts of the app flow
 */
public class TestMeasurementFragmentActivity extends TestFragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        MeasurementFragment fragment = MeasurementFragment.newInstance(FootSide.LEFT, "");
        super.transactToFragment(fragment);
    }

}
