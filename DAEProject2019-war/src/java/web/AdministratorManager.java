package web;

import dtos.AdministratorDTO;
import dtos.ClientDTO;
import dtos.DocumentDTO;
import dtos.ProductDTO;
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
import javax.ejb.EJBException;
import javax.inject.Named;
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

//@Named(value = "administratorManager")
@ManagedBean
@SessionScoped
public class AdministratorManager implements Serializable {
    private static final Logger logger = Logger.getLogger("web.AdministratorManager");
    private UIComponent component;
    private Client client;
    
    @EJB
    private UserBean usersBean;
    @EJB
    private AdministratorBean administratorBean;
    @EJB
    private ClientBean clientBean;
    @EJB
    private ProductCatalogBean productCatalogBean;
    @EJB
    private ConfigurationBean configurationBean;

    private DocumentDTO document;

    @ManagedProperty(value = "#{uploadManager}")
    private UploadManager uploadManager;

    @ManagedProperty(value = "#{guestManager}")
    private GuestManager guestManager;

    
    @PostConstruct
    public void Init(){
        client = ClientBuilder.newClient();
    };

    private ProductDTO newProductDTO;
    private ClientDTO newClient;//Gonna be a DTOlateranyway
    private AdministratorDTO newAdministrator;
    private List<Configuration> allConfigurations;
    private List<Configuration> currentConfigurations;

    
    public AdministratorManager() {
        newClient = new ClientDTO();
        newProductDTO = new ProductDTO();
        newAdministrator = new AdministratorDTO();
    }
    
    public String createClient(){
        FacesMessage facesMsg;
        try {
            clientBean.create(newClient);
            clearNewClient();
            facesMsg = new FacesMessage(FacesMessage.SEVERITY_INFO, "User Created:", "A user was successfully created");
        } catch (Exception e) {
            logger.warning("Problem removing a client in method removeClient.");
            facesMsg = new FacesMessage(FacesMessage.SEVERITY_WARN, "Create Failed:", "Problem creating a client in method createClient");
            return null;
        }
        FacesContext.getCurrentInstance().addMessage(null, facesMsg);  
        return "/admin/users/clients/view.xhtml?faces-redirect=true";
    }
    public String createAdmin(){
        FacesMessage facesMsg;
        try {
            administratorBean.create(newAdministrator);
            
            facesMsg = new FacesMessage(FacesMessage.SEVERITY_INFO, "User Created:", "A user was successfully created");
        } catch (Exception e) {
            logger.warning("Problem removing a client in method removeClient.");
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
            String id = param.getValue().toString();

            clientBean.remove(id);
            facesMsg = new FacesMessage(FacesMessage.SEVERITY_INFO, "User Deleted:", "A user was successfully deleted");
        } catch (Exception e) {
            logger.warning("Problem removing a client in method removeClient.");
            facesMsg = new FacesMessage(FacesMessage.SEVERITY_WARN, "Delete Failed:", "Problem removing a client in method removeClient");
        }
        FacesContext.getCurrentInstance().addMessage(null, facesMsg);
    }
    public void removeAdministrator(ActionEvent event){
        FacesMessage facesMsg;
        try {
            UIParameter param = (UIParameter) event.getComponent().findComponent("deleteAdministratorId");
            String id = param.getValue().toString();

            administratorBean.remove(id);
            facesMsg = new FacesMessage(FacesMessage.SEVERITY_INFO, "User Deleted:", "A user was successfully deleted");
        } catch (Exception e) {
            logger.warning("Problem removing a administrator in method removeAdministrator.");
            facesMsg = new FacesMessage(FacesMessage.SEVERITY_WARN, "Delete Failed:", "Problem removing a administrator in method removeAdministrator");
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
            
            guestManager.reset();//Forces update of list
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
    
    public void clearNewClient(){
        this.newClient.setUsername(null);
        this.newClient.setName(null);
        this.newClient.setPassword(null);
        this.newClient.setEmail(null);
        this.newClient.setAddress(null);
        this.newClient.setContact(null);
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

    public void setCurrentConfigurations(List<Configuration> currentConfigurations) {
        this.currentConfigurations = currentConfigurations;
    }     
    
    public void reset(){
        allConfigurations = null;
        currentConfigurations = null;
        //guestManager.reset();
    }
      
    public List<User> getAllUsers(){
        List<User> users = new ArrayList<>();
        users.addAll(usersBean.getAll());
        
        return users;
    }
    public List<AdministratorDTO> getAllAdministrators(){
        List<AdministratorDTO> users = new ArrayList<>();
        users.addAll(administratorBean.getAll());
        
        return users;
    }
    public List<User> getAllClients(){
        List<User> users = new ArrayList<>();
        users.addAll(clientBean.getAll());
        
        return users;
    }

    public void setGuestManager(GuestManager guestManager) {
        this.guestManager = guestManager;
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


}
