package web;

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
                filename = extractFileName(file);//file.getSubmittedFileName();
                completePathFile = URILookup.getServerDocumentsFolder() +"\\"+ filename;

                InputStream in = file.getInputStream();
                FileOutputStream out = new FileOutputStream(completePathFile);


                byte[] b = new byte[1024];
                int readBytes = in.read(b);
            
                while (readBytes != -1) {
                    out.write(b, 0, readBytes);
                    readBytes = in.read(b);
                }

                in.close();
                out.close();

                FacesMessage message = new FacesMessage("File: " + filename + " uploaded successfully!");
                FacesContext.getCurrentInstance().addMessage(null, message);

            } catch (IOException e) {
                FacesMessage message = new FacesMessage("ERROR :: File: " + file.getName() + " not uploaded!");
                FacesContext.getCurrentInstance().addMessage(null, message);
            }
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
    
    private String extractFileName(Part part) {
        String contentDisp = part.getHeader("content-disposition");
        String[] items = contentDisp.split(";");
        for (String s : items) {
            if (s.trim().startsWith("filename")) {
                return s.substring(s.indexOf("=") + 2, s.length()-1);
            }
        }
        return "";
    }
    
}
