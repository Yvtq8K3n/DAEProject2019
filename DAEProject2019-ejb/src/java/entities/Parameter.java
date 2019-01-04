/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entities;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

/**
 *
 * @author Olek
 */
@Entity
@Table(name ="Parameters")
public class Parameter {
    
    
    public enum MaterialType {
        HARDWARE,CLOUD,REPOSITORY,EXTENSIONS,ACTIVE_LICENSES,PARAMETERIZATION
    }
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    
    @Enumerated(EnumType.STRING)
    private MaterialType materialType;
    
    @NotNull(message = "Name must not be empty")
    private String name;
    
    @NotNull(message = "Parameter must have base Valid Data")
    private String description;
    
    
    private String validDate;
    
    public Parameter(){
        
    }

    public Parameter(MaterialType MaterialType, String name, String description, String validDate) {
        this.materialType = MaterialType;
        this.name = name;
        this.description = description;
        this.validDate = validDate;
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

    public void setDescription(String DesString) {
        this.description = DesString;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getValidDate() {
        return validDate;
    }

    public void setValidDate(String validDate) {
        this.validDate = validDate;
    }
    
}
