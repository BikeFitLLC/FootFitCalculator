package com.bikefit.wedgecalculator.camera;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.afollestad.materialcamera.MaterialCamera;
import com.bikefit.wedgecalculator.BikeFitApplication;
import com.bikefit.wedgecalculator.R;
import com.bikefit.wedgecalculator.main.MainMenuActivity;
import com.bikefit.wedgecalculator.view.FootSide;
import com.squareup.leakcanary.RefWatcher;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Show Instructions on how to use the camera
 */
public class CameraInstructionsFragment extends Fragment {


    //region STATIC LOCAL CONSTANTS ----------------------------------------------------------------

    private final static int CAMERA_RQ = 6969;
    private final static int PERMISSION_RQ = 84;

    //todo parameterize this value when we start creating pictures for right foot
    private final static String SAVE_FOLDER_NAME = "leftfoot";

    //endregion

    //region INJECTED VIEWS ------------------------------------------------------------------------

    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    //endregion

    //region CLASS VARIABLES -----------------------------------------------------------------------

    private Unbinder viewUnbinder;
    private MaterialCamera materialCamera;
    private FootSide mFootSide = FootSide.LEFT;

    //endregion

    //region CONSTRUCTOR ---------------------------------------------------------------------------

    public static CameraInstructionsFragment newInstance(FootSide footSide) {
        CameraInstructionsFragment fragment = new CameraInstructionsFragment();
        Bundle args = new Bundle();
        args.putSerializable(FootSide.FOOTSIDE_KEY, footSide);

        fragment.setArguments(args);
        return fragment;
    }

    //endregion

    //region LIFECYCLE METHODS ---------------------------------------------------------------------

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.camera_instructions_fragment, container, false);
        viewUnbinder = ButterKnife.bind(this, view);

        mToolbar.setTitle(getResources().getString(R.string.camera_instructions_fragment_title_text, mFootSide.getLabel()));

        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Bundle args = getArguments();
        if (args != null) {
            mFootSide = (FootSide) args.getSerializable(FootSide.FOOTSIDE_KEY);
        } else {
            mFootSide = FootSide.LEFT;
        }

        //Shouldn't be needed if we're not using external storage
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            // Request permission to save videos in external storage
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSION_RQ);
        }

    }

    @Override
    public void onDestroyView() {
        materialCamera = null;

        super.onDestroyView();
        viewUnbinder.unbind();
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        RefWatcher refWatcher = BikeFitApplication.getRefWatcher(getActivity());
        refWatcher.watch(this);
    }

    //endregion

    //region WIDGET --------------------------------------------------------------------------------

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Received recording or error from MaterialCamera
        if (requestCode == CAMERA_RQ) {
            if (resultCode == Activity.RESULT_OK) {
                final File file = new File(data.getData().getPath());
                MeasurementFragment fragment = MeasurementFragment.newInstance(mFootSide, file.getAbsolutePath());
                ((MainMenuActivity) getActivity()).showFragment(fragment, true);
            } else if (data != null) {
                Exception e = (Exception) data.getSerializableExtra(MaterialCamera.ERROR_EXTRA);
                handleCameraResultError(e);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {
            // Sample was denied WRITE_EXTERNAL_STORAGE permission
            Toast.makeText(getActivity(), getActivity().getString(R.string.camera_instructions_fragment_permission_denied_text), Toast.LENGTH_LONG).show();
        }
    }

    //endregion


    //region LISTENERS -----------------------------------------------------------------------------

    @OnClick(R.id.camera_instructions_fragment_snapshot_button)
    public void onLaunchCameraButton() {

        File saveDir = new File(getActivity().getExternalFilesDir(null), SAVE_FOLDER_NAME);

        materialCamera = new MaterialCamera(this)
                .saveDir(saveDir)
                .showPortraitWarning(true)
                .allowRetry(true)
                .defaultToFrontFacing(false);

        materialCamera.stillShot();
        materialCamera.start(CAMERA_RQ);
    }

    @OnClick(R.id.toolbar)
    public void onToolbarBackPressed() {
        getActivity().onBackPressed();
    }

    //endregion

    //region PRIVATE METHODS -----------------------------------------------------------------------

    private void handleCameraResultError(Exception e) {
        String message = "";
        if (e != null) {
            e.printStackTrace();
            message = e.getMessage() == null ? "" : ": " + e.getMessage();
        }
        Toast.makeText(getActivity(), "Camera Error" + message, Toast.LENGTH_LONG).show();
    }

    //endregion

}
