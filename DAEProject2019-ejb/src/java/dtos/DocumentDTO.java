package dtos;

import java.io.Serializable;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "Document")
@XmlAccessorType(XmlAccessType.FIELD)
public class DocumentDTO implements Serializable {
    
    private int id;
    
    private String filepath;

    private String desiredName;
    
    private String mimeType;

    public DocumentDTO() {
        
    }

    public DocumentDTO(int id, String filepath, String desiredName, String mimeType) {
        this.id = id;
        this.filepath = filepath;
        this.desiredName = desiredName;
        this.mimeType = mimeType;
    }
    
    public DocumentDTO(String filepath, String desiredName, String mimeType) {
        this(-1, filepath, desiredName, mimeType);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
    
    public String getFilepath() {
        return filepath;
    }

    public void setFilepath(String filepath) {
        this.filepath = filepath;
    }

    public String getDesiredName() {
        return desiredName;
    }

    public void setDesiredName(String desiredName) {
        this.desiredName = desiredName;
    }

    public String getMimeType() {
        return mimeType;
    }

    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }    
}
