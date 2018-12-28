/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejbs;

import dtos.AdministratorDTO;
import dtos.ClientDTO;
import dtos.ProductDTO;
import entities.Comment;
import entities.Configuration;
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
    private ProductCatalogBean productCatalogBean;
    
    @EJB//Dà uma instancida do EJB ConfigurationBean
    private ConfigurationBean configurationBean;
    
    @EJB//Dà uma istancida do EJB ProductBean
    private ProductBean productBean;
    
    @EJB
    private ModuleBean moduleBean;
    
    @EJB
    private CommentBean commentBean;
      
    @PostConstruct
    public void populateDB() {
        System.out.println("HEHE");
        try{
            clientBean.create(new ClientDTO("client1", "secret", "Manuel", "dae.ei.ipleiria@gmail.com", "Av. José Maceda", "918 923 232"));
            clientBean.create(new ClientDTO("client2", "secret", "Manuel", "dae.ei.ipleiria@gmail.com", "Av. Alberto Alves", "+00351 256 0033 12"));
            adminstratorBean.create(new AdministratorDTO("administrator1", "secret", "Manuel", "dae.ei.ipleiria@gmail.com", "Director"));
        }catch(Exception ex){
            System.out.println(ex);
        }
            configurationBean.create("Configuration 1","Im just a configuration 1", Configuration.Status.INACTIVE, "V1.5.0", "Contract data 1");
            configurationBean.create("Configuration 2","Im just a configuration 2", Configuration.Status.INACTIVE, "V1.7.8", "Contract data 2");

            try{
                productCatalogBean.create(new ProductDTO(1L, "Big Beng", "We can never be sure if its a Watch of a Explosion","",""), configurationBean.getAll());
                productCatalogBean.create(new ProductDTO(2L, "Wall of China", "This template contain all the procedures in order to sucessfully deploy a wall in a huge scale","",""), configurationBean.getAll());
            }catch (Exception ex){
                System.out.println("FUCK");
                System.out.println(ex);
            }

            //productCatalogBean.addConfiguration(1, 1);
            //productCatalogBean.addConfiguration(1, 2);

            productBean.create(new ProductDTO(1L, "PRODUCT1", "Im just a normal product", "v1.10", "client1"));
            productBean.create(new ProductDTO(2L, "REMIDA", "Website about energy", "v1.40", "client1"));
            productBean.create(new ProductDTO(3L, "JAVA EE Aplication", "Complex project", "v1.40", "client1"));
            //clientBean.addProduct("client1", new Long(1));

            Comment parent = commentBean.create(null, 5,"Initial comment");
            Comment child = commentBean.create(parent.getId(), 5,"Not initial comment");
            parent.addChildren(child);

            moduleBean.create("module 1", "1.0");

            configurationBean.addModule(new Long(2), new Long(5));
        System.out.println("I FINISH");
    }
}
