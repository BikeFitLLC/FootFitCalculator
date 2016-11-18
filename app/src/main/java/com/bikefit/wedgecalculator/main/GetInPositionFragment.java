package com.bikefit.wedgecalculator.main;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bikefit.wedgecalculator.R;
import com.bikefit.wedgecalculator.measure.CameraInstructionsFragment;
import com.bikefit.wedgecalculator.measure.model.FootSide;
import com.bikefit.wedgecalculator.settings.AnalyticsTracker;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class GetInPositionFragment extends Fragment {

    //region INJECTED CLASSES ----------------------------------------------------------------------
    //endregion

    //region INJECTED VIEWS ------------------------------------------------------------------------

    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    //endregion

    //region PUBLIC INTERFACES ---------------------------------------------------------------------
    //endregion

    //region STATIC LOCAL CONSTANTS ----------------------------------------------------------------
    //endregion

    //region CLASS VARIABLES -----------------------------------------------------------------------

    private Unbinder mViewUnBinder;

    //endregion

    //region CONSTRUCTOR ---------------------------------------------------------------------------

    public static GetInPositionFragment newInstance() {
        Bundle args = new Bundle();
        GetInPositionFragment fragment = new GetInPositionFragment();
        fragment.setArguments(args);
        return fragment;
    }

    //endregion

    //region LIFECYCLE METHODS ---------------------------------------------------------------------

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.get_in_position_fragment, container, false);
        mViewUnBinder = ButterKnife.bind(this, view);

        mToolbar.setTitle(getResources().getString(R.string.get_in_position_fragment_title_label));
        mToolbar.setNavigationOnClickListener(mNavigationListener);
        AnalyticsTracker.INSTANCE.sendAnalyticsScreen(getResources().getString(R.string.get_in_position_fragment_title_label));

        return view;
    }

    @Override
    public void onDestroyView() {
        mToolbar = null;

        super.onDestroyView();
        mViewUnBinder.unbind();
    }

    //endregion

    //region WIDGET --------------------------------------------------------------------------------
    //endregion

    //region LISTENERS -----------------------------------------------------------------------------


    View.OnClickListener mNavigationListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            getActivity().onBackPressed();
        }
    };

    @OnClick(R.id.get_in_position_fragment_measure_button)
    public void okButtonClicked() {
        CameraInstructionsFragment fragment = CameraInstructionsFragment.newInstance(FootSide.LEFT);
        ((MainMenuActivity) getActivity()).showFragment(fragment, true);
    }

    //endregion

    //region ACCESSORS -----------------------------------------------------------------------------
    //endregion

    //region PUBLIC CLASS METHODS ------------------------------------------------------------------
    //endregion

    //region PRIVATE METHODS -----------------------------------------------------------------------
    //endregion

}
