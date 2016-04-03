package com.studiomoob.fokas.common.smnetworking;

import java.io.File;

public class SMNameFilePair
{
    private String name;
    private File   file;

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public File getFile()
    {
        return file;
    }

    public void setFile(File file)
    {
        this.file = file;
    }

    public SMNameFilePair(String name, File file)
    {
        super();
        this.name = name;
        this.file = file;
    }


}
