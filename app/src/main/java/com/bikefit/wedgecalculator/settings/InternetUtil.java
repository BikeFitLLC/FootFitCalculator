package com.bikefit.wedgecalculator.settings;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;

import com.bikefit.wedgecalculator.BikeFitApplication;

/**
 * Internet Connection Utilities
 */
public class InternetUtil {

    /**
     * Check that the network is connected and available
     *
     * @return if network status is available (true/false)
     */
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

    /**
     * Open an external web page of a specified URL
     *
     * @param url URL to open
     */
    public void openExternalWebPage(String url) {
        Uri webpage = Uri.parse(url);
        Intent intent = new Intent(Intent.ACTION_VIEW, webpage);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (intent.resolveActivity(BikeFitApplication.getInstance().getPackageManager()) != null) {
            BikeFitApplication.getInstance().startActivity(intent);
        }
    }

}
