package web;

import ejbs.ClientBean;
import entities.Client;
import java.io.Serializable;
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
    
    Client clientDTO;
    
    private String user;
    
    public ClientManager(){
        
    }
    @PostConstruct
    public void Init(){
        //user = userManager.getUsername();
        clientDTO = cb.getClient(userManager.getUsername());
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
    }
    
    
    
}