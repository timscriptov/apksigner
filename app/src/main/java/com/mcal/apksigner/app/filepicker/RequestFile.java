package com.mcal.apksigner.app.filepicker;

import androidx.annotation.NonNull;

import java.io.InputStream;

/**
 * Created by radiationx on 12.01.17.
 */

public class RequestFile {
    private InputStream fileStream;
    private final String fileName;
    private final String mimeType;
    private String requestName;

    public RequestFile(String fileName, String mimeType, InputStream fileStream) {
        this.fileStream = fileStream;
        this.fileName = fileName;
        this.mimeType = mimeType;
    }

    public RequestFile(String requestName, String fileName, String mimeType, InputStream fileStream) {
        this.requestName = requestName;
        this.fileStream = fileStream;
        this.fileName = fileName;
        this.mimeType = mimeType;
    }

    public void setRequestName(String requestName) {
        this.requestName = requestName;
    }

    public InputStream getFileStream() {
        return fileStream;
    }

    public String getFileName() {
        return fileName;
    }

    public String getMimeType() {
        return mimeType;
    }

    public String getRequestName() {
        return requestName;
    }

    public void setFileStream(InputStream fileStream) {
        this.fileStream = fileStream;
    }

    @NonNull
    @Override
    public String toString() {
        return "RequestFile{" + fileName + ", " + mimeType + ", " + requestName + ", " + fileStream + "}";
    }
}
