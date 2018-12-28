/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dtos;

import java.io.Serializable;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Olek
 */
@XmlRootElement(name = "Product")
@XmlAccessorType(XmlAccessType.FIELD)
public class ConfigurationDTO extends TemplateDTO implements Serializable{
    
    private String baseVersion;
    private String clientUsername;

    public ConfigurationDTO(){
    }
  
    public ConfigurationDTO(Long id, String name, String description, String baseVersion, String clientUsername){
        super(id, name, description);
        this.baseVersion = baseVersion;
        this.clientUsername = clientUsername;
    }

    @Override
    public void reset() {
        super.reset();
        setBaseVersion(null);
        setClientUsername(null);
    }

    public String getBaseVersion() {
        return baseVersion;
    }

    public void setBaseVersion(String baseVersion) {
        this.baseVersion = baseVersion;
    }

    public String getClientUsername() {
        return clientUsername;
    }

    public void setClientUsername(String clientUsername) {
        this.clientUsername = clientUsername;
    }
}
