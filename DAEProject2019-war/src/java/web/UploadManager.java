package web;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.logging.Logger;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.Part;
import util.URILookup;

@ManagedBean
@SessionScoped
public class UploadManager {
    private static final Logger logger = Logger.getLogger("web.AdministratorManager");
    Part file;
    
    String completePathFile;
    String filename;

    public UploadManager() {
    }

    public void upload() {
        
        if (file != null) {
            try {
                logger.warning("am-entrou0");
       
                filename = file.getSubmittedFileName();
                logger.warning("am-entrou1"+filename);
               
                completePathFile = URILookup.getServerDocumentsFolder() +"\\"+ filename;
                logger.warning("am-entrou2"+completePathFile);
                InputStream in = file.getInputStream();
                logger.warning("am-entrou3");
                FileOutputStream out = new FileOutputStream(completePathFile);

                logger.warning("am-entrou4");

                byte[] b = new byte[1024];
                int readBytes = in.read(b);
                logger.warning("am-entrou5");
                while (readBytes != -1) {
                    out.write(b, 0, readBytes);
                    readBytes = in.read(b);
                }
                logger.warning("am-entrou6");
                in.close();
                out.close();

                FacesMessage message = new FacesMessage("File: " + file.getName() + " uploaded successfully!");
                FacesContext.getCurrentInstance().addMessage(null, message);

            } catch (IOException e) {
                FacesMessage message = new FacesMessage("ERROR :: File: " + file.getName() + " not uploaded!");
                FacesContext.getCurrentInstance().addMessage(null, message);
            }
            logger.warning("am-entrou6");
        }
    }

    public Part getFile() {
        return file;
    }

    public void setFile(Part file) {
        this.file = file;
    }

    public String getCompletePathFile() {
        return completePathFile;
    }

    public void setCompletePathFile(String completePathFile) {
        this.completePathFile = completePathFile;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }
    public String getPath() throws UnsupportedEncodingException {

    String path = this.getClass().getClassLoader().getResource("").getPath();
    String fullPath = URLDecoder.decode(path, "UTF-8");
    String pathArr[] = fullPath.split("/WEB-INF/classes/");
    System.out.println(fullPath);
    System.out.println(pathArr[0]);
    fullPath = pathArr[0];

    return fullPath;

    }

    
}
