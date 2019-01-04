/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

/**
 *
 * @author Joao Marquez
 */
import java.io.InputStream;

public interface StreamedContent {

    public String getName();

    public InputStream getStream();

    public String getContentType();

    public String getContentEncoding();

    public Integer getContentLength();

}
