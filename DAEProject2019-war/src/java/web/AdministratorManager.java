
package web;

import dtos.AdministratorDTO;
import dtos.ClientDTO;
import dtos.ArtifactDTO;
import dtos.CommentDTO;
import dtos.ConfigurationDTO;
import dtos.EmailDTO;
import dtos.ModuleDTO;
import dtos.ParameterDTO;
import dtos.TemplateDTO;
import dtos.UserDTO;
import java.io.Serializable;
import java.util.List;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.Asynchronous;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.component.UIComponent;
import javax.faces.component.UIParameter;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation;
import javax.inject.Named;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import util.URILookup;
import lombok.Getter;
import lombok.Setter;
import org.glassfish.jersey.client.authentication.HttpAuthenticationFeature;
import util.MessageHandler;


@Named(value = "administratorManager")
@ManagedBean
@SessionScoped
public class AdministratorManager implements Serializable {
    private static final int HTTP_CREATED = Response.Status.CREATED.getStatusCode();
    private static final int HTTP_OK = Response.Status.OK.getStatusCode();

    private static final Logger logger = Logger.getLogger("web.AdministratorManager");
    
    private @Getter @Setter UIComponent component;

    private @Getter @Setter ClientDTO clientDTO;
    private @Getter @Setter AdministratorDTO administratorDTO;
    private @Getter @Setter ModuleDTO moduleDTO;
    private @Getter @Setter ParameterDTO parameterDTO;
    private @Getter @Setter TemplateDTO templateDTO;
    private @Getter @Setter ConfigurationDTO configurationDTO;
    private @Getter @Setter CommentDTO commentDTO;
    private @Getter @Setter ArtifactDTO artifactDTO;

    public AdministratorManager() {
        clientDTO = new ClientDTO();
        templateDTO = new TemplateDTO();
        commentDTO = new CommentDTO();
        artifactDTO = new ArtifactDTO();
        configurationDTO = new ConfigurationDTO();
        administratorDTO = new AdministratorDTO();
        moduleDTO = new ModuleDTO();
        parameterDTO = new ParameterDTO();
    }
        
    @PostConstruct
    public void Init(){
        
    };
    
    public AdministratorDTO getLoggedUser(String username){
        try {
            Invocation.Builder invocationBuilder = addHeaderBASIC().target(URILookup.getBaseAPI())
                    .path("/administrators/administrator")
                    .path(username)
                    .request(MediaType.APPLICATION_XML);
        
            Response response = invocationBuilder.get(Response.class);
         
            return response.readEntity(new GenericType<AdministratorDTO>(){});
        } catch (Exception e) {
            FacesExceptionHandler.handleException(e, "Unexpected error! Try again latter!", logger);
            return null;
        }
    }
    
    private Client addHeaderBASIC() {
        Client client = ClientBuilder.newClient();
        FacesContext context = FacesContext.getCurrentInstance();
        javax.faces.application.Application app = context.getApplication();
        UserManager userManager = app.evaluateExpressionGet(context, "#{userManager}", UserManager.class);
        
        String username = userManager.getUsername();
        String password = userManager.getPassword();
        
        HttpAuthenticationFeature feature = HttpAuthenticationFeature.basic(username, password);
        client.register(feature);
        return client;
    }
    
    public String createClient(){
        FacesMessage facesMsg;
        try {
            Invocation.Builder invocationBuilder = addHeaderBASIC()
                    .target(URILookup.getBaseAPI())
                    .path("/clients")
                    .request(MediaType.APPLICATION_XML);
            
            Response response = invocationBuilder.post(Entity.xml(clientDTO));

            if (response.getStatus() != HTTP_CREATED){
                String message = response.readEntity(String.class);
                throw new Exception(message);
            }
            
            clientDTO.reset();
            String message = response.readEntity(String.class);           
            MessageHandler.successMessage("Client Created:", message);
        } catch (Exception e) {
            MessageHandler.failMessage("Created Failed:", e.getMessage());
            return null;
        }
        return "/admin/users/clients/view.xhtml?faces-redirect=true";
    }
    public String createAdmin(){
        try {
            Invocation.Builder invocationBuilder = 
                addHeaderBASIC().target(URILookup.getBaseAPI())
                    .path("/administrators")
                    .request(MediaType.APPLICATION_XML);
            
            Response response = invocationBuilder.post(Entity.xml(administratorDTO));

            if (response.getStatus() != HTTP_CREATED){
                String message = response.readEntity(String.class);
                throw new Exception(message);
            }
            
            administratorDTO.reset();
            String message = response.readEntity(String.class);           
            MessageHandler.successMessage("Client Created:", message);
        } catch (Exception e) {
            MessageHandler.failMessage("Created Failed:", e.getMessage());
            return null;
        }
        return "/admin/users/administrators/view.xhtml?faces-redirect=true";
    }
   
