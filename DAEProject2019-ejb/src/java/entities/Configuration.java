/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entities;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 *
 * @author Joao Marquez
 */
@Entity
@Table(name="Configurations")
public class Configuration extends Template implements Serializable {

    public Configuration() {
    }

    public Configuration(String name, String description) {
        super(name, description);
    }
    
}
