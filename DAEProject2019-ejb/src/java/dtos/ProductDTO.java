/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dtos;

import java.io.Serializable;

/**
 *
 * @author Olek
 */
public class ProductDTO extends TemplateDTO implements Serializable{
    
    private String baseVersion;
    private String clientUsername;
    
    public ProductDTO(){
        
    }
    
    public ProductDTO(String name, String description, String baseVersion, String clientUsername){
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
    
    
}
