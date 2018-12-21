/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

/**
 *
 * @author Joao Marquez
 */
public class MessageHandler {
    public static void successMessage(String title, String message){
        FacesContext context = FacesContext.getCurrentInstance();
        FacesMessage facesMsg = new FacesMessage(FacesMessage.SEVERITY_INFO, title, message);
        context.addMessage(null, facesMsg);
        context.getExternalContext().getFlash().setKeepMessages(true);
    }
    
    public static void failMessage(String title, String message){
        FacesContext context = FacesContext.getCurrentInstance();
        FacesMessage facesMsg = new FacesMessage(FacesMessage.SEVERITY_FATAL, title, message);
        context.addMessage(null, facesMsg);
        context.getExternalContext().getFlash().setKeepMessages(true);
    }
}
