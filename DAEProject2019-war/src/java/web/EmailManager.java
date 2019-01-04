package web;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Yvtq8
 */

import dtos.AdministratorDTO;
import dtos.ConfigurationDTO;
import dtos.ClientDTO;
import dtos.EmailDTO;
import ejbs.EmailBean;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import javax.ejb.Asynchronous;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

@ManagedBean(name="emailManager")
@SessionScoped
public class EmailManager implements Serializable{
    

    public EmailDTO sendEmailUpdate(AdministratorDTO admin, ConfigurationDTO conf, ClientDTO clientDTO){
        String recipient = clientDTO.getEmail();
        String msg = "Configuration " + conf.getName() + " was updated";
        EmailDTO email = new EmailDTO(admin.getEmail(),null,"DAE Project", msg, recipient);
        return email;   
    } 
    
    public EmailDTO sendEmailRemove(AdministratorDTO admin, ConfigurationDTO conf, ClientDTO clientDTO){
        String recipient = clientDTO.getEmail();
        String msg = "Configuration " + conf.getName() + " was removed";
        EmailDTO email = new EmailDTO(admin.getEmail(),null,"DAE Project", msg, recipient);
        return email;   
    } 
    
    public EmailDTO sendEmailModuleCreate(AdministratorDTO admin, ConfigurationDTO conf, ClientDTO clientDTO, String module){
        String recipient = clientDTO.getEmail();
        String msg = "New module " + module + " has been inserted in configuration " + conf.getName();
        EmailDTO email = new EmailDTO(admin.getEmail(),null,"DAE Project", msg, recipient);
        return email;   
    } 
    
    public EmailDTO sendEmailModuleRemove(AdministratorDTO admin, ConfigurationDTO conf, ClientDTO clientDTO){
        String recipient = clientDTO.getEmail();
        String msg = "Module has been removed from configuration " + conf.getName();
        EmailDTO email = new EmailDTO(admin.getEmail(),null,"DAE Project", msg, recipient);
        return email;   
    } 
    
    public EmailDTO sendEmailArtifactCreate(AdministratorDTO admin, ConfigurationDTO conf, ClientDTO clientDTO, String artifact){
        String recipient = clientDTO.getEmail();
        String msg = "New artifact " + artifact + " has been inserted in configuration " + conf.getName();
        EmailDTO email = new EmailDTO(admin.getEmail(),null,"DAE Project", msg, recipient);
        return email;   
    } 
    
    public EmailDTO sendEmailArtifactRemove(AdministratorDTO admin, ConfigurationDTO conf, ClientDTO clientDTO){
        String recipient = clientDTO.getEmail();
        String msg = "Artifact has been removed from configuration " + conf.getName();
        EmailDTO email = new EmailDTO(admin.getEmail(),null,"DAE Project", msg, recipient);
        return email;   
    } 
    
    public EmailDTO sendEmailParameterCreate(AdministratorDTO admin, ConfigurationDTO conf, ClientDTO clientDTO, String parameter){
        String recipient = clientDTO.getEmail();
        String msg = "New parameter " + parameter + " has been inserted in configuration " + conf.getName();
        EmailDTO email = new EmailDTO(admin.getEmail(),null,"DAE Project", msg, recipient);
        return email;   
    } 
    
    public EmailDTO sendEmailParameterRemove(AdministratorDTO admin, ConfigurationDTO conf, ClientDTO clientDTO){
        String recipient = clientDTO.getEmail();
        String msg = "Parameter has been removed from configuration " + conf.getName();
        EmailDTO email = new EmailDTO(admin.getEmail(),null,"DAE Project", msg, recipient);
        return email;   
    } 
    
    
    
    
}