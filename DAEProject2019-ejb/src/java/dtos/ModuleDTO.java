/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dtos;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Joao Marquez
 */
@XmlRootElement(name = "Module")
@XmlAccessorType(XmlAccessType.FIELD)
public class ModuleDTO implements Serializable{
    
    private Long id;
    private String name;
    private String version;
    
    public ModuleDTO(){
    }

    public ModuleDTO(Long id, String name, String version) {
        this.id = id;
        this.name = name;
        this.version = version;
    }
    
    public void reset() {
       setId(null);
       setName(null);
       setVersion(null);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }
}
