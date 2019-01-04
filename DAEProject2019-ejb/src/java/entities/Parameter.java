/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entities;

import java.time.LocalDate;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

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
    private @Getter @Setter Long id;
    
    @Enumerated(EnumType.STRING)
    private @Getter @Setter MaterialType materialType;
    
    @NotNull(message = "Name must not be empty")
    private @Getter @Setter String name;
    
    @NotNull(message = "Parameter must have base Valid Data")
    private @Getter @Setter String description;
    
    private @Getter @Setter LocalDate validDate;
    
    public Parameter(){
        
    }

    public Parameter(MaterialType MaterialType, String name, String description, LocalDate validDate) {
        this.materialType = MaterialType;
        this.name = name;
        this.description = description;
        this.validDate = validDate;
    } 
}
