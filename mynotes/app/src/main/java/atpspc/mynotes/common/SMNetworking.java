package com.studiomoob.fokas.common.smnetworking;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Base64;
import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MIME;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HttpContext;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.zip.GZIPInputStream;

public class SMNetworking
{
    protected class ProgressTask extends AsyncTask<String, Void, Boolean>
    {
        private SMNetworkingRequest  request;
        private SMNetworkingResponse response;

        public ProgressTask(SMNetworkingRequest request)
        {
            this.request = request;
        }

        protected void onPreExecute()
        {

        }

        @Override
        protected void onPostExecute(final Boolean success)
        {
            if (listener != null && responseError == null)
            {
                listener.loadDataSuccess(responseObject, response);
            }
            else
            {
                listener.loadDataFail(responseError, response);
            }
        }

        protected Boolean doInBackground(final String... args)
        {

            if (NetworkHelper.isOnline(SMNetworking.this.context))
            {
                switch (request.getHttpMethod())
                {
                    case HttpMethodGet:

                        if (request.getUseCache() && cache.existCache(request.getUrl()) && !cache.cacheIsExpired(request.getUrl(), request.getTimeoutCacheInSeconds()))
                        {
                            response = new SMNetworkingResponse(0, cache.getCache(request.getUrl()));
                        }
                        else
                        {
                            response = SMNetworking.this.executeRequest(request);
                        }

                        break;
                    case HttpMethodPut:

                        if (request.getUseCache() && cache.existCache(request.getUrl()) && !cache.cacheIsExpired(request.getUrl(), request.getTimeoutCacheInSeconds()))
                        {
                            response = new SMNetworkingResponse(0, cache.getCache(request.getUrl()));
                        }
                        else
                        {
                            response = SMNetworking.this.executeRequest(request);
                        }

                        break;
                    case HttpMethodPost:

                        if (request.getUseCache() && cache.existCache(request.getUrl()) && !cache.cacheIsExpired(request.getUrl(), request.getTimeoutCacheInSeconds()))
                        {
                            response = new SMNetworkingResponse(0, cache.getCache(request.getUrl()));
                        }
                        else
                        {
                            response = SMNetworking.this.executeRequest(request);
                        }

                        break;
                    case HttpMethodDelete:

                        if (request.getUseCache() && cache.existCache(request.getUrl()) && !cache.cacheIsExpired(request.getUrl(), request.getTimeoutCacheInSeconds()))
                        {
                            response = new SMNetworkingResponse(0, cache.getCache(request.getUrl()));
                        }
                        else
                        {
                            response = SMNetworking.this.executeRequest(request);
                        }

                        break;

                    default:
                        break;
                }
            }
            else
            {
                response = new SMNetworkingResponse();
                if (request.getUseOfflineData())
                {
                    String cacheResponse = cache.getCache(request.getUrl());
                    if (cacheResponse.length() > 0 && cacheResponse != null)
                    {
                        response.setResponseString(cacheResponse);
                    }
                    else
                    {
                        response.setError(new SMNetworkingError(SMNetworkingError.SMNetworkingCodeError.SMNetworkingCodeErrorOffline, "Offline", 0));
                    }
                }
                else
                {
                    response.setError(new SMNetworkingError(SMNetworkingError.SMNetworkingCodeError.SMNetworkingCodeErrorOffline, "Offline", 0));
                }
            }
            response.setRequest(request);

            //Parse response data
            SMNetworking.this.loadData(response);

            SMNetworking.this.loadDataInBackground(response);

            return true;
        }
    }

    SMNetworkingCache cache;
    protected ClientConnectionManager clientConnectionManager;
    protected HttpContext             httpContext;
    public    HttpParams              httpParams;
    protected Context                 context;
    private   SMNetworkingListener    listener;

    private SMNetworkingError responseError;
    private Object            responseObject;

    public SMNetworking(Context context)
    {
        super();
        this.context = context;
        cache = SMNetworkingCache.getInstance(context);
    }

    public void execute(SMNetworkingRequest request, SMNetworkingListener listener)
    {
        this.listener = listener;
        Log.v("SMNetworking Start ", request.getUrl());
        new ProgressTask(request).execute();
    }

