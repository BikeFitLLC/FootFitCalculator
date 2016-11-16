package com.bikefit.wedgecalculator.measure;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bikefit.wedgecalculator.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Screen for selecting a Cleat type (one of three options)
 */
public class CleatSelectionFragment extends Fragment {

    public static String WEDGE_COUNT = "WEDGE_COUNT";

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

    //todo send entire measurement model?
    public static CleatSelectionFragment newInstance() {
        Bundle args = new Bundle();

        CleatSelectionFragment fragment = new CleatSelectionFragment();
        fragment.setArguments(args);

        return fragment;
    }

    //endregion

    //region LIFECYCLE METHODS ---------------------------------------------------------------------

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.cleat_selection_fragment, container, false);
        mViewUnBinder = ButterKnife.bind(this, view);

        mToolbar.setTitle(getString(R.string.cleat_selection_fragment_title_label));
        mToolbar.setNavigationOnClickListener(mNavigationListener);

        return view;
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

    @OnClick(R.id.cleat_selection_fragment_look_cleat_button)
    public void onLookCleatButton() {
        Log.i(this.getClass().getSimpleName(), "Look cleat button");
    }

    @OnClick(R.id.cleat_selection_fragment_speedplay_cleat_button)
    public void onSpeedplayCleatButton() {
        Log.i(this.getClass().getSimpleName(), "speedplay cleat button");
    }

    @OnClick(R.id.cleat_selection_fragment_spd_cleat_button)
    public void onSPDCleatButton() {
        Log.i(this.getClass().getSimpleName(), "spd cleat button");
    }

    @OnClick(R.id.cleat_selection_fragment_dont_know_button)
    public void onDontKnowButton() {
        Log.i(this.getClass().getSimpleName(), "I don't know button");
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
    //endregion

    //region OBSERVERS -----------------------------------------------------------------------------
    //endregion

    //region INNER CLASSES -------------------------------------------------------------------------
    //endregion

}
