/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejbs;

import dtos.ProductDTO;
import dtos.TemplateDTO;
import entities.Client;
import entities.Configuration;
import entities.Product;
import entities.ProductCatalog;
import entities.Template;
import exceptions.EntityDoesNotExistException;
import exceptions.EntityExistsException;
import exceptions.MyConstraintViolationException;
import exceptions.Utils;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.ejb.EJBException;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.validation.ConstraintViolationException;


/**
 *
 * @author Joao Marquez
 */
@Stateless //Distinge que é um ejb (componente que não gere instancia nem ciclo de vida)
//Faz pedidos mas não guardam de quem esta a fazer
//Faz com que ´não tenha de ter uma instancia para cada utilizador
public class ProductCatalogBean extends Bean<ProductCatalog>{

    @PersistenceContext(name="dae_project")//Peristance context usa o nome da bd do persistance.xml
    EntityManager em;
   
    public void create(ProductDTO product, List<Configuration> configurationsDTO) 
            throws EntityDoesNotExistException, MyConstraintViolationException{
        
        try{
            ProductCatalog cat = new ProductCatalog(
                product.getName(), 
                product.getDescription()
            );
            em.persist(cat);
            em.flush();//Reconnect bd whit object giving it id
            for (Configuration cDTO : configurationsDTO){
                Configuration conf = em.find(Configuration.class, cDTO.getId());
                if (conf == null) {
                    throw new EntityDoesNotExistException("A configuration whit that id is Invalid Configuration");
                }
                addConfiguration(cat.getId(), conf.getId());
            }
        }catch (EntityDoesNotExistException e) {
            throw e;
        }catch (ConstraintViolationException e){
            throw new MyConstraintViolationException(Utils.getConstraintViolationMessages(e));
        }catch (Exception e) {
            throw new EJBException(e.getMessage());
        }  
    }
       
    public void remove(Long id) 
            throws EntityDoesNotExistException, MyConstraintViolationException{
        try{
            ProductCatalog template = em.find(ProductCatalog.class, id);

            if (template == null) {
                throw new EntityDoesNotExistException("A template with that id doesnt exists.");
            }
              
            em.remove(template);
        }catch (EntityDoesNotExistException e) {
            throw e;
        }catch (ConstraintViolationException e){
            throw new MyConstraintViolationException(Utils.getConstraintViolationMessages(e));
        }catch (Exception e) {
            throw new EJBException(e.getMessage());
        }
    }
    
    public List<Configuration> getConfigurations(Long id){
        ProductCatalog productCatalog = em.find(ProductCatalog.class, id);
        if (productCatalog == null) System.out.println("ERROR");//Temporary
        
        List<Configuration> configurations = productCatalog.getConfigurations();
        if (configurations == null) System.out.println("ERROR");//Temporary
        return productCatalog.getConfigurations();
    }
    
    public void addConfiguration(long id, long idConf){
        ProductCatalog productCatalog = em.find(ProductCatalog.class, id);
        if (productCatalog == null) System.out.println("ERROR INVALID ID!");//Temporary
        
        Configuration configuration = em.find(Configuration.class, idConf);
        if (configuration == null) System.out.println("ERROR INVALID ID_CONFIG!");
        
        productCatalog.addConfiguration(configuration);
    }
    
    public Collection<ProductDTO> getAll(){
        List<Product> templates = new ArrayList<>();
        templates = em.createNamedQuery("getAllProductCatalog").getResultList();
        
        return toDTOs(templates, ProductDTO.class);
    }
}
