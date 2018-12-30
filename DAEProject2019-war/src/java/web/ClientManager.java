package web;

import dtos.ConfigurationDTO;
import ejbs.ClientBean;
import ejbs.ConfigurationBean;
import ejbs.SoftwareBean;
import entities.Client;
import entities.Configuration;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
//import javax.enterprise.context.SessionScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import org.glassfish.jersey.client.authentication.HttpAuthenticationFeature;
import util.URILookup;

//@Named(value = "clientManager")
@ManagedBean(name = "clientManager")
@SessionScoped
public class ClientManager implements Serializable {
 
    @ManagedProperty(value="#{userManager}")
    private UserManager userManager;
    
    @EJB
    private ClientBean cb;
    
    @EJB
    private ConfigurationBean confBean;
    
    private Client clientDTO;
    
    private javax.ws.rs.client.Client client;
    
    private Collection<ConfigurationDTO> configurationDTOs;
    
    private String user;
    
    public ClientManager(){
        configurationDTOs = new ArrayList<>();
        client = ClientBuilder.newClient();
    }
    @PostConstruct
    public void Init(){
        //user = userManager.getUsername();
        //HttpAuthenticationFeature feature = HttpAuthenticationFeature.basic(userManager.getUsername(), userManager.getPassword());
        HttpAuthenticationFeature feature = HttpAuthenticationFeature.basic("administrator1", "secret");
        client.register(feature);
        setClientDTO(cb.getClient(userManager.getUsername()));
    }
    
    public void clientProducts(){
        /*for(ConfigurationDTO confDTO : confBean.getClientConfigurations(userManager.getUsername())){
            configurationDTOs.add(confDTO);
        }*/
        System.out.println("OOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOO: " + URILookup.getBaseAPI());
        configurationDTOs = client.target(URILookup.getBaseAPI())
                    .path("/configurations/user")
                    .path(clientDTO.getUsername())
                    .request(MediaType.APPLICATION_XML)
                    .get(new GenericType<Collection<ConfigurationDTO>>() {
                    });
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

    public Collection<ConfigurationDTO> getConfigurationDTOs() {
        return configurationDTOs;
    }

    public void setConfigurationDTOs(Collection<ConfigurationDTO> configurationDTOs) {
        this.configurationDTOs = configurationDTOs;
    }
    
    
    
}