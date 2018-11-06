package entities;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.NamedQuery;
import javax.validation.constraints.NotNull;

@Entity
@NamedQuery(
    name = "getAllAdminstrators",
    query = "SELECT a FROM Adminstrator a ORDER BY a.name"
)
public class Adminstrator extends User implements Serializable {

    @NotNull(message = "Occupation must not be empty")
    private String occupation;

    protected Adminstrator() {
       
    }

    public Adminstrator(String username, String password, String name, String email, String occupation) {
        super(username, password, name, email);
        this.occupation = occupation;
       
    }
}
