package web;

import dtos.DocumentDTO;
import ejbs.AdministratorBean;
import ejbs.ClientBean;
import ejbs.UsersBean;
import entities.Administrator;
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
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import util.URILookup;

@ManagedBean
//@Named(value = "administratorManager")
@SessionScoped
public class AdministratorManager implements Serializable {
    private static final Logger logger = Logger.getLogger("web.AdministratorManager");
    private UIComponent component;
    private Client client;
          
    //Gonna disappear after rest    
    @EJB
    private UsersBean usersBean;
    @EJB
    private AdministratorBean administratorBean;
    @EJB
    private ClientBean clientBean;
    
    private DocumentDTO document;
    
    @ManagedProperty(value = "#{uploadManager}")
    private UploadManager uploadManager;
    
    @PostConstruct
    public void Init(){
        client = ClientBuilder.newClient();
    };    
    
    private entities.Client newClient;//Gonna be a DTOlateranyway
    private Administrator newAdministrator;

    public AdministratorManager() {
        newClient = new entities.Client();
        newAdministrator = new Administrator();
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

    
    public entities.Client getNewClient() {
        return newClient;
    }
    public void setNewClient(entities.Client newClient) {
        this.newClient = newClient;
    }
    public Administrator getNewAdministrator() {
        return newAdministrator;
    }
    public void setNewAdministrator(Administrator newAdministrator) {
        this.newAdministrator = newAdministrator;
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