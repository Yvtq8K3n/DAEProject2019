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
import dtos.EmailDTO;
import ejbs.EmailBean;
import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.mail.MessagingException;


@ManagedBean
@SessionScoped
public class EmailManager implements Serializable{
    
    ///////////////////////////////////////////////////////////////
    //This are just examples
    ///////////////////////////////////////////////////////////////
    /* 
    public EmailDTO removeProposta(AdministratorDTO admin, 
           PropostaDTO proposta, List<String> recipients) throws MessagingException{
        String msg = "<strong>A proposta:</strong> "+proposta.getTitulo()
                    +".<br><br><strong>Com descricao:</strong> "+ proposta.getResumo()+"."
                    +".<br><br><strong>Foi removida por:</strong> "+ admin.getName()+".";
        
        EmailDTO email = new EmailDTO(admin.getEmail(), "Remoção da Proposta "+proposta.getTitulo(), msg, recipients);
        
        return email;   
    }   */     
    
    /* 
    public EmailDTO updateProva(AdministratorDTO admin,
            PropostaDTO proposta, List<String> alterations, List<String> recipients) throws MessagingException{
        String msg = "<strong>A prova:</strong> "+proposta.getTitulo()
                    +".<br><br><strong>Com descricao:</strong> "+ proposta.getResumo()+"."
                    +".<br><br><strong>Foi atualizada por:</strong> "+ admin.getName()+".";
                            
                    if (alterations!=null){
                        msg+="<br><br><strong>Com as seguintes alteracoes:</strong>";
                        for(String alteration: alterations){
                            msg+="<br>"+alteration;
                        }
                    } 
        
        EmailDTO email = new EmailDTO(admin.getEmail(), "Alteração da Prova "+proposta.getTitulo(), msg, recipients);
        
        return email;
    } */
}