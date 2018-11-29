/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

/**
 *
 * @author Joao Marquez
 */
@Entity
@Table(name="Configurations")
public class Configuration implements Serializable {

    @Id
    @Column(name="ID")
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;
    
    @NotNull(message = "Template must have a description")
    private String description;
    
    @ManyToMany(mappedBy="configurations")
    private List<Product> products;
    
    @ManyToMany(mappedBy="configurations")
    private List<ProductCatalog> productsCatalog;
    
    @OneToMany(cascade=CascadeType.REMOVE)
    private List<Module> modules;
    

    public Configuration(){ 
         modules = new ArrayList<>();
         products= new ArrayList<>();
         productsCatalog= new ArrayList<>();
    }

    public Configuration(String description) {
        this();
        this.description = description;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
    
     public void addModule(Module module){
        modules.add(module);
    }
    
    public void rmeoveModule(Module module){
        modules.remove(module);
    }
}
