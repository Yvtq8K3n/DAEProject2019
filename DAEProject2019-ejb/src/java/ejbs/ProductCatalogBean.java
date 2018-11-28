/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejbs;

import entities.Configuration;
import entities.ProductCatalog;
import entities.Template;
import java.util.ArrayList;
import java.util.List;
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
public class ProductCatalogBean{

    @PersistenceContext(name="DAEProject2019")//Peristance context usa o nome da bd do persistance.xml
    EntityManager em;
   
    public void create(String name, String description) {
        ProductCatalog productCatalog = new ProductCatalog(name, description);
        em.persist(productCatalog);
    }
    
    public List<Configuration> getConfigurations(Long id){
        ProductCatalog productCatalog = em.find(ProductCatalog.class, id);
        if (productCatalog == null) System.out.println("ERROR");//Temporary
        
        //return productCatalog.getConfigurations();
        return null;
    }
    
    public void addConfiguration(long id, long idConf){
        ProductCatalog productCatalog = em.find(ProductCatalog.class, id);
        if (productCatalog == null) System.out.println("ERROR INVALID ID!");//Temporary
        
        Configuration configuration = em.find(Configuration.class, idConf);
        if (configuration == null) System.out.println("ERROR INVALID ID_CONFIG!");
        
        //productCatalog.addConfiguration(configuration);
    }
    
    public List<Template> getAll(){
        List<Template> templates = new ArrayList<>();
        templates = em.createNamedQuery("getAllProductCatalog").getResultList();

        return templates;
    }
}
