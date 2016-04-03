package com.studiomoob.fokas.common.smnetworking;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Base64;

import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.SocketTimeoutException;
import java.net.URL;

public class SMNetworkingFileDownloader
{
    protected class ProgressTask extends AsyncTask<String, Void, Boolean>
    {
        private SMNetworkingRequestFileDownloader request;
        private SMNetworkingResponse              response;

        public ProgressTask(SMNetworkingRequestFileDownloader request)
        {
            this.request = request;
        }

        protected void onPreExecute()
        {

        }

        @Override
        protected void onPostExecute(final Boolean success)
        {
            SMNetworkingFileDownloader.this.loadData(response);
        }

        protected Boolean doInBackground(final String... args)
        {

            if (NetworkHelper.isOnline(SMNetworkingFileDownloader.this.context))
            {
                switch (request.getHttpMethod())
                {
                    case HttpMethodGet:

                        response = SMNetworkingFileDownloader.this.executeRequest(request);

                        break;
                    case HttpMethodPost:

                        response = SMNetworkingFileDownloader.this.executeRequest(request);

                        break;

                    default:
                        break;
                }
            }
            else
            {
                response = new SMNetworkingResponse();
                response.setError(new SMNetworkingError(SMNetworkingError.SMNetworkingCodeError.SMNetworkingCodeErrorOffline, "Offline", 0));
            }
            response.setRequest(request);

            return true;
        }
    }

    protected Context              context;
    private   SMNetworkingListener listener;

    public SMNetworkingFileDownloader(Context context)
    {
        super();
        this.context = context;
    }

    public void getFile(SMNetworkingRequestFileDownloader request, SMNetworkingListener listener)
    {
        request.setRequestType(SMNetworkingRequest.SMNetworkingRequestType.SMNetworkingRequestTypeJSON);
        this.listener = listener;

        new ProgressTask(request).execute();
    }

    public void postFile(SMNetworkingRequestFileDownloader request, SMNetworkingListener listener)
    {
        request.setRequestType(SMNetworkingRequest.SMNetworkingRequestType.SMNetworkingRequestTypeJSON);
        this.listener = listener;

        new ProgressTask(request).execute();
    }

    private SMNetworkingResponse executeRequest(SMNetworkingRequestFileDownloader request)
    {
        SMNetworkingResponse httpResponse = new SMNetworkingResponse();
        int statusCode = 0;

        try
        {
            HttpURLConnection connection = null;

            String params = "";
            if (request.getParameters() != null && request.getParameters().size() > 0)
            {
                for (NameValuePair nameValuePair : request.getParameters())
                {
                    params = params + nameValuePair.getName() + "=" + nameValuePair.getValue() + "&";
                }
                params = params.substring(0, params.length() - 1);
            }

            switch (request.getHttpMethod())
            {
                case HttpMethodGet:

                    connection = (HttpURLConnection) new URL(request.getUrl() + "?" + params).openConnection();
                    connection.setRequestMethod("GET");

                    break;

                case HttpMethodPost:

                    connection = (HttpURLConnection) new URL(request.getUrl() + params).openConnection();
                    connection.setDoOutput(true);
                    connection.setRequestMethod("POST");
                    connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                    connection.setRequestProperty("Content-Length", "" + Integer.toString(params.getBytes().length));

                    DataOutputStream wr = new DataOutputStream(connection.getOutputStream());
                    wr.writeBytes(params);
                    wr.flush();
                    wr.close();

                    break;

                default:
                    break;
            }

            //Headers
            if (request.getHeaders() != null && request.getHeaders().size() > 0)
            {
                for (NameValuePair nameValuePair : request.getHeaders())
                {
                    connection.setRequestProperty(nameValuePair.getName(), nameValuePair.getValue());
                }
            }
            //Authenticate
            if (request.getAuthorization())
            {
                connection.setRequestProperty("Authorization", "Basic " + Base64.encodeToString((request.getUser() + ":" + request.getPassword()).getBytes(), Base64.NO_WRAP));
            }

            connection.setRequestProperty("charset", "utf-8");
            connection.setDoInput(true);

            connection.setConnectTimeout(request.getTimeout());
            connection.setUseCaches(false);

            connection.connect();

            statusCode = connection.getResponseCode();

            if (statusCode == 200)
            {
                InputStream input = connection.getInputStream();
                File fileOutPut = new File(request.getDownloadLocalPath());

                //Verificando se existe o diretório
                File dirOutput = new File(fileOutPut.getParent());
                if (dirOutput.exists())
                {
                    this.deleteRecursive(dirOutput);
                }
                dirOutput.mkdirs();

                FileOutputStream fOut = new FileOutputStream(fileOutPut);

                byte[] buffer = new byte[4096];
                int bytesRead = -1;
                while ((bytesRead = input.read(buffer)) != -1)
                {
                    fOut.write(buffer, 0, bytesRead);
                }
                fOut.flush();
                fOut.close();
            }
            else
            {
                httpResponse.setError(new SMNetworkingError(SMNetworkingError.SMNetworkingCodeError.SMNetworkingCodeErrorHttp, "HTTP Error.", statusCode));
            }
        }
        catch (SocketTimeoutException e)
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
        catch (Exception e)
        {
            httpResponse.setError(new SMNetworkingError(SMNetworkingError.SMNetworkingCodeError.SMNetworkingCodeErrorUnknown, e.getMessage(), statusCode));
            e.printStackTrace();
        }
        httpResponse.setHttpStatusCode(statusCode);
        return httpResponse;
    }

    private void deleteRecursive(File fileOrDirectory)
    {
        if (fileOrDirectory.isDirectory())
        {
            for (File child : fileOrDirectory.listFiles())
            {
                this.deleteRecursive(child);
            }
        }

        fileOrDirectory.delete();
    }


    private void loadData(SMNetworkingResponse response)
    {
        if (listener != null)
        {
            if (response.getError() == null)
            {
                listener.loadDataSuccess(true, response);
            }
            else
            {
                listener.loadDataFail(response.getError(), response);
            }
        }
    }
}
