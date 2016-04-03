package com.studiomoob.fokas.common.smnetworking;

public class SMNetworkingResponse
{
    private SMNetworkingError error = null;
    private int                 httpStatusCode;
    private String              responseString;
    private SMNetworkingRequest request;

    public SMNetworkingResponse(int statusCode, String responseString)
    {
        super();
        this.httpStatusCode = statusCode;
        this.responseString = responseString;
    }

    public SMNetworkingRequest getRequest()
    {
        return request;
    }

    public void setRequest(SMNetworkingRequest request)
    {
        this.request = request;
    }

    public SMNetworkingError getError()
    {
        return error;
    }

    public void setError(SMNetworkingError error)
    {
        this.error = error;
    }

    public int getHttpStatusCode()
    {
        return httpStatusCode;
    }

    public void setHttpStatusCode(int httpStatusCode)
    {
        this.httpStatusCode = httpStatusCode;
    }

    public String getResponseString()
    {
        return responseString;
    }

    public void setResponseString(String responseString)
    {
        this.responseString = responseString;
    }

    public SMNetworkingResponse()
    {
        super();
    }
}
