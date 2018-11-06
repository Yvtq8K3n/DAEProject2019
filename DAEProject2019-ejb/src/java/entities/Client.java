package entities;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.NamedQuery;
import javax.validation.constraints.NotNull;

@Entity
@NamedQuery(
    name = "getAllClients",
    query = "SELECT c FROM Client c ORDER BY c.name"
)
public class Client extends User implements Serializable {

    @NotNull(message = "Address must not be empty")
    private String address;
    
    @NotNull(message = "Contact must not be empty")
    private String contact;

    protected Client() {
       
    }

    public Client(String username, String password, String name, String email, String address, String contact) {
        super(username, password, name, email);
        this.address = address;
        this.contact = contact;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }
}
