/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejbs;

import dtos.AdministratorDTO;
import dtos.ClientDTO;
import dtos.CommentDTO;
import dtos.ConfigurationDTO;
import dtos.ModuleDTO;
import dtos.TemplateDTO;
import entities.Comment;
import entities.Configuration;
import java.util.ArrayList;
import java.util.Arrays;
import javax.annotation.PostConstruct;
import javax.annotation.security.RunAs;
import javax.ejb.EJB;
import javax.ejb.Singleton;
import javax.ejb.Startup;
/**
 *
 * @author Joao Marquez
 */
@RunAs("Admin")
@Singleton
@Startup 
public class ConfigBean {

    @EJB//Dà uma instancida do EJB ClientBean
    private ClientBean clientBean;
    
    @EJB//Dà uma istancida do EJB AdminstratorBean
    private AdministratorBean adminstratorBean;
    
    @EJB//Dà uma instancida do EJB TemplateBean
    private TemplateBean templateBean;
    
    @EJB//Dà uma instancida do EJB ConfigurationBean
    private ConfigurationBean configurationBean;
    
    @EJB//Dà uma istancida do EJB ProductBean
    private SoftwareBean productBean;
    
    @EJB
    private ModuleBean moduleBean;
    
    @EJB
    private CommentBean commentBean;
      
    @PostConstruct
    public void populateDB() {
        System.out.println("HEHE");
        try{
            //Create Users
            clientBean.create(new ClientDTO("client1", "secret", "Manuel", "dae.ei.ipleiria@gmail.com", "Av. José Maceda", "918 923 232"));
            clientBean.create(new ClientDTO("client2", "secret", "Teresa", "dae.ei.ipleiria@gmail.com", "Av. Alberto Alves", "+00351 256 0033 12"));
            adminstratorBean.create(new AdministratorDTO("administrator1", "secret", "Manuel", "dae.ei.ipleiria@gmail.com", "Director"));
            adminstratorBean.create(new AdministratorDTO("administrator2", "secret", "João Marques", "dae.ei.ipleiria@gmail.com", "Vendedor"));
            
            //Create Configurations
            configurationBean.create(new ConfigurationDTO(null,"Office 2017","Service for life", "V1.5.0", "client1",ConfigurationDTO.Status.ACTIVE,"16/12/2018"));
            configurationBean.create(new ConfigurationDTO(null,"Karsperkey","Securing your information", "V1.7.8", "client1",ConfigurationDTO.Status.ACTIVE,"15/12/2012"));
            configurationBean.create(new ConfigurationDTO(null,"CMTV","The wost tv news ever created!", "V1.5.0", "client1",ConfigurationDTO.Status.ACTIVE,"16/12/2041"));
            configurationBean.create(new ConfigurationDTO(null,"Pokemon","Hey! Someone got catche them all, lets hope is not you", "V1.7.8", "client1",ConfigurationDTO.Status.ACTIVE,"16/3/2139"));
            
            //Create Templates
            templateBean.create(new TemplateDTO(1L, "Office 2017 Professional Edition", "A combination of escencial tools that provide, the best of personal computers"));
            templateBean.create(new TemplateDTO(2L, "Wall of China", "This template contain all the procedures in order to sucessfully deploy a wall in a huge scale"));
            templateBean.create(new TemplateDTO(3L, "Under the Sea!", "A jorney under the world wide sea! Kind of big, thought may be a long long jorney."));
            templateBean.create(new TemplateDTO(4L, "Cry your Heart Out!", "Life isnt always great, this pack will make u feel better, at least thats what we hope."));
        }catch (Exception ex){
            System.out.println(ex);
        }

        try{
            //Create module for Configuration:Office 2017
            moduleBean.create(new ModuleDTO(null,"Word 2017", "21.40"));
            moduleBean.create(new ModuleDTO(null,"PowerPoint 2017", "32.1"));
            moduleBean.create(new ModuleDTO(null,"Excel 2017", "11.30"));
            configurationBean.addModule(1L, 9L);
            configurationBean.addModule(1L, 10L);
            configurationBean.addModule(1L, 11L);
            
            //Create module for Configuration:Pokemon
            moduleBean.create(new ModuleDTO(null,"Charizard", "21.40"));
            moduleBean.create(new ModuleDTO(null,"Pikachu", "32.1"));
            moduleBean.create(new ModuleDTO(null,"Onix", "21.40"));
            moduleBean.create(new ModuleDTO(null,"Salamance", "32.1"));
            configurationBean.addModule(2L, 12L);
            configurationBean.addModule(2L, 13L);
            configurationBean.addModule(2L, 14L);
            configurationBean.addModule(2L, 15L);
            
            //Create module for Template:Office 2017
            moduleBean.create(new ModuleDTO(null,"Word 2017", "21.40"));
            moduleBean.create(new ModuleDTO(null,"PowerPoint 2017", "32.1"));
            moduleBean.create(new ModuleDTO(null,"Excel 2017", "11.30"));
            moduleBean.create(new ModuleDTO(null,"OneNote 2017", "32.22"));
            moduleBean.create(new ModuleDTO(null,"Publisher 2017", "45.34"));
            templateBean.addModule(5L, 16L);
            templateBean.addModule(5L, 17L);
            templateBean.addModule(5L, 18L);
            templateBean.addModule(5L, 19L);
            templateBean.addModule(5L, 20L);
        }catch (Exception ex){
            System.out.println(ex);
        }
        
        try{           
            commentBean.create(new CommentDTO(null, null, null, 1L, "Im Your Granpha!","client1"));//granpha
            commentBean.create(new CommentDTO(null, 21L, null, 1L, "Im Your True DAD!", "client1"));//dad
            commentBean.create(new CommentDTO(null, 22L, null, 1L, "Im Child1!","client1"));//child1
            commentBean.create(new CommentDTO(null, 22L, null, 1L, "Im Child2!","client1"));//child2
            commentBean.create(new CommentDTO(null, 21L, null, 1L, "I Ain't Your DAD!", "client1"));//dad
        }catch (Exception ex){
            System.out.println(ex);
        }
        
        //productBean.create(new ConfigurationDTO(1L, "PRODUCT1", "Im just a normal product", "v1.10", "client1"));
        //productBean.create(new ConfigurationDTO(2L, "REMIDA", "Website about energy", "v1.40", "client1"));
        //productBean.create(new ConfigurationDTO(3L, "JAVA EE Aplication", "Complex project", "v1.40", "client1"));
        //clientBean.addProduct("client1", new Long(1));

        /*Comment parent = commentBean.create(null, 1,"Initial comment");
        Comment child = commentBean.create(parent.getId(), 1,"Not initial comment");
        parent.addChildren(child);

        moduleBean.create("module 1", "1.0");

        configurationBean.addModule(new Long(2), new Long(5));
        System.out.println("I FINISH");*/
    }
}
