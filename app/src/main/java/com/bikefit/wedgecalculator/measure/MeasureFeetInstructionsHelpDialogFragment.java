package com.bikefit.wedgecalculator.measure;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;

import com.bikefit.wedgecalculator.R;

import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Display a styled informative dialog for the MeasureFeetInstructionsHelpFragment class
 */
public class MeasureFeetInstructionsHelpDialogFragment extends DialogFragment {

    //region CLASS VARIABLES -----------------------------------------------------------------------

    private AlertDialog mDialog;
    private Unbinder mViewUnBinder;

    //endregion

    //region CONSTRUCTOR ---------------------------------------------------------------------------

    public static MeasureFeetInstructionsHelpDialogFragment newInstance() {
        return new MeasureFeetInstructionsHelpDialogFragment();
    }

    //endregion

    //region LIFECYCLE METHODS ---------------------------------------------------------------------

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.measurement_feet_instructions_help_dialog_fragment, null);
        mViewUnBinder = ButterKnife.bind(this, dialogView);

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getActivity(), R.style.WedgeDialog);
        dialogBuilder.setView(dialogView);
        dialogBuilder.setCancelable(false);
        mDialog = dialogBuilder.create();

        return mDialog;
    }

    @Override
    public void onDestroyView() {
        mDialog = null;
        super.onDestroyView();
        mViewUnBinder.unbind();
    }

    //endregion

    //region LISTENERS -----------------------------------------------------------------------------

    @OnClick(R.id.measurement_feet_instructions_help_dialog_fragment_button)
    public void OnReadyButton() {
        mDialog.dismiss();
    }

    //endregion

}
