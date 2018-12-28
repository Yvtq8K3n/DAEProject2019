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
    
    @OneToMany(mappedBy = "owner", cascade = CascadeType.REMOVE)
    private List<Configuration> products;
        
    
    public Client() {
       products = new ArrayList<>();
    }

    public Client(String username, String password, String name, String email, String address, String contact) {
        super(username, password, GROUP.Client, name, email);
        this.address = address;
        this.contact = contact;
        products = new ArrayList<>();
        
    }
    
    public void addProduct(Configuration product){
        products.add(product);
    }
    
    public void removeProduct(Configuration product){
        products.remove(product);
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

    public List<Configuration> getProducts() {
        return products;
    }

    public void setProducts(List<Configuration> products) {
        this.products = products;
    }
    
    
}
