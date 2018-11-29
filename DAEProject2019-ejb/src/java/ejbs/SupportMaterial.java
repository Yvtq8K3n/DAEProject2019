/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejbs;

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
 * @author admin
 */
@Entity
@Table(name = "SupportMaterials")
public class SupportMaterial {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    
    @NotNull(message = "SupportMaterial must have a name")
    private String name;
    
    private String url;
    
    @NotNull(message = "SupportMaterial must have a description")
    private String description;
    
    @Enumerated(EnumType.STRING)
    private MaterialType materialType;
    
    private String file;
    
    @Enumerated(EnumType.STRING)
    private UserType userType;
    
    
    
    
}
