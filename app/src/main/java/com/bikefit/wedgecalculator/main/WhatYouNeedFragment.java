package com.bikefit.wedgecalculator.main;


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

public class WhatYouNeedFragment extends Fragment {

    //region INJECTED CLASSES ----------------------------------------------------------------------
    //endregion

    //region INJECTED VIEWS ------------------------------------------------------------------------

    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    //endregion

    //region PUBLIC INTERFACES ---------------------------------------------------------------------

    public static WhatYouNeedFragment newInstance() {
        Bundle args = new Bundle();
        WhatYouNeedFragment fragment = new WhatYouNeedFragment();
        fragment.setArguments(args);
        return fragment;
    }

    //endregion

    //region STATIC LOCAL CONSTANTS ----------------------------------------------------------------
    //endregion

    //region CLASS VARIABLES -----------------------------------------------------------------------

    private Unbinder mViewUnBinder;

    //endregion

    //region CONSTRUCTOR ---------------------------------------------------------------------------
    //endregion

    //region LIFECYCLE METHODS ---------------------------------------------------------------------

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.what_you_need_fragment, container, false);
        mViewUnBinder = ButterKnife.bind(this, view);

        mToolbar.setTitle(getResources().getString(R.string.what_you_need_fragment_title_label));
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    //endregion

    //region WIDGET --------------------------------------------------------------------------------
    //endregion

    //region LISTENERS -----------------------------------------------------------------------------

    @OnClick(R.id.toolbar)
    public void onToolbarBackPressed() {
        getActivity().onBackPressed();
    }

    @OnClick(R.id.what_you_need_fragment_ok_button)
    public void okButtonClicked() {
        Log.d(this.getClass().getSimpleName(), "Button clicked");
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
