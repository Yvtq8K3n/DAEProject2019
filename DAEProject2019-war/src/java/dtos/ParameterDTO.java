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
@XmlRootElement(name = "Parameter")
@XmlAccessorType(XmlAccessType.FIELD)
public class ParameterDTO implements Serializable{
    
    public enum MaterialType {
        HARDWARE,CLOUD,REPOSITORY,EXTENSIONS,USER_MANUAL,PROGRAMMER_MANUAL
    }
    private Long id;
    private MaterialType materialType;
    private String name;
    private String description;
    
    public ParameterDTO(){
        
    }

    public ParameterDTO(MaterialType materialType, String name, String DesString) {
        this.materialType = materialType;
        this.name = name;
        this.description = DesString;
    }
    
    public void reset(){
        setId(null);
        setMaterialType(null);
        setDescription(null);
        setName(null);
    }

    public MaterialType getMaterialType() {
        return materialType;
    }

    public void setMaterialType(MaterialType MaterialType) {
        this.materialType = MaterialType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    
    
}
