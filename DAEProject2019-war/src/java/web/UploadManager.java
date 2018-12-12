package web;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.FileSystems;
import java.util.logging.Logger;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import org.primefaces.model.UploadedFile;
import util.URILookup;

@ManagedBean
@SessionScoped
public class UploadManager {
    private static final Logger logger = Logger.getLogger("web.AdministratorManager");
    UploadedFile file;
    
    String completePathFile;
    String filename;

    public UploadManager() {
    }

    public void upload() {
        
        if (file != null) {
            try {
                logger.warning("am-entrou0");
                filename = file.getFileName().substring(file.getFileName().lastIndexOf("\\") + 1);
                logger.warning("am-entrou1"+filename);
                 completePathFile = FileSystems.getDefault().getPath("./").normalize().toAbsolutePath().toString();
                //completePathFile = URILookup.getServerDocumentsFolder() + filename;
                logger.warning("am-entrou2"+completePathFile);
                InputStream in = file.getInputstream();
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

                FacesMessage message = new FacesMessage("File: " + file.getFileName() + " uploaded successfully!");
                FacesContext.getCurrentInstance().addMessage(null, message);

            } catch (IOException e) {
                FacesMessage message = new FacesMessage("ERROR :: File: " + file.getFileName() + " not uploaded!");
                FacesContext.getCurrentInstance().addMessage(null, message);
            }
            logger.warning("am-entrou6");
        }
    }

    public UploadedFile getFile() {
        return file;
    }

    public void setFile(UploadedFile file) {
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
    
    
}
