package web;

import dtos.ClientDTO;
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
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
//import javax.enterprise.context.SessionScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.glassfish.jersey.client.authentication.HttpAuthenticationFeature;
import util.URILookup;

//@Named(value = "clientManager")
@ManagedBean(name = "clientManager")
@SessionScoped
public class ClientManager implements Serializable {
 
    @ManagedProperty(value="#{userManager}")
    private UserManager userManager;
    
    private static final Logger logger = Logger.getLogger("web.AdministratorManager");
    
    private ClientDTO clientDTO;
    
    private javax.ws.rs.client.Client client;
    
    private Collection<ConfigurationDTO> configurationDTOs;
    
    private ConfigurationDTO currentConfiguration;
    
    private String user;
    
    public ClientManager(){
        configurationDTOs = new ArrayList<>();
        client = ClientBuilder.newClient();
    }
    @PostConstruct
    public void Init(){
        HttpAuthenticationFeature feature = HttpAuthenticationFeature.basic(userManager.getUsername(), userManager.getPassword());
        client.register(feature);
        setClientDTO(getClient(userManager.getUsername()));
    }
    
    public ClientDTO getClient(String username){
        return client.target(URILookup.getBaseAPI())
                    .path("/clients/cliento")
                    .path(username)
                    .request(MediaType.APPLICATION_XML)
                    .get(ClientDTO.class); 
    }
    
    public List<ConfigurationDTO> getClientProducts(){
        try {
            Invocation.Builder invocationBuilder = client
                    .target(URILookup.getBaseAPI())
                    .path("/clients/")
                    .path(clientDTO.getUsername())
                    .path("/configurations")
                    .request(MediaType.APPLICATION_XML);
            
            Response response = invocationBuilder.get(Response.class);

            List<ConfigurationDTO> configurationsDTO =
                response.readEntity(new GenericType<List<ConfigurationDTO>>() {}); 
          
            return configurationsDTO;
        } catch (Exception e) {
            FacesExceptionHandler.handleException(e, "Unexpected error! Try again latter!", logger);
            return null;
        }
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

    public ClientDTO getClientDTO() {
        return clientDTO;
    }

    public void setClientDTO(ClientDTO clientDTO) {
        this.clientDTO = clientDTO;
        this.configurationDTOs = getClientProducts();
    }

    public Collection<ConfigurationDTO> getConfigurationDTOs() {
        return configurationDTOs;
    }

    public void setConfigurationDTOs(Collection<ConfigurationDTO> configurationDTOs) {
        this.configurationDTOs = configurationDTOs;
    }

    public ConfigurationDTO getCurrentConfiguration() {
        return currentConfiguration;
    }

    public void setCurrentConfiguration(ConfigurationDTO currentConfiguration) {
        this.currentConfiguration = currentConfiguration;
    }
    
}