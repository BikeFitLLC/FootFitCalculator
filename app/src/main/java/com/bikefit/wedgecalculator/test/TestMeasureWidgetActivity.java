package com.bikefit.wedgecalculator.test;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.TextView;

import com.bikefit.wedgecalculator.R;
import com.bikefit.wedgecalculator.measure.model.FootSide;
import com.bikefit.wedgecalculator.view.MeasureWidget;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class TestMeasureWidgetActivity extends AppCompatActivity {

    //region INJECTED CLASSES ----------------------------------------------------------------------
    //endregion

    //region INJECTED VIEWS ------------------------------------------------------------------------

    @BindView(R.id.test_measurewidget_widget)
    MeasureWidget mMeasureWidget;

    @BindView(R.id.test_measurewidget_angle)
    TextView mAngleText;

    @BindView(R.id.test_measurewidget_togglebutton)
    Button mFootToggleButton;

    //endregion

    //region PUBLIC INTERFACES ---------------------------------------------------------------------
    //endregion

    //region STATIC LOCAL CONSTANTS ----------------------------------------------------------------
    //endregion

    //region CLASS VARIABLES -----------------------------------------------------------------------

    private FootSide mFootSide = FootSide.LEFT;

    //endregion

    //region CONSTRUCTOR ---------------------------------------------------------------------------
    //endregion

    //region LIFECYCLE METHODS ---------------------------------------------------------------------

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test_measurewidget_activity);

        ButterKnife.bind(this);
        mMeasureWidget.setFootSide(mFootSide);
        mMeasureWidget.setAngleListener(new MeasureWidget.AngleListener() {
            @Override
            public void onAngleUpdate(float angle) {
                mAngleText.setText(Float.toString(angle));
            }
        });

        setFootSide(mFootSide);
    }

    //endregion

    //region LISTENERS -----------------------------------------------------------------------------

    @OnClick(R.id.test_measurewidget_togglebutton)
    void clickFootToggle() {
        FootSide newSide = mFootSide == FootSide.LEFT ? FootSide.RIGHT : FootSide.LEFT;
        setFootSide(newSide);
    }

    //endregion

    //region ACCESSORS -----------------------------------------------------------------------------
    //endregion

    //region PUBLIC CLASS METHODS ------------------------------------------------------------------
    //endregion

    //region PRIVATE METHODS -----------------------------------------------------------------------

    private void setFootSide(FootSide footSide) {
        mFootToggleButton.setText(footSide.toString());
        mMeasureWidget.setFootSide(footSide);
        mFootSide = footSide;
    }

    //endregion

    //region OBSERVERS -----------------------------------------------------------------------------
    //endregion

    //region INNER CLASSES -------------------------------------------------------------------------
    //endregion

}
