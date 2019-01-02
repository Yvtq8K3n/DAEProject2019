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

/**
 *
 * @author Olek
 */
@Entity
@Table(name = "Artifacts")
public class Artifact implements Serializable {
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    
    private String filepath;

    private String desiredName;
    
    private String mimeType;
    
    public Artifact(){
        
    }
    
    public Artifact(String filepath, String desiredName, String mimeType) {
        this.filepath = filepath;
        this.desiredName = desiredName;
        this.mimeType = mimeType;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFilepath() {
        return filepath;
    }

    public void setFilepath(String filepath) {
        this.filepath = filepath;
    }

    public String getDesiredName() {
        return desiredName;
    }

    public void setDesiredName(String desiredName) {
        this.desiredName = desiredName;
    }

    public String getMimeType() {
        return mimeType;
    }

    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }        
}
