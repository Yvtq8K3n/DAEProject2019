/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package web;

import dtos.ArtifactDTO;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.logging.Logger;
import util.DefaultStreamedContent;
import util.StreamedContent;
import javax.faces.bean.ManagedBean;

/**
 *
 * @author Joao Marquez
 */
@ManagedBean
public class DownloadManager {
     
    private static final Logger logger = Logger.getLogger("web.DownloadManager");
    
    public StreamedContent getDocumentFile(ArtifactDTO artifactDTO) {
        try {
            InputStream in = new FileInputStream(artifactDTO.getFilepath());
            return new DefaultStreamedContent(in, artifactDTO.getMimeType(), artifactDTO.getDesiredName());
        } catch (Exception  e) {
            FacesExceptionHandler.handleException(e, "Could not download the file", logger);
            return null;
        }
    }    
}