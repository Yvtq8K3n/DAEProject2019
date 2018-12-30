package web;

import dtos.AdministratorDTO;
import dtos.ClientDTO;
import dtos.ModuleDTO;
import dtos.DocumentDTO;    
import dtos.EmailDTO;
import dtos.ConfigurationDTO;
import dtos.TemplateDTO;
import dtos.UserDTO;
import ejbs.ConfigurationBean;
import ejbs.TemplateBean;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
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
    private @Getter @Setter ClientDTO newClient;
    private @Getter @Setter AdministratorDTO newAdministrator;
    private List<ModuleDTO> allConfigurations;
    private List<ModuleDTO> currentConfigurations;
    private DocumentDTO document;
    
    @EJB
    private TemplateBean productCatalogBean;
    @EJB
    private ConfigurationBean configurationBean;

    @ManagedProperty(value = "#{uploadManager}")
    private UploadManager uploadManager;
    
    @ManagedProperty(value="#{userManager}")
    private @Getter @Setter UserManager userManager;
    
    @ManagedProperty(value="#{emailManager}")
    private EmailManager emailManager;
    
    public AdministratorManager() {
        newClient = new ClientDTO();
        newProductDTO = new TemplateDTO();
        newAdministrator = new AdministratorDTO();
    }
        
    @PostConstruct
    public void Init(){
        
    };
    
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
            
            Response response = invocationBuilder.post(Entity.xml(newClient));
            newClient.reset();
            
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
      
    public List<ModuleDTO> getAllConfigurations(){
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
    
    /*public void setGuestManager(GuestManager guestManager) {
        this.guestManager = guestManager;
    }*/
    public void reset(){
        allConfigurations = null;
        currentConfigurations = null;
    }
    

    public String uploadDocument() {
        try {
            logger.warning("entrou0");
            logger.warning("File: " +uploadManager.getCompletePathFile());
            logger.warning("File: " +uploadManager.getCompletePathFile());
            logger.warning("File: " +uploadManager.getFilename());
            logger.warning("File: " +String.valueOf(uploadManager.getFile().getSize()));
            document = new DocumentDTO(uploadManager.getCompletePathFile(), uploadManager.getFilename(), uploadManager.getFile().getContentType());
            logger.warning("entrou");
            /*client.target(URILookup.getBaseAPI())
                    .path("/files/")
                    .request(MediaType.TEXT_HTML)
                    .put(Entity.text(document));
            logger.warning("entrou2");*/
        } catch (Exception e) {
            FacesExceptionHandler.handleException(e, "Unexpected error! Try again latter!", logger);
            return null;
        }

        return "/index.xhtml?faces-redirect=true";
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

    public EmailManager getEmailManager() {
        return emailManager;
    }

    public void setEmailManager(EmailManager emailManager) {
        this.emailManager = emailManager;
    }    
    
    public UploadManager getUploadManager() {
        return uploadManager;
    }

    public void setUploadManager(UploadManager uploadManager) {
        this.uploadManager = uploadManager;
    }

    
    public List<ModuleDTO> getCurrentConfigurations(){
        return currentConfigurations;
    }
    public void addConfiguration(ModuleDTO selectedConfiguration){
        allConfigurations.remove(selectedConfiguration);
        if (currentConfigurations == null) currentConfigurations = new ArrayList<>();
        currentConfigurations.add(selectedConfiguration);
    }
    public void removeConfiguration(ModuleDTO selectedConfiguration){
        currentConfigurations.remove(selectedConfiguration);
        allConfigurations.add(selectedConfiguration);
    }
    public void setCurrentConfigurations(List<ModuleDTO> currentConfigurations) {
        this.currentConfigurations = currentConfigurations;
    }
    
    
    
}
