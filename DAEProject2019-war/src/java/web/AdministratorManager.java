package web;

import dtos.AdministratorDTO;
import dtos.ClientDTO;
import dtos.DocumentDTO;
import dtos.ProductDTO;
import dtos.UserDTO;
import ejbs.AdministratorBean;
import ejbs.ClientBean;
import ejbs.ConfigurationBean;
import ejbs.ProductCatalogBean;
import ejbs.UserBean;
import entities.Administrator;
import entities.Configuration;
import entities.User;
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
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import util.URILookup;
import org.glassfish.jersey.client.authentication.HttpAuthenticationFeature;

//@Named(value = "administratorManager")
@ManagedBean
@SessionScoped
public class AdministratorManager implements Serializable {
    private static final Logger logger = Logger.getLogger("web.AdministratorManager");
    private UIComponent component;
    private Client client;
    private HttpAuthenticationFeature feature;
    private final String baseUri = "http://localhost:8080/DAEProject2019-war/webapi";
    
    @EJB
    private ProductCatalogBean productCatalogBean;
    @EJB
    private ConfigurationBean configurationBean;

    @ManagedProperty(value = "#{uploadManager}")
    private UploadManager uploadManager;

    @ManagedProperty(value = "#{userManager}")
    private UserManager userManager;
        
    @PostConstruct
    public void Init(){
        feature = HttpAuthenticationFeature.basic(userManager.getUsername(), userManager.getPassword());
        client.register(feature);
    };

    private ProductDTO newProductDTO;
    private ClientDTO newClient;
    private DocumentDTO document;
    private AdministratorDTO newAdministrator;
    private List<Configuration> allConfigurations;
    private List<Configuration> currentConfigurations;

    
    public AdministratorManager() {
        client = ClientBuilder.newClient();
        newClient = new ClientDTO();
        newProductDTO = new ProductDTO();
        newAdministrator = new AdministratorDTO();
    }
    
    public String createClient(){
        FacesMessage facesMsg;
        try {
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
                client.target(URILookup.getBaseAPI())
                    .path("/administrators")
                    .request(MediaType.APPLICATION_XML);
            
            Response response = invocationBuilder.post(Entity.xml(newAdministrator));
        
            newAdministrator.reset();
            
            facesMsg = new FacesMessage(FacesMessage.SEVERITY_INFO, "User Created:", "A user was successfully created");
        } catch (Exception e) {
            FacesExceptionHandler.handleException(e, "FAILED", component, logger);
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
            
            Invocation.Builder invocationBuilder = 
                client.target(URILookup.getBaseAPI())
                    .path("/clients/"+clientId)
                    .request(MediaType.APPLICATION_XML);
            Response response = invocationBuilder.delete();

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

            Invocation.Builder invocationBuilder = 
                client.target(URILookup.getBaseAPI())
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
            List<UserDTO> users=client.target(URILookup.getBaseAPI())
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
            List<AdministratorDTO> administratos=client.target(URILookup.getBaseAPI())
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
            List<ClientDTO> clients=client.target(URILookup.getBaseAPI())
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
    
    public UIComponent getComponent() {
        return component;
    }

    public void setComponent(UIComponent component) {
        this.component = component;
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
            /*lient.target(URILookup.getBaseAPI())
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

    public ClientDTO getNewClient() {
        return newClient;
    }
    public void setNewClient(ClientDTO newClient) {
        this.newClient = newClient;
    }
    public AdministratorDTO getNewAdministrator() {
        return newAdministrator;
    }
    public void setNewAdministrator(AdministratorDTO newAdministrator) {
        this.newAdministrator = newAdministrator;
    }
    public ProductDTO getNewProductDTO() {
        return newProductDTO;
    }
    public void setNewProductDTO(ProductDTO productDTO) {
        this.newProductDTO = productDTO;
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
