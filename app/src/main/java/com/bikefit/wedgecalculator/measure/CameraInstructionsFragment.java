package com.bikefit.wedgecalculator.measure;

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
import com.bikefit.wedgecalculator.measure.model.FootSide;
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

    //endregion

    //region INJECTED VIEWS ------------------------------------------------------------------------

    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    //endregion

    //region CLASS VARIABLES -----------------------------------------------------------------------

    private Unbinder mViewUnBinder;
    private MaterialCamera materialCamera;
    private FootSide mFootSide = FootSide.LEFT;
    private String mFolderName = FootSide.LEFT.toString();

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
        mViewUnBinder = ButterKnife.bind(this, view);
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

        mToolbar.setTitle(getResources().getString(R.string.camera_instructions_fragment_title_text, mFootSide.getLabel()));
        mToolbar.setNavigationOnClickListener(mNavigationListener);

        mFolderName = mFootSide.toString();

        //Shouldn't be needed if we're not using external storage
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            // Request permission to save videos in external storage
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSION_RQ);
        }

    }

    @Override
    public void onDestroyView() {
        materialCamera = null;
        mToolbar = null;
        mFootSide = null;
        mFolderName = null;

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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Received recording or error from MaterialCamera
        if (requestCode == CAMERA_RQ) {
            if (resultCode == Activity.RESULT_OK) {
                String path = data.getData().getPath();
                MeasurementFragment fragment = MeasurementFragment.newInstance(mFootSide, path);
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

        final File saveDir = new File(getActivity().getExternalFilesDir(null), mFolderName);
        int color = ContextCompat.getColor(getActivity().getApplicationContext(), R.color.materialCameraBottomBarColor);

        materialCamera = new MaterialCamera(this)
                .saveDir(saveDir)
                .showPortraitWarning(true)
                .allowRetry(true)
                .defaultToFrontFacing(false)
                .primaryColor(color);

        materialCamera.stillShot();
        materialCamera.start(CAMERA_RQ);
    }

    @OnClick(R.id.camera_instructions_fragment_more_button)
    public void onTellMeMoreButton() {
        // "Tell me more" button - not implemented yet
    }

    View.OnClickListener mNavigationListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            getActivity().onBackPressed();
        }
    };


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