    public String createConfiguration(){
        configurationDTO.setOwner(clientDTO.getUsername());
        try {
            Invocation.Builder invocationBuilder = 
                addHeaderBASIC().target(URILookup.getBaseAPI())
                    .path("/configurations")
                    .request(MediaType.APPLICATION_XML);
            
            Response response = invocationBuilder.post(Entity.xml(configurationDTO));
            
            if (response.getStatus() != HTTP_CREATED){
                String message = response.readEntity(String.class);
                throw new Exception(message);
            }
            String confId = response.readEntity(String.class);
            
            configurationDTO.setId(Long.valueOf(confId));
            MessageHandler.successMessage("Configuration Created:","Configuration was successfully created");
        } catch (Exception e) {
            FacesExceptionHandler.handleException(e, e.getMessage(), component, logger);
            return null;
        }
        return "/admin/users/clients/configurations/details.xhtml?faces-redirect=true";
    }
    public String createConfigurationFromTemplate(TemplateDTO templateDTO){
        configurationDTO.setOwner(clientDTO.getUsername());
        logger.info(String.valueOf(templateDTO.getId()));
        try {
            Invocation.Builder invocationBuilder = 
                addHeaderBASIC().target(URILookup.getBaseAPI())
                    .path("/templates")
                    .path(String.valueOf(templateDTO.getId()))
                    .path("/baseOn/Configuration")                    
                    .request(MediaType.APPLICATION_XML);
            
            Response response = invocationBuilder.post(Entity.xml(configurationDTO));
            if (response.getStatus() != HTTP_CREATED){
                String message = response.readEntity(String.class);
                throw new Exception(message);
            }
            this.configurationDTO = response.readEntity(ConfigurationDTO.class);
           
            MessageHandler.successMessage("Configuration Created:","Configuration was successfully created");
        } catch (Exception e) {
            MessageHandler.successMessage("Create Failed:", e.getMessage());
            return null;
        }
        return "/admin/users/clients/configurations/details.xhtml?faces-redirect=true";
    }
    public String createConfigurationFromConfiguration(ConfigurationDTO configurationDTO){
        configurationDTO.setOwner(clientDTO.getUsername());
        
        try {
            Invocation.Builder invocationBuilder = 
                addHeaderBASIC().target(URILookup.getBaseAPI())
                    .path("/configurations")
                    .path(String.valueOf(configurationDTO.getId()))
                    .path("/baseOn/Configuration")                    
                    .request(MediaType.APPLICATION_XML);
            
            Response response = invocationBuilder.post(Entity.xml(configurationDTO));
            if (response.getStatus() != HTTP_CREATED){
                String message = response.readEntity(String.class);
                throw new Exception(message);
            }
            
            this.configurationDTO = response.readEntity(ConfigurationDTO.class);      
            MessageHandler.successMessage("Configuration Created:","Configuration was successfully created");
        } catch (Exception e) {
            MessageHandler.successMessage("Create Failed:", e.getMessage());
            return null;
        }
        return "/admin/users/clients/configurations/details.xhtml?faces-redirect=true";
    }
    
    public String createTemplate(){
        try {
            Invocation.Builder invocationBuilder = 
                addHeaderBASIC().target(URILookup.getBaseAPI())
                    .path("/templates")
                    .request(MediaType.APPLICATION_XML);
            
            Response response = invocationBuilder.post(Entity.xml(templateDTO));
            
            String message = response.readEntity(String.class);
            if (response.getStatus() != HTTP_CREATED){
                throw new Exception(message);
            }
            
            templateDTO.setId(Long.valueOf(message));
            MessageHandler.successMessage("Template Created:","Template was successfully created");
        } catch (Exception e) {
            FacesExceptionHandler.handleException(e, e.getMessage(), component, logger);
            return null;
        }
        return "/admin/catalog/details.xhtml?faces-redirect=true";
    }
    
    public void createConfigurationComment(Long configurationId, Long parentId){
        //Retrieves userManager
        FacesContext context = FacesContext.getCurrentInstance();
        javax.faces.application.Application app = context.getApplication();
        UserManager userManager = app.evaluateExpressionGet(context, "#{userManager}", UserManager.class);
        
        //Build Comment
        if (parentId != null) commentDTO.setParent(parentId);
        commentDTO.setConfiguration(configurationId);
        commentDTO.setAuthor(userManager.getUsername());
        
        try {
            Invocation.Builder invocationBuilder = 
                addHeaderBASIC().target(URILookup.getBaseAPI())
                    .path("/comments")
                    .request(MediaType.APPLICATION_XML);
            
            Response response = invocationBuilder.post(Entity.xml(commentDTO));
            commentDTO.reset();
            
           
            if (response.getStatus() != HTTP_CREATED){
                String message = response.readEntity(String.class);
                throw new Exception(message);
            }
            
            MessageHandler.successMessage("Comment Created:","Comment was successfully created.");
        } catch (Exception e) {
            FacesExceptionHandler.handleException(e, e.getMessage(), component, logger);
        }
    }
    public void createConfigurationModule(){
        try {
            Invocation.Builder invocationBuilder = 
                addHeaderBASIC().target(URILookup.getBaseAPI())
                    .path("/configurations/")
                    .path(String.valueOf(configurationDTO.getId()))
                    .path("/modules")
                    .request(MediaType.APPLICATION_XML);
            
            Response response = invocationBuilder.post(Entity.xml(moduleDTO));
            
            
            String message = response.readEntity(String.class);
            if (response.getStatus() != HTTP_CREATED){
                throw new Exception(message);
            }
            
            MessageHandler.successMessage("Module Created:",message);
            sendMailCreateModule(moduleDTO.getName());
            moduleDTO.reset();
        } catch (Exception e) {
            FacesExceptionHandler.handleException(e, e.getMessage(), component, logger);
        }
    }
    
    
    public void createConfigurationParameter(){
        try {
            System.out.println("ENTREI 1");
            Invocation.Builder invocationBuilder = 
                addHeaderBASIC().target(URILookup.getBaseAPI())
                    .path("/configurations/")
                    .path(String.valueOf(configurationDTO.getId()))
                    .path("/parameters")
                    .request(MediaType.APPLICATION_XML);
            System.out.println("ENTREI 2");
            Response response = invocationBuilder.post(Entity.xml(parameterDTO));
            System.out.println("ENTREI 3");
            
            System.out.println("ENTREI 4");
            if (response.getStatus() != HTTP_CREATED){
                String message = response.readEntity(String.class);
                throw new Exception(message);
            }
            String message = response.readEntity(String.class);
            System.out.println("ENTREI 5");
            
            MessageHandler.successMessage("Parameter Created:",message);
            sendMailCreateParameter(parameterDTO.getName());
            moduleDTO.reset();
        } catch (Exception e) {
            FacesExceptionHandler.handleException(e, e.getMessage(), component, logger);
        }
    }
    
