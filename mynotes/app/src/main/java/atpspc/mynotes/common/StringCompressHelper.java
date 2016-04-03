package com.studiomoob.fokas.common.smnetworking;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

/**
 * Created by creatorbh on 11/10/14.
 */
public class StringCompressHelper
{
    public static byte[] compress(String string) throws IOException
    {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        GZIPOutputStream gzos = null;
        try
        {
            gzos = new GZIPOutputStream(baos);
            gzos.write(string.getBytes("UTF-8"));
        }
        finally
        {
            if (gzos != null)
            {
                try
                {
                    gzos.close();
                }
                catch (IOException ignore)
                {
                }
            }
        }

        return baos.toByteArray();
    }

    public static String decompress(String strCompressed)
    {
        String result = "";

        try
        {
            //byte[] compressed = Base64.decode(strCompressed, Base64.NO_WRAP);
            byte[] compressed = strCompressed.getBytes("UTF-8");
            if (compressed.length > 4)
            {
                GZIPInputStream gzipInputStream = new GZIPInputStream(new ByteArrayInputStream(compressed, 4, compressed.length - 4));

                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                for (int value = 0; value != -1; )
                {
                    value = gzipInputStream.read();
                    if (value != -1)
                    {
                        baos.write(value);
                    }
                }
                gzipInputStream.close();
                baos.close();
                return new String(baos.toByteArray(), "UTF-8");
            }
            else
            {
                return "";
            }

        }
        catch (Exception e)
        {
            //Log.v("asdf", "asdf");
        }

        return result;
    }

}
