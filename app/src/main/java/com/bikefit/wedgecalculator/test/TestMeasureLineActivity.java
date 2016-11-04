package com.bikefit.wedgecalculator.test;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.bikefit.wedgecalculator.R;
import com.bikefit.wedgecalculator.view.MeasureWidget;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TestMeasureLineActivity extends AppCompatActivity {

    @BindView(R.id.measurement_fragment_angle_measure_widget)
    MeasureWidget mMeasureWidget;

    @BindView(R.id.test_measureline_angle)
    TextView mAngleText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test_measureline_activity);

        ButterKnife.bind(this);
        mMeasureWidget.setAngleListener(new MeasureWidget.AngleListener() {
            @Override
            public void onAngleUpdate(float angle) {
                mAngleText.setText(Float.toString(angle));
            }
        });
    }
}
