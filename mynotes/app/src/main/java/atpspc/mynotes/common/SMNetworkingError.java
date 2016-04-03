package com.studiomoob.fokas.common.smnetworking;

public class SMNetworkingError extends Error
{

    public enum SMNetworkingCodeError
    {
        SMNetworkingCodeErrorOffline,
        SMNetworkingCodeErrorHttp,
        SMNetworkingCodeErrorParserJSON,
        SMNetworkingCodeErrorTimeOut,
        SMNetworkingCodeErrorUnknown
    }

    private static final long serialVersionUID = 1L;
    private SMNetworkingCodeError code;
    private int                   httpStatusCode;

    public int getHttpStatusCode()
    {
        return httpStatusCode;
    }

    public void setHttpStatusCode(int httpStatusCode)
    {
        this.httpStatusCode = httpStatusCode;
    }

    public SMNetworkingError(SMNetworkingCodeError code, String message, int httpStatusCode)
    {
        super(message);
        this.code = code;
        this.httpStatusCode = httpStatusCode;
    }

    public SMNetworkingError(SMNetworkingCodeError code)
    {
        super();
        this.code = code;
    }

    public SMNetworkingCodeError getCode()
    {
        return code;
    }

    public void setCode(SMNetworkingCodeError code)
    {
        this.code = code;
    }

}
