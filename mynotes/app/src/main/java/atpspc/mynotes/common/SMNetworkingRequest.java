package com.studiomoob.fokas.common.smnetworking;

import org.apache.http.NameValuePair;

import java.util.ArrayList;
import java.util.List;


public class SMNetworkingRequest
{
    public enum SMHttpMethod
    {
        HttpMethodGet,
        HttpMethodPost,
        HttpMethodPut,
        HttpMethodDelete
    }

    public enum SMNetworkingRequestType
    {
        SMNetworkingRequestTypeJSON,
        SMNetworkingRequestTypeXML,
        SMNetworkingRequestTypeCompressedJSON,
        SMNetworkingRequestTypeString
    }

    public enum SMNetworkingParameterType
    {
        SMNetworkingParameterTypeDefault,
        SMNetworkingParameterTypeEmbebedJSON,
        SMNetworkingParameterTypeCompressedBody,
        SMNetworkingParameterTypeMultipart
    }

    private Boolean                   authorization;
    private String                    user;
    private String                    password;
    private Boolean                   async;
    private Boolean                   useCache;
    private String                    url;
    private int                       timeout;
    private int                       timeoutCacheInSeconds;
    private Boolean                   useOfflineData;
    private SMHttpMethod              httpMethod;
    private SMNetworkingRequestType   requestType;
    private String                    contentType;
    private SMNetworkingParameterType parameterType;
    List<NameValuePair> parameters;
    List<NameValuePair> headers;
    private String enconding;
    List<SMNameFilePair> fileParameters;
    private Boolean ignoreSSL;
    private String  HTTPBody;


    public SMNetworkingRequest()
    {
        super();

        this.async = true;
        this.useCache = false;
        this.timeoutCacheInSeconds = LibraryConfig.DEFAULT_TIMEOUT;
        this.useOfflineData = false;
        this.httpMethod = SMHttpMethod.HttpMethodGet;
        this.parameterType = SMNetworkingRequest.SMNetworkingParameterType.SMNetworkingParameterTypeMultipart;
        this.requestType = SMNetworkingRequest.SMNetworkingRequestType.SMNetworkingRequestTypeJSON;
        this.contentType = "application/x-www-form-urlencoded";
        this.headers = new ArrayList<>();
        this.authorization = true;
        this.ignoreSSL = true;
        this.timeout = 100000;
    }

    public int getTimeout()
    {
        return timeout;
    }

    public void setTimeout(int timeout)
    {
        this.timeout = timeout;
    }

    public List<NameValuePair> getHeaders()
    {
        return headers;
    }

    public void setHeaders(List<NameValuePair> headers)
    {
        this.headers = headers;
    }

    public Boolean getAuthorization()
    {
        return authorization;
    }

    public void setAuthorization(Boolean authorization)
    {
        this.authorization = authorization;
    }

    public String getUser()
    {
        return user;
    }

    public void setUser(String user)
    {
        this.user = user;
    }

    public String getPassword()
    {
        return password;
    }

    public void setPassword(String password)
    {
        this.password = password;
    }

    public String getContentType()
    {
        return contentType;
    }

    public void setContentType(String contentType)
    {
        this.contentType = contentType;
    }

    public SMNetworkingParameterType getParameterType()
    {
        return parameterType;
    }

    public void setParameterType(SMNetworkingParameterType parameterType)
    {
        this.parameterType = parameterType;
    }

    public List<NameValuePair> getParameters()
    {
        return parameters;
    }

    public void setParameters(List<NameValuePair> parameters)
    {
        this.parameters = parameters;
    }

    public List<SMNameFilePair> getFileParameters()
    {
        return fileParameters;
    }

    public void setFileParameters(List<SMNameFilePair> fileParameters)
    {
        this.fileParameters = fileParameters;
    }

    public SMNetworkingRequestType getRequestType()
    {
        return requestType;
    }

    public void setRequestType(SMNetworkingRequestType requestType)
    {
        this.requestType = requestType;
    }

    public SMHttpMethod getHttpMethod()
    {
        return httpMethod;
    }

    public void setHttpMethod(SMHttpMethod httpMethod)
    {
        this.httpMethod = httpMethod;
    }

    public Boolean getUseOfflineData()
    {
        return useOfflineData;
    }

    public void setUseOfflineData(Boolean useOfflineData)
    {
        this.useOfflineData = useOfflineData;
    }

    public String getUrl()
    {
        return url;
    }

    public void setUrl(String url)
    {
        this.url = url;
    }

    public int getTimeoutCacheInSeconds()
    {
        return timeoutCacheInSeconds;
    }

    public void setTimeoutCacheInSeconds(int timeoutCacheInSeconds)
    {
        this.timeoutCacheInSeconds = timeoutCacheInSeconds;
    }

    public Boolean getAsync()
    {
        return async;
    }

    public void setAsync(Boolean async)
    {
        this.async = async;
    }

    public Boolean getUseCache()
    {
        return useCache;
    }

    public void setUseCache(Boolean useCache)
    {
        this.useCache = useCache;
    }

    public String getEnconding()
    {
        return enconding;
    }

    public void setEnconding(String enconding)
    {
        this.enconding = enconding;
    }

    public Boolean getIgnoreSSL()
    {
        return ignoreSSL;
    }

    public void setIgnoreSSL(Boolean ignoreSSL)
    {
        this.ignoreSSL = ignoreSSL;
    }

    public String getHTTPBody()
    {
        return HTTPBody;
    }

    public void setHTTPBody(String HTTPBody)
    {
        this.HTTPBody = HTTPBody;
    }
}
