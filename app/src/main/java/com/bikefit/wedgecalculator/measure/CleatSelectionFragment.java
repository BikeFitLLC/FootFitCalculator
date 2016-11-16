package com.bikefit.wedgecalculator.measure;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bikefit.wedgecalculator.R;
import com.bikefit.wedgecalculator.main.MainMenuActivity;
import com.bikefit.wedgecalculator.measure.model.MeasureModel;
import com.bikefit.wedgecalculator.settings.InternetUtil;

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
    InternetUtil mInternetUtil = new InternetUtil();
    int mTotalWedgeCount;

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

        mTotalWedgeCount = MeasureModel.getTotalWedgeCount();

        return view;
    }

    @Override
    public void onDestroyView() {
        mInternetUtil = null;
        super.onDestroyView();
        mViewUnBinder.unbind();
    }

    //endregion

    //region WIDGET --------------------------------------------------------------------------------
    //endregion

    //region LISTENERS -----------------------------------------------------------------------------

    final View.OnClickListener mNavigationListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            getActivity().onBackPressed();
        }
    };

    @OnClick(R.id.cleat_selection_fragment_look_cleat_button)
    public void onLookCleatButton() {
        if (mInternetUtil.checkInternet()) {
            String url = getString(R.string.cleat_selection_fragment_look_url);
            mInternetUtil.openExternalWebPage(url);
        }
    }

    @OnClick(R.id.cleat_selection_fragment_speedplay_cleat_button)
    public void onSpeedplayCleatButton() {
        if (mInternetUtil.checkInternet()) {
            String url = getString(R.string.cleat_selection_fragment_speedplay_url);
            mInternetUtil.openExternalWebPage(url);
        }
    }

    @OnClick(R.id.cleat_selection_fragment_spd_cleat_button)
    public void onSPDCleatButton() {
        if (mInternetUtil.checkInternet()) {
            String url = getString(R.string.cleat_selection_fragment_spd_url);
            mInternetUtil.openExternalWebPage(url);
        }
    }

    @OnClick(R.id.cleat_selection_fragment_dont_know_button)
    public void onDontKnowButton() {
        CleatSelectionHelpFragment fragment = CleatSelectionHelpFragment.newInstance();
        ((MainMenuActivity) getActivity()).showFragment(fragment, true);
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
