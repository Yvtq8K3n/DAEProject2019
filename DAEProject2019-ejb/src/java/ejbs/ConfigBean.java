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
    private AdminstratorBean adminstratorBean;
    
    @EJB//Dà uma instancida do EJB TemplateBean
    private TemplateBean templateBean;
    
    @EJB//Dà uma istancida do EJB ProductBean
    private ProductBean productBean;
      
    @PostConstruct//Excecuta assim que o bean é instanciado
    public void populateDB() {
        
        clientBean.create("1111111", "Manuel", "Manuel", "dae.ei.ipleiria@gmail.com", "Av. José Maceda", "918 923 232");
        clientBean.create("1111111", "Manuel", "Manuel", "dae.ei.ipleiria@gmail.com", "Av. Alberto Alves", "+00351 256 0033 12");
        adminstratorBean.create("1111111", "Manuel", "Manuel", "dae.ei.ipleiria@gmail.com", "Director");
        
        templateBean.create("Template1", "Im just a template");
        
        productBean.create("PRODUCT1", "Im just a normal product", "v1.10");
    }
}
