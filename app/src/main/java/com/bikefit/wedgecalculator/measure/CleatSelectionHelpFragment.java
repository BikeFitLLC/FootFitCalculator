package com.bikefit.wedgecalculator.measure;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bikefit.wedgecalculator.R;
import com.bikefit.wedgecalculator.settings.InternetUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Help screen for selecting a Cleat type
 */
public class CleatSelectionHelpFragment extends Fragment {

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

    //endregion

    //region CONSTRUCTOR ---------------------------------------------------------------------------

    public static CleatSelectionHelpFragment newInstance() {
        Bundle args = new Bundle();

        CleatSelectionHelpFragment fragment = new CleatSelectionHelpFragment();
        fragment.setArguments(args);

        return fragment;
    }

    //endregion

    //region LIFECYCLE METHODS ---------------------------------------------------------------------

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.cleat_selection_help_fragment, container, false);
        mViewUnBinder = ButterKnife.bind(this, view);

        mToolbar.setTitle(getString(R.string.cleat_selection_fragment_title_label));
        mToolbar.setNavigationOnClickListener(mNavigationListener);

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

    @OnClick(R.id.cleat_selection_help_fragment_get_fitted_button)
    public void onGetFittedButton() {
        if (mInternetUtil.checkInternet()) {
            String url = getString(R.string.measurement_summary_fragment_professional_fitting_url);
            mInternetUtil.openExternalWebPage(url);
        }
    }

    //endregion

    //region ACCESSORS -----------------------------------------------------------------------------
    //endregion

    //region PUBLIC CLASS METHODS ------------------------------------------------------------------
    //endregion

    //region PRIVATE METHODS -----------------------------------------------------------------------
    //endregion

}
