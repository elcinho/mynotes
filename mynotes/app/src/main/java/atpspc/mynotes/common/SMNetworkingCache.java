package com.studiomoob.fokas.common.smnetworking;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import java.util.Calendar;

public class SMNetworkingCache
{
    SharedPreferences preferences;
    private static SMNetworkingCache _instance;

    private SMNetworkingCache(Context context)
    {
        preferences = PreferenceManager.getDefaultSharedPreferences(context);
    }

    public static SMNetworkingCache getInstance(Context context)
    {
        if (_instance == null)
        {
            _instance = new SMNetworkingCache(context);
        }

        return _instance;
    }

    public Boolean existCache(String url)
    {
        return preferences.contains(url);
    }

    public Boolean cacheIsExpired(String url, int timeoutCacheInSeconds)
    {
        Long timestamp = preferences.getLong("timestamp_" + url, 0);

        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(timestamp);
        cal.add(Calendar.SECOND, timeoutCacheInSeconds);

        return cal.before(Calendar.getInstance());
    }

    public String getCache(String url)
    {
        return preferences.getString(url, "");
    }

    public void setCache(String url, String value)
    {
        SharedPreferences.Editor editor = preferences.edit();

        Calendar cal = Calendar.getInstance();
        editor.putLong("timestamp_" + url, cal.getTimeInMillis());
        editor.putString(url, value);
        editor.commit();

    }
}
