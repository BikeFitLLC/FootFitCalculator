package com.bikefit.wedgecalculator.main;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bikefit.wedgecalculator.R;

import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Fragment that contains layout of the main menu of application.
 */
public class MainMenuFragment extends Fragment {

    //region CLASS VARIABLES -----------------------------------------------------------------------

    private Unbinder viewUnbinder;

    //endregion

    //region CONSTRUCTOR ---------------------------------------------------------------------------

    public static MainMenuFragment newInstance() {
        MainMenuFragment fragment = new MainMenuFragment();
        return fragment;
    }

    //endregion

    //region LIFECYCLE METHODS ---------------------------------------------------------------------

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.main_menu_fragment, container, false);
        viewUnbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        viewUnbinder.unbind();
    }

    //endregion

    //region ACCESSORS -----------------------------------------------------------------------------
    //endregion

    //region PUBLIC CLASS METHODS ------------------------------------------------------------------

    @OnClick(R.id.main_menu_fragment_orientation_video_button)
    public void onOrientationVideoButton() {

        OrientationVideoFragment fragment = new OrientationVideoFragment().newInstance("");
        showFragment(fragment, true);

    }

    //endregion

    //region PRIVATE METHODS -----------------------------------------------------------------------

    private void showFragment(Fragment fragment, boolean addToBackstack) {
        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.main_menu_activity_fragment, fragment);
        if (addToBackstack) {
            fragmentTransaction.addToBackStack(null);
        }
        fragmentTransaction.commit();
    }

    //endregion

}