    private SMNetworkingResponse executeRequest(SMNetworkingRequest request)
    {
        String responseString = null;
        StringBuilder builder = new StringBuilder();
        SMNetworkingResponse httpResponse = new SMNetworkingResponse();
        int statusCode = 0;

        HttpClient client = null;

        if (request.getIgnoreSSL())
        {

            client = new SMHttpClient(context, clientConnectionManager, httpParams);


        }
        else
        {
            client = new DefaultHttpClient(clientConnectionManager, httpParams);
        }

        try
        {
            HttpResponse response = null;
            switch (request.getHttpMethod())
            {
                case HttpMethodGet:

                    String params = "";

                    if (request.getParameters() != null && request.getParameters().size() > 0)
                    {
                        params = "?";

                        for (NameValuePair nameValuePair : request.getParameters())
                        {
                            params = params + nameValuePair.getName() + "=" + nameValuePair.getValue() + "&";
                        }

                        params = params.substring(0, params.length() - 1);
                    }

                    HttpGet httpGet = new HttpGet(request.getUrl() + params);
                    if (request.getAuthorization())
                    {
                        httpGet.addHeader("Authorization", "Basic " + Base64.encodeToString((request.getUser() + ":" + request.getPassword()).getBytes(), Base64.NO_WRAP));
                    }

                    if (request.getHeaders() != null && request.getHeaders().size() > 0)
                    {
                        for (NameValuePair nameValuePair : request.getHeaders())
                        {
                            httpGet.addHeader(nameValuePair.getName(), nameValuePair.getValue());
                        }
                    }

                    httpGet.setParams(client.getParams());
                    response = client.execute(httpGet, httpContext);

                    break;

                case HttpMethodPut:

                    HttpPut httpPut = new HttpPut(request.getUrl());
                    httpPut.setHeader("charset", "utf-8");
                    if (request.getParameters() != null && request.getParameters().size() > 0)
                    {
                        httpPut.setEntity(new UrlEncodedFormEntity(request.getParameters()));
                    }

                    if (request.getHeaders() != null && request.getHeaders().size() > 0)
                    {
                        for (NameValuePair nameValuePair : request.getHeaders())
                        {
                            httpPut.addHeader(nameValuePair.getName(), nameValuePair.getValue());
                        }
                    }

                    if (request.getAuthorization())
                    {
                        httpPut.addHeader("Authorization", "Basic " + Base64.encodeToString((request.getUser() + ":" + request.getPassword()).getBytes(), Base64.NO_WRAP));
                    }

                    response = client.execute(httpPut, httpContext);

                    break;
                case HttpMethodDelete:

                    HttpDelete httpDelete = new HttpDelete(request.getUrl());
                    httpDelete.setHeader("charset", "utf-8");

                    if (request.getHeaders() != null && request.getHeaders().size() > 0)
                    {
                        for (NameValuePair nameValuePair : request.getHeaders())
                        {
                            httpDelete.addHeader(nameValuePair.getName(), nameValuePair.getValue());
                        }
                    }

                    if (request.getAuthorization())
                    {
                        httpDelete.addHeader("Authorization", "Basic " + Base64.encodeToString((request.getUser() + ":" + request.getPassword()).getBytes(), Base64.NO_WRAP));
                    }

                    response = client.execute(httpDelete, httpContext);

                    break;
                case HttpMethodPost:

                    HttpPost httpPost = new HttpPost(request.getUrl());
                    httpPost.setHeader("charset", "utf-8");
                    HttpEntity entity = null;

                    if (request.getParameters() != null && request.getParameters().size() > 0 || (request.getHTTPBody() != null && !request.getHTTPBody().trim().equalsIgnoreCase("")))
                    {
                        switch (request.getParameterType())
                        {
                            case SMNetworkingParameterTypeDefault:
                                entity = new UrlEncodedFormEntity(request.getParameters(), "utf-8");
                                httpPost.addHeader(entity.getContentType());
                                httpPost.setEntity(entity);

                                break;

                            case SMNetworkingParameterTypeEmbebedJSON:

                                httpPost.setHeader("Content-Type", request.getContentType());
                                if (request.getParameters().size() > 0)
                                {
                                    httpPost.setEntity(new StringEntity(request.getParameters().get(0).getValue()));
                                }
                                break;
                            case SMNetworkingParameterTypeCompressedBody:

                                httpPost.setHeader("Content-Type", request.getContentType());
                                if (request.getHTTPBody() != null)
                                {
                                    httpPost.setEntity(new ByteArrayEntity(StringCompressHelper.compress(request.getHTTPBody())));
                                }
                                break;
                            case SMNetworkingParameterTypeMultipart:

                                MultipartEntityBuilder multipartBuilder = MultipartEntityBuilder.create();
                                multipartBuilder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);

                                if (request.getFileParameters() != null)
                                {
                                    for (SMNameFilePair filePair : request.getFileParameters())
                                    {
                                        FileBody fb = new FileBody(filePair.getFile());
                                        multipartBuilder.addPart(filePair.getName(), fb);
                                    }
                                }

                                if(request.getParameters() != null)
                                {
                                    for (NameValuePair basicNameValuePair : request.getParameters())
                                    {
                                        multipartBuilder.addTextBody(basicNameValuePair.getName(), basicNameValuePair.getValue(), ContentType.create("text/plain", MIME.UTF8_CHARSET));
                                    }
                                }

                                httpPost.setEntity(multipartBuilder.build());

                                break;
                        }

                    }
                    if (request.getAuthorization())
                    {
                        httpPost.addHeader("Authorization", "Basic " + Base64.encodeToString((request.getUser() + ":" + request.getPassword()).getBytes(), Base64.NO_WRAP));
                    }

                    if (request.getHeaders() != null && request.getHeaders().size() > 0)
                    {
                        for (NameValuePair nameValuePair : request.getHeaders())
                        {
                            httpPost.addHeader(nameValuePair.getName(), nameValuePair.getValue());
                        }
                    }

                    response = client.execute(httpPost, httpContext);

                    break;
                default:
                    break;
            }

            StatusLine statusLine = response.getStatusLine();
            statusCode = statusLine.getStatusCode();

            HttpEntity entity = response.getEntity();
            InputStream content = entity.getContent();

            if (request.getRequestType() == SMNetworkingRequest.SMNetworkingRequestType.SMNetworkingRequestTypeCompressedJSON)
            {
                content = new GZIPInputStream(content);
            }

            BufferedReader reader = new BufferedReader(request.getEnconding() != null ? new InputStreamReader(content, request.getEnconding()) : new InputStreamReader(content));


            String line;

            while ((line = reader.readLine()) != null)
            {
                builder.append(line);
            }
            responseString = builder.toString();

            //Log.v("SMNetworking", "responseString: " + responseString);

            if (statusCode == 200)
            {
                if (request.getUseCache() || request.getUseOfflineData())
                {
                    cache.setCache(request.getUrl(), responseString);
                }
            }
        }
        catch (ConnectTimeoutException e)
        {
            httpResponse.setError(new SMNetworkingError(SMNetworkingError.SMNetworkingCodeError.SMNetworkingCodeErrorTimeOut, e.getMessage(), statusCode));
        }
        catch (ClientProtocolException e)
        {
            httpResponse.setError(new SMNetworkingError(SMNetworkingError.SMNetworkingCodeError.SMNetworkingCodeErrorUnknown, e.getMessage(), statusCode));
            e.printStackTrace();
        }
        catch (IOException e)
        {
            httpResponse.setError(new SMNetworkingError(SMNetworkingError.SMNetworkingCodeError.SMNetworkingCodeErrorUnknown, e.getMessage(), statusCode));
            e.printStackTrace();
        }

