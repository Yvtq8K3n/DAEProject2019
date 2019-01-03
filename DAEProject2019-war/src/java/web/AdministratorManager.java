package web;

import dtos.AdministratorDTO;
import dtos.ClientDTO;
import dtos.ArtifactDTO;
import dtos.CommentDTO;
import dtos.ConfigurationDTO;
import dtos.EmailDTO;
import dtos.ModuleDTO;
import dtos.TemplateDTO;
import dtos.UserDTO;
import ejbs.ConfigurationBean;
import ejbs.TemplateBean;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
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
    private @Getter @Setter TemplateDTO newProductDTO;
    private @Getter @Setter ClientDTO clientDTO;
    private @Getter @Setter ModuleDTO moduleDTO;
    private @Getter @Setter ConfigurationDTO configurationDTO;
    private @Getter @Setter AdministratorDTO newAdministrator;
    private @Getter @Setter CommentDTO commentDTO;
    private @Getter @Setter List<ConfigurationDTO> configurationsDTO;
    private List<ConfigurationDTO> allConfigurations;
    private List<ConfigurationDTO> currentConfigurations;
    
    @EJB
    private TemplateBean productCatalogBean;
    @EJB
    private ConfigurationBean configurationBean;
        
    /*@ManagedProperty(value="#{emailManager}")
    private EmailManager emailManager;*/
    
    public AdministratorManager() {
        clientDTO = new ClientDTO();
        commentDTO = new CommentDTO();
        configurationDTO = new ConfigurationDTO();
        newProductDTO = new TemplateDTO();
        newAdministrator = new AdministratorDTO();
        moduleDTO = new ModuleDTO();
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
            clientDTO.reset();
            
            String message = response.readEntity(String.class);
            if (response.getStatus() != HTTP_CREATED){
                throw new Exception(message);
            }
            
            MessageHandler.successMessage("Client Created:", message);
        } catch (Exception e) {
            FacesExceptionHandler.handleException(e, e.getMessage(), component, logger);
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
            
            Response response = invocationBuilder.post(Entity.xml(newAdministrator));
            newAdministrator.reset();
            
            String message = response.readEntity(String.class);
            if (response.getStatus() != HTTP_CREATED){
                throw new Exception(message);
            }
            
            MessageHandler.successMessage("Administrator Created:",message);
        } catch (Exception e) {
            FacesExceptionHandler.handleException(e, e.getMessage(), component, logger);
            return null;
        }
        return "/admin/users/administrators/view.xhtml?faces-redirect=true";
    }
    public String createProductCatalog(){
        if (currentConfigurations==null 
                || currentConfigurations.isEmpty()) {
            FacesExceptionHandler.handleException(new Exception(), "A template must have configurations", component, logger);
            return null;
        }
        
        //Create ProductCatalog
        try {
            logger.info(newProductDTO.getName());
            logger.info(newProductDTO.getDescription());
            Invocation.Builder invocationBuilder = 
                addHeaderBASIC().target(URILookup.getBaseAPI())
                    .path("/catalog")
                    .request(MediaType.APPLICATION_XML);
        
            Response response = invocationBuilder.post(Entity.xml(newProductDTO));
            
            logger.info("Template Created:"+response.getStatus());
            String message = response.readEntity(String.class);
            if (response.getStatus() != HTTP_CREATED){
                throw new Exception(message);
            }
            MessageHandler.successMessage("Template Created:", message);
        } catch (Exception e) {
            FacesExceptionHandler.handleException(e, e.getMessage(), component, logger);
            return null;
        }  
        
        //Associate Configurations to ProductCatalog
        try{
            Invocation.Builder invocationBuilder = 
                addHeaderBASIC().target(URILookup.getBaseAPI())
                    .path("/catalog/"+newProductDTO.getId())
                    .request(MediaType.APPLICATION_JSON);
            Response response = invocationBuilder.post(Entity.json(currentConfigurations.iterator()));
           
            logger.info("Configurations Associated:"+response.getStatus());
            String message = response.readEntity(String.class);
            if (response.getStatus() != HTTP_CREATED){
                throw new Exception(message);
            }
            
            MessageHandler.successMessage("Configurations Associated:",message);
            
            //productCatalogBean.create(newProductDTO, currentConfigurations);
            newProductDTO.reset();
            allConfigurations = null;
            currentConfigurations = null;
        } catch (Exception e) {
            FacesExceptionHandler.handleException(e, "An unexpected error has occurred while creating a Template", component, logger);
            return null;
        }  
        return "/index.xhtml?faces-redirect=true";
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
            
            String message = response.readEntity(String.class);
            if (response.getStatus() != HTTP_CREATED){
                throw new Exception(message);
            }
            
            MessageHandler.successMessage("Comment Created:",message);
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
    public void createConfigurationArtifact() {
        try {
            FacesContext context = FacesContext.getCurrentInstance();
            javax.faces.application.Application app = context.getApplication();
            UploadManager uploadManager = app.evaluateExpressionGet(context, "#{uploadManager}", UploadManager.class);
        
            ArtifactDTO document = 
                new ArtifactDTO(uploadManager.getCompletePathFile(), uploadManager.getFilename(), uploadManager.getFile().getContentType());

            Invocation.Builder invocationBuilder = addHeaderBASIC()
                .target(URILookup.getBaseAPI())
                .path("/configurations/")
                .path(String.valueOf(configurationDTO.getId()))
                .path("/artifacts")
                .request(MediaType.APPLICATION_XML);

            Response response = invocationBuilder.post(Entity.xml(document));     
            
            String message = response.readEntity(String.class);
            if (response.getStatus() != HTTP_CREATED){
                throw new Exception(message);
            }
            MessageHandler.successMessage("Artifact Created:", message);
            sendMailCreateArtifact(document.getDesiredName());
        } catch (Exception e) {
            FacesExceptionHandler.handleException(e, "Unexpected error! Try again latter!", logger);
        }
    }

    
    public void updateConfiguration(){
        logger.warning("ssdasd"+configurationDTO.getId());
        logger.info("sadasd");
        
        try {
            Invocation.Builder invocationBuilder = 
                addHeaderBASIC().target(URILookup.getBaseAPI())
                    .path("/configurations")
                    .request(MediaType.APPLICATION_XML);
            
            Response response = invocationBuilder.put(Entity.xml(configurationDTO));
            
            String message = response.readEntity(String.class);
            if (response.getStatus() != HTTP_OK){
                throw new Exception(message);
            }
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
        try {
            UIParameter param = (UIParameter) event.getComponent().findComponent("deleteAdministratorId");
            String administratorId = param.getValue().toString();

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
    public void removeTemplate(ActionEvent event){
        FacesMessage facesMsg;
        try {
            UIParameter param = (UIParameter) event.getComponent().findComponent("deleteTemplateId");
            Long id = (Long)param.getValue();
            productCatalogBean.remove(id);
            
            logger.warning("ID:"+id);
            
            facesMsg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Template Deleted:", "A template was successfully deleted");
            
            //guestManager.reset();//Forces update of list
        } catch (Exception e) {
            logger.warning("Problem removing a template in method removeTemplate.");
            facesMsg = new FacesMessage(FacesMessage.SEVERITY_WARN, "Delete Failed:", "Problem removing a template in method removeTemplate");
        }
        FacesContext.getCurrentInstance().addMessage(null, facesMsg);
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
            
            MessageHandler.successMessage("Configuration Deleted:", message);
            sendMailRemoveModule();
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
            
            MessageHandler.successMessage("Configuration Deleted:", message);
            sendMailRemoveArtifact();
        } catch (Exception e) {
            MessageHandler.failMessage("Delete Failed:", e.getMessage());
        }
    }
    
    public List<ConfigurationDTO> getAllConfigurations(){
        /*if (allConfigurations == null) {
            allConfigurations = new ArrayList<>();
            allConfigurations.addAll(configurationBean.getAll());
        }*/
        return allConfigurations;
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
            List<ClientDTO> clients=addHeaderBASIC().target(URILookup.getBaseAPI())
                    .path("/clients/all")
                    .request(MediaType.APPLICATION_XML)
                    .get(new GenericType<List<ClientDTO>>() {});

            return clients;
        } catch (Exception e) {
            FacesExceptionHandler.handleException(e, "Unexpected error! Try again latter!", logger);
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
                    .path(String.valueOf(configurationDTO.getId()))
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
                    .path(String.valueOf(configurationDTO.getId()))
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
                    .path(String.valueOf(configurationDTO.getId()))
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
                        .path("/email/send")
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
                        .path("/email/send")
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
                        .path("/email/send")
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
                        .path("/email/send")
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
                        .path("/email/send")
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
                        .path("/email/send")
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
/*
    public EmailManager getEmailManager() {
        return emailManager;
    }
    public void setEmailManager(EmailManager emailManager) {
        this.emailManager = emailManager;
    }    
*/
    public List<ConfigurationDTO> getCurrentConfigurations(){
        return currentConfigurations;
    }
    public void addConfiguration(ConfigurationDTO selectedConfiguration){
        allConfigurations.remove(selectedConfiguration);
        if (currentConfigurations == null) currentConfigurations = new ArrayList<>();
        currentConfigurations.add(selectedConfiguration);
    }

    public void removeConfiguration(ConfigurationDTO selectedConfiguration){
        currentConfigurations.remove(selectedConfiguration);
        allConfigurations.add(selectedConfiguration);
    }
    public void setCurrentConfigurations(List<ConfigurationDTO> currentConfigurations) {
        this.currentConfigurations = currentConfigurations;
    }
}
