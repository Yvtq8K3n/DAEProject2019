package entities;

import entities.UserGroup.GROUP;
import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.NamedQuery;
import javax.validation.constraints.NotNull;

@Entity
@NamedQuery(
    name = "getAllAdministrators",
    query = "SELECT a FROM Administrator a ORDER BY a.name"
)
public class Administrator extends User implements Serializable {

    @NotNull(message = "Occupation must not be empty")
    private String occupation;

    public Administrator() {
       
    }

    public Administrator(String username, String password, String name, String email, String occupation) {
        super(username, password, GROUP.Administrator, name, email);
        this.occupation = occupation;
    }

    public String getOccupation() {
        return occupation;
    }

    public void setOccupation(String occupation) {
        this.occupation = occupation;
    }
    
    
}
