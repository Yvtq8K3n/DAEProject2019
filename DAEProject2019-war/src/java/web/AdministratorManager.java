package web;

import dtos.AdministratorDTO;
import dtos.ClientDTO;
import dtos.DocumentDTO;    
import dtos.ProductDTO;
import dtos.UserDTO;
import ejbs.ConfigurationBean;
import ejbs.ProductCatalogBean;
import entities.Configuration;
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
import javax.faces.application.Application;
import javax.inject.Named;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import util.URILookup;
import lombok.Getter;
import lombok.Setter;
import org.glassfish.jersey.client.authentication.HttpAuthenticationFeature;


@Named(value = "administratorManager")
@ManagedBean
@SessionScoped
public class AdministratorManager implements Serializable {
    private static final Logger logger = Logger.getLogger("web.AdministratorManager");
    
    private @Getter @Setter UIComponent component;
    
    private @Getter @Setter ProductDTO newProductDTO;
    private @Getter @Setter ClientDTO newClient;
    private @Getter @Setter AdministratorDTO newAdministrator;
    private List<Configuration> allConfigurations;
    private List<Configuration> currentConfigurations;
    private DocumentDTO document;
    
    @EJB
    private ProductCatalogBean productCatalogBean;
    @EJB
    private ConfigurationBean configurationBean;

    @ManagedProperty(value = "#{uploadManager}")
    private UploadManager uploadManager;
    
    @ManagedProperty(value="#{userManager}")
    private @Getter @Setter UserManager userManager;
    
    public AdministratorManager() {
        newClient = new ClientDTO();
        newProductDTO = new ProductDTO();
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
        
        logger.warning("Username:"+userManager.getUsername());
        logger.warning("Password:"+userManager.getPassword());
        String username = userManager.getUsername();
        String password = userManager.getPassword();
        
        HttpAuthenticationFeature feature = HttpAuthenticationFeature.basic(username, password);
        client.register(feature);
        return client;
    }
    
    public String createClient(){
        FacesMessage facesMsg;
        try {
            Client client = addHeaderBASIC();
            Invocation.Builder invocationBuilder = 
                client.target(URILookup.getBaseAPI())
                    .path("/clients")
                    .request(MediaType.APPLICATION_XML);
            
            Response response = invocationBuilder.post(Entity.xml(newClient));
        
            newClient.reset();
            facesMsg = new FacesMessage(FacesMessage.SEVERITY_INFO, "User Created:", "A user was successfully created");
            FacesContext.getCurrentInstance().addMessage(null, facesMsg);  
        } catch (Exception e) {
            facesMsg = new FacesMessage(FacesMessage.SEVERITY_WARN, "Create Failed:", "Problem creating a client in method createClient");
            FacesContext.getCurrentInstance().addMessage(null, facesMsg);  
            return null;
        }
        return "/admin/users/clients/view.xhtml?faces-redirect=true";
    }
    public String createAdmin(){
        FacesMessage facesMsg;
        try {
            Invocation.Builder invocationBuilder = 
                addHeaderBASIC().target(URILookup.getBaseAPI())
                    .path("/administrators")
                    .request(MediaType.APPLICATION_XML);
            
            Response response = invocationBuilder.post(Entity.xml(newAdministrator));
        
            newAdministrator.reset();
            
            //facesMsg = new FacesMessage(FacesMessage.SEVERITY_INFO, "User Created:", "A user was successfully created");
        } catch (Exception e) {
            logger.warning("Problem removing a client in method removeClient.");
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
           
        try {
            productCatalogBean.create(newProductDTO, currentConfigurations);
           
            reset();//Resets the form, the only downside of using ajax
        } catch (Exception e) {
            FacesExceptionHandler.handleException(e, "An unexpected error has occurred while creating a Template", component, logger);
            return null;
        }  
        return "/index.xhtml?faces-redirect=true";
    }

    public void removeClient(ActionEvent event){
        FacesMessage facesMsg;
        try {
            UIParameter param = (UIParameter) event.getComponent().findComponent("deleteClientId");
            String clientId = param.getValue().toString();
            
            Invocation.Builder invocationBuilder = addHeaderBASIC()
                    .target(URILookup.getBaseAPI())
                    .path("/clients/"+clientId)
                    .request(MediaType.APPLICATION_XML);
            Response response = invocationBuilder.delete();
            logger.warning("Response:"+response.getStatus());
            facesMsg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Client Deleted:", "A client was successfully deleted");
        } catch (Exception e) {
            facesMsg = new FacesMessage(FacesMessage.SEVERITY_WARN, "Delete Failed:", "A problem has occurred while attempting to remove a client.");
        }
        FacesContext.getCurrentInstance().addMessage(null, facesMsg);
    }
    public void removeAdministrator(ActionEvent event){
        FacesMessage facesMsg;
        try {
            UIParameter param = (UIParameter) event.getComponent().findComponent("deleteAdministratorId");
            String administratorId = param.getValue().toString();

            Invocation.Builder invocationBuilder = addHeaderBASIC()
                    .target(URILookup.getBaseAPI())
                    .path("/administrators/"+administratorId)
                    .request(MediaType.APPLICATION_XML);
            Response response = invocationBuilder.delete();
            
            facesMsg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Administrator Deleted:", "A administrator was successfully deleted");
        } catch (Exception e) {
            facesMsg = new FacesMessage(FacesMessage.SEVERITY_WARN, "Delete Failed:", "A problem has occurred while attempting to remove a client.");
        }
        FacesContext.getCurrentInstance().addMessage(null, facesMsg);
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
      
    public List<Configuration> getAllConfigurations(){
        if (allConfigurations == null) {
            allConfigurations = new ArrayList<>();
            allConfigurations.addAll(configurationBean.getAll());
        }
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
                    .path("/clients")
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
        //guestManager.reset();
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

    public UploadManager getUploadManager() {
        return uploadManager;
    }

    public void setUploadManager(UploadManager uploadManager) {
        this.uploadManager = uploadManager;
    }

    
    public List<Configuration> getCurrentConfigurations(){
        return currentConfigurations;
    }
    public void addConfiguration(Configuration selectedConfiguration){
        allConfigurations.remove(selectedConfiguration);
        if (currentConfigurations == null) currentConfigurations = new ArrayList<>();
        currentConfigurations.add(selectedConfiguration);
    }
    public void removeConfiguration(Configuration selectedConfiguration){
        currentConfigurations.remove(selectedConfiguration);
        allConfigurations.add(selectedConfiguration);
    }
    public void setCurrentConfigurations(List<Configuration> currentConfigurations) {
        this.currentConfigurations = currentConfigurations;
    }    
}
