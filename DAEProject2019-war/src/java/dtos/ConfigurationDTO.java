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
@XmlRootElement(name = "Configuration")
@XmlAccessorType(XmlAccessType.FIELD)
public class ConfigurationDTO extends TemplateDTO implements Serializable{
    
    private String baseVersion;
    private String clientUsername;
    private String status;
    private String contractDate;

    public ConfigurationDTO(){
    }
  
    public ConfigurationDTO(Long id, String name, String description, String baseVersion, String clientUsername, String status, String contractDate){
        super(id, name, description);
        this.baseVersion = baseVersion;
        this.clientUsername = clientUsername;
        this.status = status;
        this.contractDate = contractDate;
    }

    @Override
    public void reset() {
        super.reset();
        setBaseVersion(null);
        setClientUsername(null);
        setStatus(null);
        setContractDate(null);
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getContractDate() {
        return contractDate;
    }

    public void setContractDate(String contractDate) {
        this.contractDate = contractDate;
    }
    
    
}
