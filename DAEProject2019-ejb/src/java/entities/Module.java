/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entities;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author admin
 */
@Entity
@Table(name ="Modules")
public class Module implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private @Getter @Setter Long id;
    
    @NotNull(message = "Name must not be empty")
    private @Getter @Setter String name;
    
    @NotNull(message = "Version must not be empty")
    private @Getter @Setter String version;
    
    public Module() {
    }

    public Module(String name, String version) {
        this.name = name;
        this.version = version;
    }
}
