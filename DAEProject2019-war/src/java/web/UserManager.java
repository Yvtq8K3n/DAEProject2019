/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package web;


import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.mail.MessagingException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 *
 * @author Joao Marquez
 */
@ManagedBean(name="userManager")
@SessionScoped
public class UserManager implements Serializable{

    private static final Logger logger = Logger.getLogger("web.UserManager");
    
    private String username;
    
    private String password;
    
    private String message;

    Boolean loginFlag = true;
        
    public UserManager() {
    }
    
    public void login() throws MessagingException {
        FacesContext context = FacesContext.getCurrentInstance();
        HttpServletRequest request =
        (HttpServletRequest) context.getExternalContext().getRequest();
        try {
            logger.warning(this.username);
            logger.warning(this.password);
            request.login(this.username, this.password);
            
        } catch (ServletException e) {
            logger.log(Level.WARNING, e.getMessage());
            
            message = "Login Failed: Invalid username or password.";
            //return "/faces/index?faces-redirect=true";
        }
        if(isUserInRole("Administrator")){
            logger.info("Administrator");
            //return "/faces/admin/index?faces-redirect=true";
        }
        if(isUserInRole("Client")){
            logger.info("Client");
            //return "/faces/client/index?faces-redirect=true";
        }
        
        message = "Login Failed: Invalid username or password.";
        //return "/faces/index?faces-redirect=true";        
    }
    
    public boolean isClient(){
        return isUserInRole("Client");
    }
    
    public boolean isAdmin(){
        return isUserInRole("Administrator");
    }
    
    public boolean isUserInRole(String role) {
        return (isSomeUserAuthenticated() &&
            FacesContext.getCurrentInstance().getExternalContext().isUserInRole(role));
    }
    public boolean isSomeUserAuthenticated() {
        return FacesContext.getCurrentInstance().getExternalContext().getUserPrincipal()!=null;
    }
    
    public String logout() {
        FacesContext context = FacesContext.getCurrentInstance();
        // remove data from beans:
        for (String bean : context.getExternalContext().getSessionMap().keySet()) {
            context.getExternalContext().getSessionMap().remove(bean);
        }
        // destroy session:
        HttpSession session =
        (HttpSession) context.getExternalContext().getSession(false);
        session.invalidate();
        // using faces-redirect to initiate a new request:
        return "/faces/index?faces-redirect=true";
    }
    
    public String clearLogin(){
        if(isSomeUserAuthenticated()){
            logout();
        }
        return "index_login.xhtml?faces-redirect=true";
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }    

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
    
    public Boolean getLoginFlag() {
        return loginFlag;
    }

    public void setLoginFlag(Boolean loginFlag) {
        this.loginFlag = loginFlag;
    }
}
