package com.bikefit.wedgecalculator.test;

import android.os.Bundle;

import com.bikefit.wedgecalculator.measure.MeasurementFragment;
import com.bikefit.wedgecalculator.measure.model.FootSide;


public class TestMeasurementFragmentActivity extends TestFragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        MeasurementFragment fragment = MeasurementFragment.newInstance(FootSide.LEFT, "");
        super.transactToFragment(fragment);
    }
}
