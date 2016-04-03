package com.studiomoob.fokas.common.smnetworking;

public interface SMResponseListener
{
    void success(Object obj, SMNetworkingResponse response);

    void fail(SMNetworkingError error);

}