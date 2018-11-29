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
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 *
 * @author Joao Marquez
 */
@Entity
@Table(name="Configurations")
public class Configuration extends Template implements Serializable {
    
    @OneToMany(cascade=CascadeType.REMOVE)
    private List<Module> modules;

    public Configuration(){ 
         modules = new ArrayList<>();
    }

    public Configuration(String name, String description) {
        super(name, description);
        modules = new ArrayList<>();
    }
    
    public void addModule(Module module){
        modules.add(module);
    }
    
    public void rmeoveModule(Module module){
        modules.remove(module);
    }
    
    
}
