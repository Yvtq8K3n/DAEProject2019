/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dtos;

import java.io.Serializable;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Olek
 */
@XmlRootElement(name = "Administrator")
@XmlAccessorType(XmlAccessType.FIELD)
public class AdministratorDTO extends UserDTO implements Serializable{
    
    private String occupation;
    
    public AdministratorDTO(){
    }

    public AdministratorDTO(String username, String password, String name, String email, String occupation) {
        super(username, password, name, email, "Administrator");
        this.occupation = occupation;
    }
    
    @Override
    public void reset() {
        super.reset();
        setOccupation(null);
    }

    public String getOccupation() {
        return occupation;
    }

    public void setOccupation(String occupation) {
        this.occupation = occupation;
    }  
    
}
