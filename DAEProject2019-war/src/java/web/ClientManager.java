package web;

import dtos.ArtifactDTO;
import dtos.ClientDTO;
import dtos.CommentDTO;
import dtos.ConfigurationDTO;
import dtos.ModuleDTO;
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
import javax.faces.application.FacesMessage;
import javax.inject.Named;
//import javax.enterprise.context.SessionScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import lombok.Getter;
import lombok.Setter;
import org.glassfish.jersey.client.authentication.HttpAuthenticationFeature;
import util.MessageHandler;
import util.URILookup;

@Named(value = "clientManager")
@ManagedBean
@SessionScoped
public class ClientManager implements Serializable {
    private static final int HTTP_CREATED = Response.Status.CREATED.getStatusCode();
    private static final int HTTP_OK = Response.Status.OK.getStatusCode();
    
    private static final Logger logger = Logger.getLogger("web.AdministratorManager");
    
    private @Getter @Setter UIComponent component;
    
    private ClientDTO clientDTO;
    private @Getter @Setter CommentDTO newCommentDTO;
    private @Getter @Setter ConfigurationDTO currentConfiguration;
    
    public ClientManager(){
        clientDTO = new ClientDTO();
        newCommentDTO = new CommentDTO();
        currentConfiguration = new ConfigurationDTO();
    }

    @PostConstruct
    public void Init(){
        FacesContext context = FacesContext.getCurrentInstance();
        javax.faces.application.Application app = context.getApplication();
        UserManager userManager = app.evaluateExpressionGet(context, "#{userManager}", UserManager.class);
        setClientDTO(getClient(userManager.getUsername()));
    }

    
    public ClientDTO getClient(String username){
        try {
            Invocation.Builder invocationBuilder = addHeaderBASIC().target(URILookup.getBaseAPI())
                    .path("/clients/cliento")
                    .path(username)
                    .request(MediaType.APPLICATION_XML);
        
            Response response = invocationBuilder.get(Response.class);
         
            return response.readEntity(new GenericType<ClientDTO>(){}); 
        } catch (Exception e) {
            FacesExceptionHandler.handleException(e, "Unexpected error! Try again latter!", logger);
            return null;
        }
    }
    
    private javax.ws.rs.client.Client addHeaderBASIC() {
        javax.ws.rs.client.Client client = ClientBuilder.newClient();
        FacesContext context = FacesContext.getCurrentInstance();
        javax.faces.application.Application app = context.getApplication();
        UserManager userManager = app.evaluateExpressionGet(context, "#{userManager}", UserManager.class);
        
        String username = userManager.getUsername();
        String password = userManager.getPassword();
        
        HttpAuthenticationFeature feature = HttpAuthenticationFeature.basic(username, password);
        client.register(feature);
        return client;
    }
    
    public List<ConfigurationDTO> getClientConfigurations(){
        try {
            Invocation.Builder invocationBuilder = addHeaderBASIC()
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
    
     public List<ModuleDTO> getConfigurationModules(){
        try {
            Invocation.Builder invocationBuilder = addHeaderBASIC()
                    .target(URILookup.getBaseAPI())
                    .path("/configurations/")
                    .path(String.valueOf(currentConfiguration.getId()))
                    .path("/modules")
                    .request(MediaType.APPLICATION_XML);
            
            Response response = invocationBuilder.get(Response.class);

            List<ModuleDTO> modulesDTO =
                response.readEntity(new GenericType<List<ModuleDTO>>() {}); 
          
            logger.warning("modules:"+modulesDTO.size());
            return modulesDTO;
        } catch (Exception e) {
            FacesExceptionHandler.handleException(e, "Unexpected error! Try again latter!", logger);
            return null;
        }
    }
     
     public List<ArtifactDTO> getConfigurationArtifacts(){
        try {
            Invocation.Builder invocationBuilder = addHeaderBASIC()
                    .target(URILookup.getBaseAPI())
                    .path("/configurations/")
                    .path(String.valueOf(currentConfiguration.getId()))
                    .path("/artifacts")
                    .request(MediaType.APPLICATION_XML);
            
            Response response = invocationBuilder.get(Response.class);

            List<ArtifactDTO> artifactDTO =
                response.readEntity(new GenericType<List<ArtifactDTO>>() {}); 
          
            logger.warning("artifact:"+artifactDTO.size());
            return artifactDTO;
        } catch (Exception e) {
            FacesExceptionHandler.handleException(e, "Unexpected error! Try again latter!", logger);
            return null;
        }
    }
     
    public List<CommentDTO> getConfigurationComments(){
        try {
            Invocation.Builder invocationBuilder = addHeaderBASIC()
                    .target(URILookup.getBaseAPI())
                    .path("/configurations/")
                    .path(String.valueOf(currentConfiguration.getId()))
                    .path("/comments")
                    .request(MediaType.APPLICATION_XML);
            
            Response response = invocationBuilder.get(Response.class);

            List<CommentDTO> commentDTO =
                response.readEntity(new GenericType<List<CommentDTO>>() {}); 

            return commentDTO;
        } catch (Exception e) {
            FacesExceptionHandler.handleException(e, "Unexpected error! Try again latter!", logger);
            return null;
        }
    }
    
    public void createConfigurationComment(Long configurationId, Long parentId){
        //Retrieves userManager
        FacesContext context = FacesContext.getCurrentInstance();
        javax.faces.application.Application app = context.getApplication();
        UserManager userManager = app.evaluateExpressionGet(context, "#{userManager}", UserManager.class);
        
        //Build Comment
        if (parentId != null) newCommentDTO.setParent(parentId);
        newCommentDTO.setConfiguration(configurationId);
        newCommentDTO.setAuthor(userManager.getUsername());
        
        try {
            Invocation.Builder invocationBuilder = 
                addHeaderBASIC().target(URILookup.getBaseAPI())
                    .path("/comments")
                    .request(MediaType.APPLICATION_XML);
            
            Response response = invocationBuilder.post(Entity.xml(newCommentDTO));
            newCommentDTO.reset();
            
            String message = response.readEntity(String.class);
            if (response.getStatus() != HTTP_CREATED){
                throw new Exception(message);
            }
            
            MessageHandler.successMessage("Comment Created:",message);
        } catch (Exception e) {
            FacesExceptionHandler.handleException(e, e.getMessage(), component, logger);
        }
    }         

    public ClientDTO getClientDTO() {
        return clientDTO;
    }

    public void setClientDTO(ClientDTO clientDTO) {
        this.clientDTO = clientDTO;
    }
}