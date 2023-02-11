package org.example.ejb.model;

import org.example.common.model.AbstractMimeTypeImageResourceImpl;
import org.example.model.exception.ApplicationException;

import java.io.IOException;
import java.net.FileNameMap;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

public class URLImageResource extends AbstractMimeTypeImageResourceImpl {

    private final String url;
    private URLConnection urlConnection;


    public URLImageResource(String url) {
        this.url = url;
        try {

            this.urlConnection = new URL(url).openConnection();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException ioException) {
            throw new ApplicationException("Can't open connection to url: " + url, ioException);
        }
    }


    @Override
    protected void copyContent() throws IOException {
        Files.copy(urlConnection.getInputStream(), getTempPath(), StandardCopyOption.REPLACE_EXISTING);
    }

    @Override
    protected String getContentType() {
        FileNameMap fileNameMap = URLConnection.getFileNameMap();
        String contentTypeFor = fileNameMap.getContentTypeFor(url);
        return urlConnection.getContentType();
    }

    @Override
    protected void deleteTempResource() {

    }

    @Override
    public String toString() {
        return String.format("%s(%s)", this.getClass().getSimpleName(), url);
    }
}