    public void createConfigurationArtifact() {
        try {
            FacesContext context = FacesContext.getCurrentInstance();
            javax.faces.application.Application app = context.getApplication();
            UploadManager uploadManager = app.evaluateExpressionGet(context, "#{uploadManager}", UploadManager.class);
        
            //Add uploadManager File properties to ArtifactDTO
            artifactDTO.setFilepath(uploadManager.getCompletePathFile());
            artifactDTO.setDesiredName(uploadManager.getFilename());
            artifactDTO.setMimeType(uploadManager.getFile().getContentType());

            Invocation.Builder invocationBuilder = addHeaderBASIC()
                .target(URILookup.getBaseAPI())
                .path("/configurations/")
                .path(String.valueOf(configurationDTO.getId()))
                .path("/artifacts")
                .request(MediaType.APPLICATION_XML);

            Response response = invocationBuilder.post(Entity.xml(artifactDTO));     
            
            String message = response.readEntity(String.class);
            if (response.getStatus() != HTTP_CREATED){
                throw new Exception(message);
            }
            
            sendMailCreateArtifact(artifactDTO.getDesiredName());
            artifactDTO.reset();
            MessageHandler.successMessage("Artifact Created:", message);
        } catch (Exception e) {
            FacesExceptionHandler.handleException(e, "Unexpected error! Try again latter!", logger);
        }
    }
   
    public void createTemplateModule(){
        try {         
            Invocation.Builder invocationBuilder = 
                addHeaderBASIC().target(URILookup.getBaseAPI())
                    .path("/templates/")
                    .path(String.valueOf(templateDTO.getId()))
                    .path("/modules")
                    .request(MediaType.APPLICATION_XML);

            Response response = invocationBuilder.post(Entity.xml(moduleDTO));
            moduleDTO.reset();
            
            String message = response.readEntity(String.class);
            if (response.getStatus() != HTTP_CREATED){
                throw new Exception(message);
            }
            
            MessageHandler.successMessage("Module Created:",message);
        } catch (Exception e) {
            FacesExceptionHandler.handleException(e, e.getMessage(), component, logger);
        }
    }
    public void createTemplateArtifact() {
        try {
            FacesContext context = FacesContext.getCurrentInstance();
            javax.faces.application.Application app = context.getApplication();
            UploadManager uploadManager = app.evaluateExpressionGet(context, "#{uploadManager}", UploadManager.class);
        
            //Add uploadManager File properties to ArtifactDTO
            artifactDTO.setFilepath(uploadManager.getCompletePathFile());
            artifactDTO.setDesiredName(uploadManager.getFilename());
            artifactDTO.setMimeType(uploadManager.getFile().getContentType());

            Invocation.Builder invocationBuilder = addHeaderBASIC()
                .target(URILookup.getBaseAPI())
                .path("/templates/")
                .path(String.valueOf(templateDTO.getId()))
                .path("/artifacts")
                .request(MediaType.APPLICATION_XML);

            Response response = invocationBuilder.post(Entity.xml(artifactDTO));     
            
            String message = response.readEntity(String.class);
            if (response.getStatus() != HTTP_CREATED){
                throw new Exception(message);
            }
            artifactDTO.reset();
            MessageHandler.successMessage("Artifact Created:", message);
        } catch (Exception e) {
            FacesExceptionHandler.handleException(e, "Unexpected error! Try again latter!", logger);
        }
    }   
    
    
    public void updateAdministrator(){
        try {
            Invocation.Builder invocationBuilder = 
                addHeaderBASIC().target(URILookup.getBaseAPI())
                    .path("/administrators")
                    .request(MediaType.APPLICATION_XML);
            
            Response response = invocationBuilder.put(Entity.xml(administratorDTO));
  
            if (response.getStatus() != HTTP_OK){
                String message = response.readEntity(String.class);
                throw new Exception(message);
            }
            
            String message = response.readEntity(String.class);
            MessageHandler.successMessage("Administrator Updated:",message);
        } catch (Exception e) {
            MessageHandler.failMessage("Update Failed:",e.getMessage());
        } 
    }
    public void updateClient(){
        try {
            Invocation.Builder invocationBuilder = 
                addHeaderBASIC().target(URILookup.getBaseAPI())
                    .path("/clients")
                    .request(MediaType.APPLICATION_XML);
            
            Response response = invocationBuilder.put(Entity.xml(clientDTO));
  
            if (response.getStatus() != HTTP_OK){
                String message = response.readEntity(String.class);
                throw new Exception(message);
            }
            
            String message = response.readEntity(String.class);
            MessageHandler.successMessage("Client Updated:",message);
        } catch (Exception e) {
            MessageHandler.failMessage("Update Failed:",e.getMessage());
        } 
    }
    public void updateTemplate(){
        templateDTO.getDescription();
        try {
            Invocation.Builder invocationBuilder = 
                addHeaderBASIC().target(URILookup.getBaseAPI())
                    .path("/templates")
                    .request(MediaType.APPLICATION_XML);
            
            Response response = invocationBuilder.put(Entity.xml(templateDTO));
            
            if (response.getStatus() != HTTP_OK){
                String message = response.readEntity(String.class);
                throw new Exception(message);
            }
            
            String message = response.readEntity(String.class);
            MessageHandler.successMessage("Template Updated:",message);
        } catch (Exception e) {
            MessageHandler.failMessage("Updated Failed:",e.getMessage());
            
        }
       
    }
    public void updateConfiguration(){
        try {
            Invocation.Builder invocationBuilder = 
                addHeaderBASIC().target(URILookup.getBaseAPI())
                    .path("/configurations")
                    .request(MediaType.APPLICATION_XML);
            
            Response response = invocationBuilder.put(Entity.xml(configurationDTO));
            
            if (response.getStatus() != HTTP_OK){
                String message = response.readEntity(String.class);
                throw new Exception(message);
            }
            
            String message = response.readEntity(String.class);
            MessageHandler.successMessage("Configuration Updated:",message);
            sendMailUpdate();
        } catch (Exception e) {
            FacesExceptionHandler.handleException(e, e.getMessage(), component, logger);
            
        }
       
    }
    
