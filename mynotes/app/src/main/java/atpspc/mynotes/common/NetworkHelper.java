package com.studiomoob.fokas.common.smnetworking;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class NetworkHelper
{

    public static boolean isOnline(Context context)
    {
        NetworkInfo netInfo = null;
        try
        {
            ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            netInfo = cm.getActiveNetworkInfo();
        }
        catch (Exception e)
        {

        }
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }
}
