/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dtos;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
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
        HARDWARE,CLOUD,REPOSITORY,EXTENSIONS
    }
    private Long id;
    private MaterialType materialType;
    private String name;
    private String description;
    private Date validDate;
    
    public ParameterDTO(){
        
    }

    public ParameterDTO(Long id, MaterialType materialType, String name, String DesString, Date validDate) {
        this.id = id;
        this.materialType = materialType;
        this.name = name;
        this.description = DesString;
        this.validDate = validDate;
    }
    
    public void reset(){
        setId(null);
        setMaterialType(null);
        setDescription(null);
        setName(null);
        setValidDate(null);
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

    public Date getValidDate() {
        return validDate;
    }

    public void setValidDate(Date validDate) {
        this.validDate = validDate;
    }
    
    public List<String> getAllMaterialType(){
        return Stream.of(MaterialType.values())
            .map(Enum::name).collect(Collectors.toList());
    }
}
