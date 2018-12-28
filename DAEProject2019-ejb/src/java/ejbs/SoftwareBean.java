/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejbs;

import dtos.ConfigurationDTO;
import entities.Client;
import entities.Configuration;
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
public class SoftwareBean extends Bean<Configuration>{

    @PersistenceContext(name="dae_project")//Peristance context usa o nome da bd do persistance.xml
    EntityManager em;
   
    /*public void create(ConfigurationDTO productDTO) {
        Client client = em.find(Client.class, productDTO.getClientUsername());
        if(client == null){
            System.out.println("NÂO ENCONTROU CLIENTE ");
            return;
        }
        System.out.println("ENCONTROU CLIENTE: " + client.getName());
        Configuration product = new Configuration(productDTO.getName(), productDTO.getDescription(), productDTO.getBaseVersion(), client);
        System.out.println("CRIOU PRODUCT : " + product.getDescription());
        client.addProduct(product);
        em.persist(product);
        em.merge(client);
    }

    public List<Configuration> getAll() {
        List<Configuration> products = new ArrayList<>();
        products = em.createNamedQuery("getAllProducts").getResultList();
        return products;
    }
    
    
    public List<Configuration> getClientProducts(String username){
        List<Configuration> products = new ArrayList<>();
        try {
            Client client = em.find(Client.class, username);
            if (client == null) {
               
            }
            for(Configuration product : client.getProducts()){
                products.add(product);
            }
            
        } catch (Exception e) {
        }
        return products;
    }*/
    
    
}
