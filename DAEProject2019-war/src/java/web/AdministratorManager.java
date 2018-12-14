package web;

import dtos.ClientDTO;
import dtos.ProductDTO;
import ejbs.AdministratorBean;
import ejbs.ClientBean;
import ejbs.ConfigurationBean;
import ejbs.ProductCatalogBean;
import ejbs.UsersBean;
import entities.Administrator;
import entities.Configuration;
import entities.User;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
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

@Named(value = "administratorManager")
@SessionScoped
public class AdministratorManager implements Serializable {
    private static final Logger logger = Logger.getLogger("web.AdministratorManager");
    private UIComponent component;
    private Client client;
    
    @ManagedProperty(value = "#{guestManager}")
    private GuestManager guestManager;
          
    //Gonna disappear after rest    
    @EJB
    private UsersBean usersBean;
    @EJB
    private AdministratorBean administratorBean;
    @EJB
    private ClientBean clientBean;
    @EJB
    private ProductCatalogBean productCatalogBean;
    @EJB
    private ConfigurationBean configurationBean;
    
    @PostConstruct
    public void Init(){
        client = ClientBuilder.newClient();
    };    

    private ProductDTO newProductDTO;
    private ClientDTO newClient;//Gonna be a DTOlateranyway
    private Administrator newAdministrator;
    private List<Configuration> allConfigurations;
    private List<Configuration> currentConfigurations;
    
    private String name;
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getWelcomeMessage() {
      return "Hello " + name;
   }
    
    public AdministratorManager() {
        newClient = new ClientDTO();
        newProductDTO = new ProductDTO();
        newAdministrator = new Administrator();
    }
        
    public String createClient(){
        FacesMessage facesMsg;
        try {
            clientBean.create(newClient);
            
            facesMsg = new FacesMessage(FacesMessage.SEVERITY_INFO, "User Created:", "A user was successfully created");
        } catch (Exception e) {
            logger.warning("Problem removing a client in method removeClient.");
            facesMsg = new FacesMessage(FacesMessage.SEVERITY_WARN, "Create Failed:", "Problem creating a client in method createClient");
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
            facesMsg = new FacesMessage(FacesMessage.SEVERITY_WARN, "Create Failed:", "Problem creating a administrator in method createAdministrator");
        }
        FacesContext.getCurrentInstance().addMessage(null, facesMsg);  
        return "/admin/users/administrators/view.xhtml?faces-redirect=true";
    }
    
    public String createProductCatalog(){
        FacesMessage facesMsg;
        try {
            productCatalogBean.create(newProductDTO, currentConfigurations);
                    
            facesMsg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Template Created:", "A template was successfully created");
           
            reset();//Resets the form, the only downside of using ajax
        } catch (Exception e) {
            logger.warning("Problem creating a template in method createProductCatalog.");
            //facesMsg = new FacesMessage(FacesMessage.SEVERITY_WARN, "Create Failed:", "Problem creating a template in method createProductCatalog");
            FacesExceptionHandler.handleException(e, name, component, logger);
        }
        //FacesContext.getCurrentInstance().addMessage(null, facesMsg);  
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
    
    public ClientDTO getNewClient() {
        return newClient;
    }
    public void setNewClient(ClientDTO newClient) {
        this.newClient = newClient;
    }
    public Administrator getNewAdministrator() {
        return newAdministrator;
    }
    public void setNewAdministrator(Administrator newAdministrator) {
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
        logger.warning("HIHIHIHIH");
        allConfigurations = null;
        currentConfigurations = null;
        guestManager.reset();
    }
      
    public List<User> getAllUsers(){
        List<User> users = new ArrayList<>();
        users.addAll(usersBean.getAll());
        
        return users;
    }
    public List<User> getAllAdministrators(){
        List<User> users = new ArrayList<>();
        users.addAll(administratorBean.getAll());
        
        return users;
    }
    public List<User> getAllClients(){
        List<User> users = new ArrayList<>();
        users.addAll(clientBean.getAll());
        
        return users;
    }

    public GuestManager getGuestManager() {
        return guestManager;
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
    
    
}