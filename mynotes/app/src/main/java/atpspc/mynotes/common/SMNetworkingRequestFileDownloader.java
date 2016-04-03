package com.studiomoob.fokas.common.smnetworking;

public class SMNetworkingRequestFileDownloader extends SMNetworkingRequest
{

    private String downloadLocalPath;

    public SMNetworkingRequestFileDownloader(Boolean async, Boolean useCache, String url, int timeoutCacheInSeconds, Boolean useOfflineData, SMHttpMethod httpMethod, SMNetworkingParameterType parameterType, String downloadLocalPath)
    {
        super();
        this.setAsync(async);
        this.setUseCache(useCache);
        this.setUrl(url);
        this.setTimeoutCacheInSeconds(timeoutCacheInSeconds);
        this.setUseOfflineData(useOfflineData);
        this.setHttpMethod(httpMethod);
        this.setParameterType(parameterType);

        this.downloadLocalPath = downloadLocalPath;
    }

    public SMNetworkingRequestFileDownloader()
    {
        super();
    }

    public String getDownloadLocalPath()
    {
        return downloadLocalPath;
    }

    public void setDownloadLocalPath(String downloadLocalPath)
    {
        this.downloadLocalPath = downloadLocalPath;
    }

}