        httpResponse.setHttpStatusCode(statusCode);
        httpResponse.setResponseString(responseString);

        return httpResponse;
    }

    private void loadDataInBackground(SMNetworkingResponse response)
    {
        if (listener != null && responseError == null)
        {
            listener.loadDataInBackground(responseObject, response);
        }
    }

    private void loadData(SMNetworkingResponse response)
    {
        if (listener != null)
        {
            if (response.getError() == null)
            {
                if (response.getResponseString() != null && !response.getResponseString().trim().equals(""))
                {
                    switch (response.getRequest().getRequestType())
                    {
                        case SMNetworkingRequestTypeJSON:
                        case SMNetworkingRequestTypeCompressedJSON:

                            try
                            {
                                responseObject = new JSONArray(response.getResponseString());
                            }
                            catch (JSONException e)
                            {
                                try
                                {
                                    responseObject = new JSONObject(response.getResponseString());
                                }
                                catch (JSONException e1)
                                {
                                    responseError = new SMNetworkingError(SMNetworkingError.SMNetworkingCodeError.SMNetworkingCodeErrorParserJSON, "JSON parser error", 0);
                                }
                            }
                            catch (Exception e)
                            {
                                responseError = new SMNetworkingError(SMNetworkingError.SMNetworkingCodeError.SMNetworkingCodeErrorParserJSON, "JSON parser error", 0);
                            }

                            break;
                        case SMNetworkingRequestTypeString:

                            try
                            {
                                responseObject = response.getResponseString();
                            }
                            catch (Exception e)
                            {
                                responseError = new SMNetworkingError(SMNetworkingError.SMNetworkingCodeError.SMNetworkingCodeErrorParserJSON, "Data string error", 0);
                            }

                            break;

                        default:
                            break;
                    }
                }
                else
                {
                    responseObject = null;
                }
            }
            else
            {
                responseError = response.getError();
            }
            Log.v("SMNetworking End ", response.getRequest().getUrl());
        }
    }
}