    public void removeClient(ActionEvent event){
        try {
            UIParameter param = (UIParameter) event.getComponent().findComponent("deleteClientId");
            String clientId = param.getValue().toString();
            
            Invocation.Builder invocationBuilder = addHeaderBASIC()
                    .target(URILookup.getBaseAPI())
                    .path("/clients/"+clientId)
                    .request(MediaType.APPLICATION_XML);
            Response response = invocationBuilder.delete();
            
            String message = response.readEntity(String.class);
            if (response.getStatus() != HTTP_OK){
                throw new Exception(message);
            }
            
            MessageHandler.successMessage("Client Deleted:", message);
        } catch (Exception e) {
            MessageHandler.failMessage("Delete Failed:", e.getMessage());
        }
    }
    public void removeAdministrator(ActionEvent event){
        Client client = ClientBuilder.newClient();
        FacesContext context = FacesContext.getCurrentInstance();
        javax.faces.application.Application app = context.getApplication();
        UserManager userManager = app.evaluateExpressionGet(context, "#{userManager}", UserManager.class);
        
        try {
            UIParameter param = (UIParameter) event.getComponent().findComponent("deleteAdministratorId");
            String administratorId = param.getValue().toString();

            if (administratorId.compareTo(userManager.getUsername())==0)
                throw new Exception("Unable to delete account.");
            
            Invocation.Builder invocationBuilder = addHeaderBASIC()
                    .target(URILookup.getBaseAPI())
                    .path("/administrators/"+administratorId)
                    .request(MediaType.APPLICATION_XML);
            Response response = invocationBuilder.delete();
            
            String message = response.readEntity(String.class);
            if (response.getStatus() != HTTP_OK){
                throw new Exception(message);
            }
            
            MessageHandler.successMessage("Administrator Deleted:", message);
        } catch (Exception e) {
            MessageHandler.failMessage("Delete Failed:", e.getMessage());
        }
    }
    
