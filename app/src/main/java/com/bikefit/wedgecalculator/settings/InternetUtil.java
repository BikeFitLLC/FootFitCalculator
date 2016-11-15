package com.bikefit.wedgecalculator.settings;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.bikefit.wedgecalculator.BikeFitApplication;

/**
 * Internet Connection Utilities
 */
public class InternetUtil {

    public boolean checkInternet() {
        boolean networkConnected;
        boolean networkAvailable;

        ConnectivityManager connectivityManager = (ConnectivityManager) BikeFitApplication.getInstance().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        if (networkInfo != null) {
            networkConnected = networkInfo.isConnected();
            networkAvailable = networkInfo.isAvailable();
        } else {
            networkConnected = false;
            networkAvailable = false;
        }

        return (networkConnected && networkAvailable);
    }

}
