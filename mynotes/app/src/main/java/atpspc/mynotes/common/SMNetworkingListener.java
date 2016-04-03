package com.studiomoob.fokas.common.smnetworking;

public interface SMNetworkingListener
{
    void loadDataInBackground(Object obj, SMNetworkingResponse response);

    void loadDataSuccess(Object obj, SMNetworkingResponse response);

    void loadDataFail(SMNetworkingError error, SMNetworkingResponse response);

}