    //STILL NOT DONE
    public void removeTemplate(ActionEvent event){
        FacesMessage facesMsg;
        try {
            UIParameter param = (UIParameter) event.getComponent().findComponent("deleteTemplateId");
            Long id = (Long)param.getValue();
                        
            Invocation.Builder invocationBuilder = addHeaderBASIC()
                    .target(URILookup.getBaseAPI())
                    .path("/templates")
                    .path(String.valueOf(templateDTO.getId()))
                    .request(MediaType.APPLICATION_XML);
            Response response = invocationBuilder.delete();
            
            String message = response.readEntity(String.class);
            if (response.getStatus() != HTTP_OK){
                throw new Exception(message);
            }
            
            MessageHandler.successMessage("Template Deleted:", message);
        } catch (Exception e) {
            MessageHandler.failMessage("Delete Failed:", e.getMessage());
        }
    }
    public void removeTemplateModule(ActionEvent event){
        
        try {
            UIParameter param = (UIParameter) event.getComponent().findComponent("deleteTemplateModuleId");
            Long id = (Long)param.getValue();
            
            Invocation.Builder invocationBuilder = addHeaderBASIC()
                    .target(URILookup.getBaseAPI())
                    .path("/templates/")
                    .path(String.valueOf(templateDTO.getId()))
                    .path("/modules/")
                    .path(String.valueOf(id))
                    .request(MediaType.APPLICATION_XML);
            Response response = invocationBuilder.delete();
            
            String message = response.readEntity(String.class);
            if (response.getStatus() != HTTP_OK){
                throw new Exception(message);
            }
            
            MessageHandler.successMessage("Module Deleted:", message);
        } catch (Exception e) {
            MessageHandler.failMessage("Delete Failed:", e.getMessage());
        }
    }
    public void removeTemplateArtifact(ActionEvent event){
        try {
            UIParameter param = (UIParameter) event.getComponent().findComponent("deleteTemplateArtifactId");
            Long id = (Long)param.getValue();
            
            Invocation.Builder invocationBuilder = addHeaderBASIC()
                    .target(URILookup.getBaseAPI())
                    .path("/templates/")
                    .path(String.valueOf(templateDTO.getId()))
                    .path("/artifacts")
                    .path(String.valueOf(id))
                    .request(MediaType.APPLICATION_XML);
            Response response = invocationBuilder.delete();
            
            String message = response.readEntity(String.class);
            if (response.getStatus() != HTTP_OK){
                throw new Exception(message);
            }
            
            MessageHandler.successMessage("Artifact Deleted:", message);
        } catch (Exception e) {
            MessageHandler.failMessage("Delete Failed:", e.getMessage());
        }
    }
    
    public void removeConfiguration(ActionEvent event){
        FacesMessage facesMsg;
        try {
            UIParameter param = (UIParameter) event.getComponent().findComponent("deleteConfigurationId");
            Long id = (Long)param.getValue();
            
            Invocation.Builder invocationBuilder = addHeaderBASIC()
                    .target(URILookup.getBaseAPI())
                    .path("/configurations/")
                    .path(String.valueOf(id))
                    .request(MediaType.APPLICATION_XML);
             Response response = invocationBuilder.delete();
            
            String message = response.readEntity(String.class);
            if (response.getStatus() != HTTP_OK){
                throw new Exception(message);
            }
            
            MessageHandler.successMessage("Configuration Deleted:", message);
        } catch (Exception e) {
            MessageHandler.failMessage("Delete Failed:", e.getMessage());
        }
        
        sendMailRemove();
    }
    public void removeConfigurationModule(ActionEvent event){
        FacesMessage facesMsg;
        try {
            UIParameter param = (UIParameter) event.getComponent().findComponent("deleteConfigurationModuleId");
            Long id = (Long)param.getValue();
            
            Invocation.Builder invocationBuilder = addHeaderBASIC()
                    .target(URILookup.getBaseAPI())
                    .path("/configurations/")
                    .path(String.valueOf(configurationDTO.getId()))
                    .path("/modules/")
                    .path(String.valueOf(id))
                    .request(MediaType.APPLICATION_XML);
            Response response = invocationBuilder.delete();
            
            String message = response.readEntity(String.class);
            if (response.getStatus() != HTTP_OK){
                throw new Exception(message);
            }
            
            MessageHandler.successMessage("Module Deleted:", message);
            sendMailRemoveModule();
        } catch (Exception e) {
            MessageHandler.failMessage("Delete Failed:", e.getMessage());
        }
    }
    
    
 
    public void removeConfigurationParameter(ActionEvent event){
        FacesMessage facesMsg;
        try {
            UIParameter param = (UIParameter) event.getComponent().findComponent("deleteConfigurationParameterId");
            Long id = (Long)param.getValue();
            
            
            Invocation.Builder invocationBuilder = addHeaderBASIC()
                    .target(URILookup.getBaseAPI())
                    .path("/configurations/")
                    .path(String.valueOf(configurationDTO.getId()))
                    .path("/parameters/")
                    .path(String.valueOf(id))
                    .request(MediaType.APPLICATION_XML);
            Response response = invocationBuilder.delete();
            
            
            if (response.getStatus() != HTTP_OK){
                String message = response.readEntity(String.class);
                throw new Exception(message);
            }
            String message = response.readEntity(String.class);
            
            MessageHandler.successMessage("Parameter Deleted:", message);
            sendMailRemoveParameter();
        } catch (Exception e) {
            MessageHandler.failMessage("Delete Failed:", e.getMessage());
        }
    }
    
    public void removeConfigurationArtifact(ActionEvent event){
        FacesMessage facesMsg;
        try {
            UIParameter param = (UIParameter) event.getComponent().findComponent("deleteConfigurationArtifactId");
            Long id = (Long)param.getValue();
            
            Invocation.Builder invocationBuilder = addHeaderBASIC()
                    .target(URILookup.getBaseAPI())
                    .path("/configurations/")
                    .path(String.valueOf(configurationDTO.getId()))
                    .path("/artifacts/")
                    .path(String.valueOf(id))
                    .request(MediaType.APPLICATION_XML);
            Response response = invocationBuilder.delete();
            
            String message = response.readEntity(String.class);
            if (response.getStatus() != HTTP_OK){
                throw new Exception(message);
            }
            
            MessageHandler.successMessage("Artifact Deleted:", message);
            sendMailRemoveArtifact();
        } catch (Exception e) {
            MessageHandler.failMessage("Delete Failed:", e.getMessage());
        }
    }
        
