package com.bikefit.wedgecalculator.camera;

import android.Manifest;
import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.afollestad.materialcamera.MaterialCamera;
import com.bikefit.wedgecalculator.R;

import java.io.File;
import java.text.DecimalFormat;

import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class MaterialCameraFragment extends Fragment {

    //region CLASS VARIABLES -----------------------------------------------------------------------

    private final static int CAMERA_RQ = 6969;
    private final static int PERMISSION_RQ = 84;

    private Unbinder viewUnbinder;

    //endregion

    //region CONSTRUCTOR ---------------------------------------------------------------------------

    public MaterialCameraFragment() {
    }

    public static MaterialCameraFragment newInstance() {
        MaterialCameraFragment fragment = new MaterialCameraFragment();
        Bundle args = new Bundle();
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
        View view = inflater.inflate(R.layout.material_camera_fragment, container, false);
        viewUnbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //Shouldn't be needed if we're not using external storage
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            // Request permission to save videos in external storage
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSION_RQ);
        }

    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        viewUnbinder.unbind();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Received recording or error from MaterialCamera
        if (requestCode == CAMERA_RQ) {

            if (resultCode == Activity.RESULT_OK) {

                final File file = new File(data.getData().getPath());
                Toast.makeText(getActivity(), String.format("Saved to: %s, size: %s", file.getAbsolutePath(), fileSize(file)), Toast.LENGTH_LONG).show();

            } else if (data != null) {

                Exception e = (Exception) data.getSerializableExtra(MaterialCamera.ERROR_EXTRA);
                if (e != null) {
                    e.printStackTrace();
                    Toast.makeText(getActivity(), "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                }

            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {
            // Sample was denied WRITE_EXTERNAL_STORAGE permission
            Toast.makeText(getActivity(), "Videos will be saved in a cache directory instead of an external storage directory since permission was denied.", Toast.LENGTH_LONG).show();
        }
    }

    //endregion


    //region LISTENERS -----------------------------------------------------------------------------

    @OnClick(R.id.launchCameraStillshot)
    public void onLaunchCameraButton() {

        File saveDir = new File(getActivity().getExternalFilesDir(null), "leftFoot.jpg");
/*
        File saveDir = null;
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            saveDir = new File(getActivity().getExternalFilesDir(null), "leftFoot.jpg");
        }
*/

        MaterialCamera materialCamera = new MaterialCamera(this)
                .saveDir(saveDir)
                .showPortraitWarning(true)
                .allowRetry(true)
                .defaultToFrontFacing(false);

        materialCamera.stillShot(); // launches the Camera in stillshot mode
        materialCamera.start(CAMERA_RQ);
    }

    //endregion

    //region PRIVATE METHODS -----------------------------------------------------------------------

    private String readableFileSize(long size) {
        if (size <= 0) return size + " B";
        final String[] units = new String[]{"B", "KB", "MB", "GB", "TB"};
        int digitGroups = (int) (Math.log10(size) / Math.log10(1024));
        return new DecimalFormat("#,##0.##").format(size / Math.pow(1024, digitGroups)) + " " + units[digitGroups];
    }

    private String fileSize(File file) {
        return readableFileSize(file.length());
    }

    //endregion

}
