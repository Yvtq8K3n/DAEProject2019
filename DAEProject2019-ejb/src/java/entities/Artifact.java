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
import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author Olek
 */
@Entity
@Table(name = "Artifacts")
public class Artifact implements Serializable {
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private @Getter @Setter Long id;
    private @Getter @Setter String filepath;
    private @Getter @Setter String desiredName;
    private @Getter @Setter String mimeType;
    
    public Artifact(){
        
    }
    
    public Artifact(String filepath, String desiredName, String mimeType) {
        this.filepath = filepath;
        this.desiredName = desiredName;
        this.mimeType = mimeType;
    }
}
