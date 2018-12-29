package web;

import ejbs.ClientBean;
import ejbs.ProductBean;
import entities.Client;
import entities.Product;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
//import javax.enterprise.context.SessionScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;

//@Named(value = "clientManager")
@ManagedBean(name = "clientManager")
@SessionScoped
public class ClientManager implements Serializable {
 
    @ManagedProperty(value="#{userManager}")
    private UserManager userManager;
    
    @EJB
    private ClientBean cb;
    
    @EJB
    private ProductBean pb;
    
    private Client clientDTO;
    
    private List<Product> productDTOs;
    
    private String user;
    
    public ClientManager(){
        productDTOs = new ArrayList<>();
    }
    @PostConstruct
    public void Init(){
        //user = userManager.getUsername();
        setClientDTO(cb.getClient(userManager.getUsername()));
    }
    
    public void clientProducts(){
        productDTOs = pb.getClientProducts(userManager.getUsername());
    }

    public UserManager getUserManager() {
        return userManager;
    }

    public void setUserManager(UserManager userManager) {
        this.userManager = userManager;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public Client getClientDTO() {
        return clientDTO;
    }

    public void setClientDTO(Client clientDTO) {
        this.clientDTO = clientDTO;
        clientProducts();
    }

    public List<Product> getProductDTOs() {
        return productDTOs;
    }

    public void setProductDTOs(List<Product> productDTOs) {
        this.productDTOs = productDTOs;
    }
    
    
    
}