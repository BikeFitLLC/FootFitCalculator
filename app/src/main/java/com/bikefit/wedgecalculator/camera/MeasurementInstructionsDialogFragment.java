package com.bikefit.wedgecalculator.camera;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.bikefit.wedgecalculator.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Show a 2-step dialog with measurement instructions.
 */
public class MeasurementInstructionsDialogFragment extends DialogFragment {

    public interface MeasurementInstructionsDialogFragmentListener {
        public void onDismissed();
    }


    //region INJECTED VIEWS ------------------------------------------------------------------------

    @BindView(R.id.measurement_instructions_dialog_fragment_button)
    Button mDialogButton;

    @BindView(R.id.measurement_instructions_dialog_fragment_text)
    TextView mDialogText;

    //endregion

    //region CLASS VARIABLES -----------------------------------------------------------------------

    private AlertDialog mDialog;
    private Unbinder mViewUnbinder;

    //endregion

    //region CONSTRUCTOR ---------------------------------------------------------------------------

    public static MeasurementInstructionsDialogFragment newInstance() {
        MeasurementInstructionsDialogFragment fragment = new MeasurementInstructionsDialogFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }
    //endregion

    //region LIFECYCLE METHODS ---------------------------------------------------------------------

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.measurement_instructions_dialog_fragment, null);
        mViewUnbinder = ButterKnife.bind(this, dialogView);

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getActivity(), R.style.WedgeDialog);
        dialogBuilder.setView(dialogView);
        mDialog = dialogBuilder.create();

        displayScreen1();

        return mDialog;
    }

    @Override
    public void onDestroyView() {
        mDialog = null;
        super.onDestroyView();
        mViewUnbinder.unbind();
    }

    //endregion

    //region LISTENERS -----------------------------------------------------------------------------

    @OnClick(R.id.measurement_instructions_dialog_fragment_button)
    public void onButtonClick() {

        String currentButtonText = (String) mDialogButton.getText();
        String button1Text = getActivity().getString(R.string.measurement_instructions_dialog_fragment_screen1_button_text);

        Log.d(this.getClass().getSimpleName(), "Button pressed: " + currentButtonText);

        if (currentButtonText.equalsIgnoreCase(button1Text)) {
            displayScreen2();
        } else {
            mDialog.dismiss();
            getListener().onDismissed();
        }
    }


    //endregion

    //region PRIVATE METHODS -----------------------------------------------------------------------

    private void displayScreen1() {
        mDialogButton.setText(getActivity().getString(R.string.measurement_instructions_dialog_fragment_screen1_button_text));
        mDialogText.setText(getActivity().getString(R.string.measurement_instructions_dialog_fragment_screen1_description_text));
    }

    private void displayScreen2() {
        mDialogButton.setText(getActivity().getString(R.string.measurement_instructions_dialog_fragment_screen2_button_text));
        mDialogText.setText(getActivity().getString(R.string.measurement_instructions_dialog_fragment_screen2_description_text));
    }

    private MeasurementInstructionsDialogFragmentListener getListener() {
        MeasurementInstructionsDialogFragmentListener listener = (MeasurementInstructionsDialogFragmentListener) getTargetFragment();
        if (listener == null) {
            listener = (MeasurementInstructionsDialogFragmentListener) getActivity();
        }
        return listener;
    }

    //endregion

}
