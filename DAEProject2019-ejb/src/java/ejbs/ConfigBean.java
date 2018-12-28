/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejbs;

import dtos.AdministratorDTO;
import dtos.ClientDTO;
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
            clientBean.create(new ClientDTO("client2", "secret", "Manuel", "dae.ei.ipleiria@gmail.com", "Av. Alberto Alves", "+00351 256 0033 12"));
            adminstratorBean.create(new AdministratorDTO("administrator1", "secret", "Manuel", "dae.ei.ipleiria@gmail.com", "Director"));
            
            //Create Configurations
            configurationBean.create(new ConfigurationDTO(null,"Configuration 1","Im just a configuration 1", "V1.5.0", "client1","Active","Contract data 1"));
            configurationBean.create(new ConfigurationDTO(null,"Configuration 2","Im just a configuration 2", "V1.7.8", "client1","Active","Contract data 2"));
            
            //Create Templates
            templateBean.create(new TemplateDTO(1L, "Office 2017 Professional Edition", "A combination of escencial tools that provide, the best of personal computers"));
            templateBean.create(new TemplateDTO(2L, "Wall of China", "This template contain all the procedures in order to sucessfully deploy a wall in a huge scale"));
        }catch (Exception ex){
            System.out.println(ex);
        }

        try{
            moduleBean.create(new ModuleDTO(null,"Word 2017", "21.40"));
            moduleBean.create(new ModuleDTO(null,"PowerPoint 2017", "32.1"));
            moduleBean.create(new ModuleDTO(null,"Excel 2017", "11.30"));
            moduleBean.create(new ModuleDTO(null,"OneNote 2017", "32.22"));
            moduleBean.create(new ModuleDTO(null,"Publisher 2017", "45.34"));
        }catch (Exception ex){
            System.out.println(ex);
        }
        
        try{
            ArrayList<Long> modules = new ArrayList<>();
            modules.add(7L);
            modules.add(9L);
            modules.add(8L);
            modules.add(5L);
            modules.add(6L);
            templateBean.addModule(3L, modules);
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
