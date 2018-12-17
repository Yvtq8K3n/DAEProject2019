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
public class ProductDTO extends TemplateDTO implements Serializable{
    
    private Long id;
    private String baseVersion;
    private String clientUsername;
    
    public ProductDTO(){
        
    }
    
    public ProductDTO(Long id, String name, String description, String baseVersion, String clientUsername){
        this.id = id;
        this.name = name;
        this.description = description;
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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
