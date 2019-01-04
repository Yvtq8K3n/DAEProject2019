package web;

import dtos.ArtifactDTO;
import dtos.ConfigurationDTO;
import dtos.ModuleDTO;
import dtos.TemplateDTO;
import ejbs.TemplateBean;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.component.UIComponent;
import javax.inject.Named;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import lombok.Getter;
import lombok.Setter;
import util.MessageHandler;
import util.URILookup;

/**
 *
 * @author Joao Marquez
 */
@Named(value = "guestManager")
@ManagedBean
@SessionScoped
public class GuestManager implements Serializable {
    private static final int HTTP_CREATED = Response.Status.CREATED.getStatusCode();
    private static final int HTTP_OK = Response.Status.OK.getStatusCode();    
    
    private static final Logger logger = Logger.getLogger("web.GuestManager");
    private UIComponent component;
 
    private @Getter @Setter String searchTemplate;
    private @Getter @Setter TemplateDTO templateDTO;
        
    public void GuestManager(){
    
    }
    
    public List<ModuleDTO> getTemplateModules(){
        try {

            Invocation.Builder invocationBuilder = ClientBuilder.newClient()
                    .target(URILookup.getBaseAPI())
                    .path("/templates/")
                    .path(String.valueOf(templateDTO.getId()))
                    .path("/modules")
                    .request(MediaType.APPLICATION_XML);
            
            Response response = invocationBuilder.get(Response.class);
            if (response.getStatus() != HTTP_OK){
                String message = response.readEntity(String.class);
                throw new Exception(message);
            }

            List<ModuleDTO> modulesDTO =
                response.readEntity(new GenericType<List<ModuleDTO>>() {}); 

            return modulesDTO;
        } catch (Exception e) {
            MessageHandler.failMessage("Unexpected error!", e.getMessage());
            return null;
        }
    }
    public List<ArtifactDTO> getTemplateArtifacts(){
        try {
            Invocation.Builder invocationBuilder = ClientBuilder.newClient()
                    .target(URILookup.getBaseAPI())
                    .path("/templates/")
                    .path(String.valueOf(templateDTO.getId()))
                    .path("/artifacts")
                    .request(MediaType.APPLICATION_XML);
            
            Response response = invocationBuilder.get(Response.class);
            if (response.getStatus() != HTTP_OK){
                String message = response.readEntity(String.class);
                throw new Exception(message);
            }

            List<ArtifactDTO> artifactDTO =
                response.readEntity(new GenericType<List<ArtifactDTO>>() {}); 

            return artifactDTO;
        } catch (Exception e) {
            MessageHandler.failMessage("Unexpected error!", e.getMessage());
            return null;
        }
    }
    
    public List<TemplateDTO> getAllTemplates(){
        try {
            Invocation.Builder invocationBuilder = ClientBuilder.newClient()
                    .target(URILookup.getBaseAPI())
                    .path("/templates")
                    .request(MediaType.APPLICATION_XML);
            
            Response response = invocationBuilder.get(Response.class);
            if (response.getStatus() != HTTP_OK){
                String message = response.readEntity(String.class);
                throw new Exception(message);
            }
            
            List<TemplateDTO> templatesDTO =
                response.readEntity(new GenericType<List<TemplateDTO>>() {}); 
            
            return templatesDTO;
        } catch (Exception e) {
            MessageHandler.failMessage("Unexpected error!", e.getMessage());
            return null;
        }
    }
    public List<TemplateDTO> getAllTemplatesCatalog(){
        List<TemplateDTO> allTemplateDTO = getAllTemplates();
        
        //Apply Filter
        if (searchTemplate != null && !searchTemplate.isEmpty()){
            List<TemplateDTO> filterTemplates = allTemplateDTO.stream()
                .filter(p ->
                    p.getName().toUpperCase().contains(searchTemplate.toUpperCase())
                    || p.getDescription().toUpperCase().contains(searchTemplate.toUpperCase())
                ).collect(Collectors.toList());
            return filterTemplates;
        }
        
        return allTemplateDTO;
    }

    
    
    
    void reset() {
        //templates  = getAllProductCatalog();
    }
}