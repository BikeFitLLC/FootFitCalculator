package com.bikefit.wedgecalculator.measure;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bikefit.wedgecalculator.BikeFitApplication;
import com.bikefit.wedgecalculator.R;
import com.bikefit.wedgecalculator.measure.model.FootSide;
import com.bikefit.wedgecalculator.settings.AnalyticsTracker;
import com.squareup.leakcanary.RefWatcher;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Represents a view for showing help for Instructions (shows camera view with help instructions)
 */
public class MeasureFeetInstructionsHelpFragment extends Fragment {

    //region STATIC LOCAL CONSTANTS ----------------------------------------------------------------

    public static final String FOOTSIDE_LABEL_KEY = "FOOTSIDE_LABEL_KEY";

    //endregion

    //region INJECTED VIEWS ------------------------------------------------------------------------

    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    //endregion

    //region PUBLIC INTERFACES ---------------------------------------------------------------------
    //endregion

    //region CLASS VARIABLES -----------------------------------------------------------------------

    private Unbinder mViewUnBinder;

    //endregion

    //region CONSTRUCTOR ---------------------------------------------------------------------------

    public static MeasureFeetInstructionsHelpFragment newInstance(String footSideLabel) {
        MeasureFeetInstructionsHelpFragment fragment = new MeasureFeetInstructionsHelpFragment();
        Bundle args = new Bundle();
        args.putString(FOOTSIDE_LABEL_KEY, footSideLabel);
        fragment.setArguments(args);
        return fragment;
    }

    //endregion

    //region LIFECYCLE METHODS ---------------------------------------------------------------------

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.measure_feet_instructions_help_fragment, container, false);
        mViewUnBinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        String footSideLabel;
        Bundle args = getArguments();
        if (args != null) {
            footSideLabel = args.getString(FOOTSIDE_LABEL_KEY, FootSide.LEFT.getLabel());
        } else {
            footSideLabel = FootSide.LEFT.getLabel();
        }

        String title = getResources().getString(R.string.measure_feet_instructions_help_fragment_title_text, footSideLabel);
        mToolbar.setTitle(title);
        mToolbar.setNavigationOnClickListener(mNavigationListener);
        AnalyticsTracker.INSTANCE.sendAnalyticsScreen(title);

        showHelpDialog();
    }

    @Override
    public void onDestroyView() {
        mToolbar = null;
        super.onDestroyView();
        mViewUnBinder.unbind();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        RefWatcher refWatcher = BikeFitApplication.getRefWatcher(getActivity());
        refWatcher.watch(this);
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

    @OnClick(R.id.measure_feet_instructions_help_fragment_ready_button)
    public void onReadyButton() {
        getActivity().onBackPressed();
    }

    //endregion

    //region PRIVATE METHODS -----------------------------------------------------------------------

    private void showHelpDialog() {
        MeasureFeetInstructionsHelpDialogFragment dialogFragment = MeasureFeetInstructionsHelpDialogFragment.newInstance();
        dialogFragment.setTargetFragment(this, 1);
        dialogFragment.show(getFragmentManager(), null);
    }

    //endregion

    //region INNER CLASSES -------------------------------------------------------------------------
    //endregion

}
