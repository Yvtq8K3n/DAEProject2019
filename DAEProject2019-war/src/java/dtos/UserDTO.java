package dtos;

import java.io.Serializable;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "User")
@XmlAccessorType(XmlAccessType.FIELD)
public class UserDTO implements Serializable{

    protected String username;
    protected String password;    
    protected String name;
    protected String email;
    protected String group;
    
    public UserDTO() {
    }    
    
    public UserDTO(String username, String password, String name, String email, String group) {
        this.username = username;
        this.password = password;
        this.name = name;
        this.email = email;
        this.group = group;
    }
    
    public void reset() {
        setUsername(null);
        setPassword(null);
        setName(null);
        setEmail(null);
    }        

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }
    
    
}
