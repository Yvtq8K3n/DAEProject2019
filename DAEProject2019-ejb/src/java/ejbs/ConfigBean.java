/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejbs;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Singleton;
import javax.ejb.Startup;

/**
 *
 * @author Joao Marquez
 */
@Singleton
@Startup//Assim que faz depply e corre a aplicação o component é instanciado
public class ConfigBean {
    
    @EJB//Dà uma instancida do EJB StudentBean
    private ClientBean clientBean;
    
    @EJB//Dà uma istancida do EJB AdminstratorBean
    private AdministratorBean adminstratorBean;
    
    @EJB//Dà uma instancida do EJB TemplateBean
    private TemplateBean templateBean;
    
    @EJB//Dà uma instancida do EJB ConfigurationBean
    private ConfigurationBean configurationBean;
    
    @EJB//Dà uma istancida do EJB ProductBean
    private ProductBean productBean;
      
    @PostConstruct//Excecuta assim que o bean é instanciado
    public void populateDB() {  
        clientBean.create("client1", "secret", "Manuel", "dae.ei.ipleiria@gmail.com", "Av. José Maceda", "918 923 232");
        clientBean.create("client2", "secret", "Manuel", "dae.ei.ipleiria@gmail.com", "Av. Alberto Alves", "+00351 256 0033 12");
        adminstratorBean.create("administrator1", "secret", "Manuel", "dae.ei.ipleiria@gmail.com", "Director");
        
        templateBean.create("Big Beng", "We can't never be sure if its a Watch of a Explosion");
        templateBean.create("Wall of China", "This template contain all the procedures in order to sucessfully deploy a wall in a huge scale");
        configurationBean.create("Im just a configuration 1");
        configurationBean.create("Im just a configuration 2");
        
        templateBean.addConfiguration(1, 1);
        templateBean.addConfiguration(1, 2);
        
        productBean.create("PRODUCT1", "Im just a normal product", "v1.10");
    }
}
