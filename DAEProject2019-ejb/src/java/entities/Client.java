package entities;

import entities.UserGroup.GROUP;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Entity
@NamedQuery(
    name = "getAllClients",
    query = "SELECT c FROM Client c ORDER BY c.name"
)
public class Client extends User implements Serializable {

    @NotNull(message = "Address must not be empty")
    private @Getter @Setter String address;
    
    @NotNull(message = "Contact must not be empty")
    private @Getter @Setter String contact;
    
    @OneToMany(mappedBy = "owner", cascade = CascadeType.ALL, orphanRemoval=true)
    private @Getter @Setter List<Configuration> configurations;

    
    public Client() {
       configurations = new ArrayList<>();
    }

    
    public Client(String username, String password, String name, String email, String address, String contact) {
        super(username, password, GROUP.Client, name, email);
        this.address = address;
        this.contact = contact;
        configurations = new ArrayList<>();  
    }
    
    
    public void addConfiguration(Configuration product){
        configurations.add(product);
    }
    public void removeConfiguration(Configuration product){
        configurations.remove(product);
    }
}
