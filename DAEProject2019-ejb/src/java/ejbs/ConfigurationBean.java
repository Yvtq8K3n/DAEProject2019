/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejbs;

import entities.Configuration;
import entities.Module;
import entities.Template;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;


/**
 *
 * @author Joao Marquez
 */
@Stateless //Distinge que é um ejb (componente que não gere instancia nem ciclo de vida)
//Faz pedidos mas não guardam de quem esta a fazer
//Faz com que ´não tenha de ter uma instancia para cada utilizador
public class ConfigurationBean{

    @PersistenceContext(name="DAEProject2019")//Peristance context usa o nome da bd do persistance.xml
    EntityManager em;
   
    public void create(String description, Configuration.Status status, String baseVersion, String contractData) {
        Configuration configuration = new Configuration(description, status, baseVersion, contractData);
        System.out.println("CONFIGURATION ID: " + configuration.getId()); 
        em.persist(configuration);
    }
    
    public void addModule(Long configurationId, Long moduleId){
        try {
            Configuration configuration = em.find(Configuration.class, configurationId);
            if (configuration == null) {
                System.out.println("ERRO NO METODO ADD_MODULE DE CONFIG_BEAN - NÃO ENCONTROU CONFIGURATION");
            }else{
                System.out.println("ENCONTROU CONFIGURATION " + configuration.getDescription());
            }
            Module module = em.find(Module.class, moduleId);
            if (module == null) {
                System.out.println("ERRO NO METODO ADD_MODULE DE CONFIG_BEAN - NÃO ENCONTROU MODULE");   
            }else{
                System.out.println("ENCONTROU MODULE " + module.getName());
            }
            
            
            configuration.addModule(module);
            em.merge(configuration);
            
        } catch (Exception e) {
            System.out.println("ERRO NO METODO ADD_MODULE DE CONFIG_BEAN " + e.getMessage());
        }
    }
}