    public List<UserDTO> getAllUsers(){
        try {
            List<UserDTO> users=addHeaderBASIC()
                    .target(URILookup.getBaseAPI())
                    .path("/users")
                    .request(MediaType.APPLICATION_XML)
                    .get(new GenericType<List<UserDTO>>() {});

            return users;
        } catch (Exception e) {
            FacesExceptionHandler.handleException(e, "Unexpected error! Try again latter!", logger);
            return null;
        }
    }
    public List<AdministratorDTO> getAllAdministrators(){
        try {
            List<AdministratorDTO> administratos=addHeaderBASIC()
                    .target(URILookup.getBaseAPI())
                    .path("/administrators")
                    .request(MediaType.APPLICATION_XML)
                    .get(new GenericType<List<AdministratorDTO>>() {});

            return administratos;
        } catch (Exception e) {
            FacesExceptionHandler.handleException(e, "Unexpected error! Try again latter!", logger);
            return null;
        }
    }
    public List<ClientDTO> getAllClients(){
        try {
            Invocation.Builder invocationBuilder = addHeaderBASIC()
                    .target(URILookup.getBaseAPI())
                    .path("/clients")
                    .request(MediaType.APPLICATION_XML);
            
            Response response = invocationBuilder.get(Response.class);
            if (response.getStatus() != HTTP_OK){
                String message = response.readEntity(String.class);
                throw new Exception(message);
            }
           
            List<ClientDTO> clientsDTO =
                response.readEntity(new GenericType<List<ClientDTO>>() {}); 
            
            return clientsDTO;
        } catch (Exception e) {
            MessageHandler.failMessage("Unexpected error!", e.getMessage());
            return null;
        }
    }
    public List<TemplateDTO> getAllTemplates(){
        try {
            Invocation.Builder invocationBuilder = addHeaderBASIC()
                    .target(URILookup.getBaseAPI())
                    .path("/templates")
                    .request(MediaType.APPLICATION_XML);
            
            Response response = invocationBuilder.get(Response.class);

            List<TemplateDTO> templateDTO =
                response.readEntity(new GenericType<List<TemplateDTO>>() {}); 
          
            return templateDTO;
        } catch (Exception e) {
            MessageHandler.failMessage("Unexpected error!", e.getMessage());      
            return null;
        }
    }
    public List<ConfigurationDTO> getAllConfigurations(){
        try {
            Invocation.Builder invocationBuilder = addHeaderBASIC()
                    .target(URILookup.getBaseAPI())
                    .path("/configurations")
                    .request(MediaType.APPLICATION_XML);
            
            Response response = invocationBuilder.get(Response.class);
            if (response.getStatus() != HTTP_OK){
                String message = response.readEntity(String.class);
                throw new Exception(message);
            }
           
            List<ConfigurationDTO> configurationDTO =
                response.readEntity(new GenericType<List<ConfigurationDTO>>() {}); 
            
            return configurationDTO;
        } catch (Exception e) {
            MessageHandler.failMessage("Unexpected error!", e.getMessage());
            return null;
        }
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
            if (response.getStatus() != HTTP_OK){
                String message = response.readEntity(String.class);
                throw new Exception(message);
            }
            
            List<ConfigurationDTO> configurationsDTO =
                response.readEntity(new GenericType<List<ConfigurationDTO>>() {}); 
          
            return configurationsDTO;
        } catch (Exception e) {
            MessageHandler.failMessage("Unexpected error!", e.getMessage()); 
            return null;
        }
    }
    public List<ModuleDTO> getConfigurationModules(){
        try {
            Invocation.Builder invocationBuilder = addHeaderBASIC()
                    .target(URILookup.getBaseAPI())
                    .path("/configurations/")
                    .path(String.valueOf(configurationDTO.getId()))
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
    
    public List<ParameterDTO> getConfigurationParameters(){
        try {
            Invocation.Builder invocationBuilder = addHeaderBASIC()
                    .target(URILookup.getBaseAPI())
                    .path("/configurations/")
                    .path(String.valueOf(configurationDTO.getId()))
                    .path("/parameters")
                    .request(MediaType.APPLICATION_XML);
            
            Response response = invocationBuilder.get(Response.class);
            if (response.getStatus() != HTTP_OK){
                String message = response.readEntity(String.class);
                throw new Exception(message);
            }

            List<ParameterDTO> parameterDTOs =
                response.readEntity(new GenericType<List<ParameterDTO>>() {}); 

            return parameterDTOs;
        } catch (Exception e) {
            MessageHandler.failMessage("Unexpected error!", e.getMessage());      
            return null;
        }
    }
    
    
    public List<ArtifactDTO> getConfigurationArtifacts(){
        try {
            Invocation.Builder invocationBuilder = addHeaderBASIC()
                    .target(URILookup.getBaseAPI())
                    .path("/configurations/")
                    .path(String.valueOf(configurationDTO.getId()))
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
    public List<CommentDTO> getConfigurationComments(){
        try {
            Invocation.Builder invocationBuilder = addHeaderBASIC()
                    .target(URILookup.getBaseAPI())
                    .path("/configurations/")
                    .path(String.valueOf(configurationDTO.getId()))
                    .path("/comments")
                    .request(MediaType.APPLICATION_XML);
            
            Response response = invocationBuilder.get(Response.class);
            if (response.getStatus() != HTTP_OK){
                String message = response.readEntity(String.class);
                throw new Exception(message);
            }

            List<CommentDTO> commentDTO =
                response.readEntity(new GenericType<List<CommentDTO>>() {}); 

            return commentDTO;
        } catch (Exception e) {
            MessageHandler.failMessage("Unexpected error!", e.getMessage());
            return null;
        }
    }
    
    public List<ModuleDTO> getTemplateModules(){
        try {

            Invocation.Builder invocationBuilder = addHeaderBASIC()
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
            Invocation.Builder invocationBuilder = addHeaderBASIC()
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
    
    public void emailExample(){
        /*
        EmailDTO email = emailManager.removeProposta(admin, currentProposta, recipients);
        
        Invocation.Builder invocationBuilder = addHeaderBASIC()
                    .target(URILookup.getBaseAPI())
                    .path("/email")
                    .request(MediaType.APPLICATION_XML);
        Response response = invocationBuilder.post(Entity.xml(email));
        */
    }
    public void sendMailUpdate(){
        try {
            FacesContext context = FacesContext.getCurrentInstance();
            javax.faces.application.Application app = context.getApplication();
            UserManager userManager = app.evaluateExpressionGet(context, "#{userManager}", UserManager.class);
            AdministratorDTO administratorDTO = getLoggedUser(userManager.getUsername());
            EmailManager emailManager = app.evaluateExpressionGet(context, "#{emailManager}", EmailManager.class);
            EmailDTO email = email = emailManager.sendEmailUpdate(administratorDTO, configurationDTO, clientDTO); 
            
            Invocation.Builder invocationBuilder = addHeaderBASIC()
                        .target(URILookup.getBaseAPI())
                        .path("/email")
                        .request(MediaType.APPLICATION_XML);
            Response response = invocationBuilder.post(Entity.xml(email));   
            
            String message = response.readEntity(String.class);
            if (response.getStatus() != HTTP_OK){
                throw new Exception(message);
            }
            MessageHandler.successMessage("Message sent: ", message);
        } catch (Exception e) {
            FacesExceptionHandler.handleException(e, "Unexpected error! Try again latter!", logger);
        } 
    }
    public void sendMailRemove(){
        try {
            FacesContext context = FacesContext.getCurrentInstance();
            javax.faces.application.Application app = context.getApplication();
            UserManager userManager = app.evaluateExpressionGet(context, "#{userManager}", UserManager.class);
            AdministratorDTO administratorDTO = getLoggedUser(userManager.getUsername());
            EmailManager emailManager = app.evaluateExpressionGet(context, "#{emailManager}", EmailManager.class);
            EmailDTO email = email = emailManager.sendEmailRemove(administratorDTO, configurationDTO, clientDTO); 
            
            Invocation.Builder invocationBuilder = addHeaderBASIC()
                        .target(URILookup.getBaseAPI())
                        .path("/email")
                        .request(MediaType.APPLICATION_XML);
            Response response = invocationBuilder.post(Entity.xml(email));   
            
            String message = response.readEntity(String.class);
            if (response.getStatus() != HTTP_OK){
                throw new Exception(message);
            }
            MessageHandler.successMessage("Message sent: ", message);
        } catch (Exception e) {
            FacesExceptionHandler.handleException(e, "Unexpected error! Try again latter!", logger);
        } 
    }
    public void sendMailCreateModule(String moduleName){
        try {
            FacesContext context = FacesContext.getCurrentInstance();
            javax.faces.application.Application app = context.getApplication();
            UserManager userManager = app.evaluateExpressionGet(context, "#{userManager}", UserManager.class);
            AdministratorDTO administratorDTO = getLoggedUser(userManager.getUsername());
            EmailManager emailManager = app.evaluateExpressionGet(context, "#{emailManager}", EmailManager.class);
            EmailDTO email = email = emailManager.sendEmailModuleCreate(administratorDTO, configurationDTO, clientDTO, moduleName); 
            
            Invocation.Builder invocationBuilder = addHeaderBASIC()
                        .target(URILookup.getBaseAPI())
                        .path("/email")
                        .request(MediaType.APPLICATION_XML);
            Response response = invocationBuilder.post(Entity.xml(email));   
            
            String message = response.readEntity(String.class);
            if (response.getStatus() != HTTP_OK){
                throw new Exception(message);
            }
            MessageHandler.successMessage("Message sent: ", message);
        } catch (Exception e) {
            FacesExceptionHandler.handleException(e, "Unexpected error! Try again latter!", logger);
        } 
    }
    public void sendMailRemoveModule(){
        try {
            FacesContext context = FacesContext.getCurrentInstance();
            javax.faces.application.Application app = context.getApplication();
            UserManager userManager = app.evaluateExpressionGet(context, "#{userManager}", UserManager.class);
            AdministratorDTO administratorDTO = getLoggedUser(userManager.getUsername());
            EmailManager emailManager = app.evaluateExpressionGet(context, "#{emailManager}", EmailManager.class);
            EmailDTO email = email = emailManager.sendEmailModuleRemove(administratorDTO, configurationDTO, clientDTO); 
            
            Invocation.Builder invocationBuilder = addHeaderBASIC()
                        .target(URILookup.getBaseAPI())
                        .path("/email")
                        .request(MediaType.APPLICATION_XML);
            Response response = invocationBuilder.post(Entity.xml(email));   
            
            String message = response.readEntity(String.class);
            if (response.getStatus() != HTTP_OK){
                throw new Exception(message);
            }
            MessageHandler.successMessage("Message sent: ", message);
        } catch (Exception e) {
            FacesExceptionHandler.handleException(e, "Unexpected error! Try again latter!", logger);
        } 
    }
    public void sendMailCreateArtifact(String artifactName){
        try {
            FacesContext context = FacesContext.getCurrentInstance();
            javax.faces.application.Application app = context.getApplication();
            UserManager userManager = app.evaluateExpressionGet(context, "#{userManager}", UserManager.class);
            AdministratorDTO administratorDTO = getLoggedUser(userManager.getUsername());
            EmailManager emailManager = app.evaluateExpressionGet(context, "#{emailManager}", EmailManager.class);
            EmailDTO email = email = emailManager.sendEmailArtifactCreate(administratorDTO, configurationDTO, clientDTO, artifactName); 
            
            Invocation.Builder invocationBuilder = addHeaderBASIC()
                        .target(URILookup.getBaseAPI())
                        .path("/email")
                        .request(MediaType.APPLICATION_XML);
            Response response = invocationBuilder.post(Entity.xml(email));   
            
            String message = response.readEntity(String.class);
            if (response.getStatus() != HTTP_OK){
                throw new Exception(message);
            }
            MessageHandler.successMessage("Message sent: ", message);
        } catch (Exception e) {
            FacesExceptionHandler.handleException(e, "Unexpected error! Try again latter!", logger);
        } 
    }
    
    public void sendMailCreateParameter(String parameterName){
        try {
            FacesContext context = FacesContext.getCurrentInstance();
            javax.faces.application.Application app = context.getApplication();
            UserManager userManager = app.evaluateExpressionGet(context, "#{userManager}", UserManager.class);
            AdministratorDTO administratorDTO = getLoggedUser(userManager.getUsername());
            EmailManager emailManager = app.evaluateExpressionGet(context, "#{emailManager}", EmailManager.class);
            EmailDTO email = email = emailManager.sendEmailParameterCreate(administratorDTO, configurationDTO, clientDTO, parameterName); 
            
            Invocation.Builder invocationBuilder = addHeaderBASIC()
                        .target(URILookup.getBaseAPI())
                        .path("/email")
                        .request(MediaType.APPLICATION_XML);
            Response response = invocationBuilder.post(Entity.xml(email));   
            
            String message = response.readEntity(String.class);
            if (response.getStatus() != HTTP_OK){
                throw new Exception(message);
            }
            MessageHandler.successMessage("Message sent: ", message);
        } catch (Exception e) {
            FacesExceptionHandler.handleException(e, "Unexpected error! Try again latter!", logger);
        } 
    }
    
    public void sendMailRemoveArtifact(){
        try {
            FacesContext context = FacesContext.getCurrentInstance();
            javax.faces.application.Application app = context.getApplication();
            UserManager userManager = app.evaluateExpressionGet(context, "#{userManager}", UserManager.class);
            AdministratorDTO administratorDTO = getLoggedUser(userManager.getUsername());
            EmailManager emailManager = app.evaluateExpressionGet(context, "#{emailManager}", EmailManager.class);
            EmailDTO email = email = emailManager.sendEmailArtifactRemove(administratorDTO, configurationDTO, clientDTO); 
            
            Invocation.Builder invocationBuilder = addHeaderBASIC()
                        .target(URILookup.getBaseAPI())
                        .path("/email")
                        .request(MediaType.APPLICATION_XML);
            Response response = invocationBuilder.post(Entity.xml(email));   
            
            String message = response.readEntity(String.class);
            if (response.getStatus() != HTTP_OK){
                throw new Exception(message);
            }
            MessageHandler.successMessage("Message sent: ", message);
        } catch (Exception e) {
            FacesExceptionHandler.handleException(e, "Unexpected error! Try again latter!", logger);
        } 
    }
    
    @Asynchronous
    public void sendMailRemoveParameter(){
        try {
            FacesContext context = FacesContext.getCurrentInstance();
            javax.faces.application.Application app = context.getApplication();
            UserManager userManager = app.evaluateExpressionGet(context, "#{userManager}", UserManager.class);
            AdministratorDTO administratorDTO = getLoggedUser(userManager.getUsername());
            EmailManager emailManager = app.evaluateExpressionGet(context, "#{emailManager}", EmailManager.class);
            EmailDTO email = email = emailManager.sendEmailParameterRemove(administratorDTO, configurationDTO, clientDTO); 
            
            Invocation.Builder invocationBuilder = addHeaderBASIC()
                        .target(URILookup.getBaseAPI())
                        .path("/email")
                        .request(MediaType.APPLICATION_XML);
            Response response = invocationBuilder.post(Entity.xml(email));   
            
            String message = response.readEntity(String.class);
            if (response.getStatus() != HTTP_OK){
                throw new Exception(message);
            }
            MessageHandler.successMessage("Message sent: ", message);
        } catch (Exception e) {
            FacesExceptionHandler.handleException(e, "Unexpected error! Try again latter!", logger);
        } 
    }
}
