package dtos;

import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "Artifact")
@XmlAccessorType(XmlAccessType.FIELD)
public class ArtifactDTO implements Serializable {
    
    public enum UserType {
        USER,PROGRAMMER,SUPPORT
    }
    
    public enum MaterialType {
        VIDEO,TUTORIAL,MANUAL,LINK,DOCUMENT
    }
    
    private Long id;
    private String filepath;
    private String desiredName;
    private String mimeType;
    private MaterialType materialType;
    private UserType userType;
    

    public ArtifactDTO() {
        
    }

    public ArtifactDTO(Long id, String filepath, String desiredName, String mimeType, UserType userType, MaterialType materialType) {
        this.id = id;
        this.filepath = filepath;
        this.desiredName = desiredName;
        this.mimeType = mimeType;
        this.userType = userType;
        this.materialType = materialType;
    }
    
    public ArtifactDTO(String filepath, String desiredName, String mimeType,UserType userType, MaterialType materialType) {
        this(-1L, filepath, desiredName, mimeType, userType, materialType);
    }

    public void reset() {
        setId(null);
        setFilepath(null);
        setDesiredName(null);
        setMimeType(null);
        setUserType(null);
        setMaterialType(null);
    }
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
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

    public MaterialType getMaterialType() {
        return materialType;
    }

    public void setMaterialType(MaterialType materialType) {
        this.materialType = materialType;
    }

    public UserType getUserType() {
        return userType;
    }

    public void setUserType(UserType userType) {
        this.userType = userType;
    }
    
    public List<String> getAllUserType(){
        return Stream.of(UserType.values())
            .map(Enum::name).collect(Collectors.toList());
    }
    
    public List<String> getAllMaterialType(){
        return Stream.of(MaterialType.values())
            .map(Enum::name).collect(Collectors.toList());
    }
}
