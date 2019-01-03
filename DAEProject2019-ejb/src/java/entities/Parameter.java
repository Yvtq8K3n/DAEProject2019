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
        HARDWARE,CLOUD,REPOSITORY,EXTENSIONS,USER_MANUAL,PROGRAMMER_MANUAL
    }
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    
    @Enumerated(EnumType.STRING)
    private MaterialType materialType;
    
    @NotNull(message = "Name must not be empty")
    private String name;
    
    private String DesString;
    
    public Parameter(){
        
    }

    public Parameter(MaterialType MaterialType, String name, String DesString) {
        this.materialType = MaterialType;
        this.name = name;
        this.DesString = DesString;
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

    public String getDesString() {
        return DesString;
    }

    public void setDesString(String DesString) {
        this.DesString = DesString;
    }
    
}
