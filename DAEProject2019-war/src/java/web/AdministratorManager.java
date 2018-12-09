package web;

import ejbs.AdministratorBean;
import ejbs.ClientBean;
import ejbs.UsersBean;
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
    
      
    //Gonna disappear after rest    
    @EJB
    private UsersBean usersBean;
    @EJB
    private AdministratorBean administratorBean;
    @EJB
    private ClientBean clientBean;
    
    @PostConstruct
    public void Init(){
        client = ClientBuilder.newClient();
    };    
    
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
}