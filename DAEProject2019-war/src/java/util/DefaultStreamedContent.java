/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

import java.io.InputStream;

/**
 *
 * @author Joao Marquez
 */
public class DefaultStreamedContent implements StreamedContent {

    private InputStream stream;

    private String contentType;

    private String name;

    private String contentEncoding;

    private Integer contentLength;

    public DefaultStreamedContent() {
    }

    public DefaultStreamedContent(InputStream stream) {
        this.stream = stream;
    }

    public DefaultStreamedContent(InputStream stream, String contentType) {
        this(stream);
        this.contentType = contentType;
    }

    public DefaultStreamedContent(InputStream stream, String contentType, String name) {
        this(stream, contentType);
        this.name = name;
    }

    public DefaultStreamedContent(InputStream stream, String contentType, String name, String contentEncoding) {
        this(stream, contentType, name);
        this.contentEncoding = contentEncoding;
    }

    public DefaultStreamedContent(InputStream stream, String contentType, String name, Integer contentLength) {
        this(stream, contentType, name);
        this.contentLength = contentLength;
    }

    public DefaultStreamedContent(InputStream stream, String contentType, String name, String contentEncoding, Integer contentLength) {
        this(stream, contentType, name);
        this.contentEncoding = contentEncoding;
        this.contentLength = contentLength;
    }

    @Override
    public InputStream getStream() {
        return stream;
    }

    public void setStream(InputStream stream) {
        this.stream = stream;
    }

    @Override
    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    @Override
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getContentEncoding() {
        return contentEncoding;
    }

    public void setContentEncoding(String contentEncoding) {
        this.contentEncoding = contentEncoding;
    }

    @Override
    public Integer getContentLength() {
        return contentLength;
    }
}
