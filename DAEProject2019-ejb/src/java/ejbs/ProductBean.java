/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejbs;

import dtos.ProductDTO;
import entities.Client;
import entities.Module;
import entities.Product;
import entities.Template;
//import static entities.Template_.description;
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
public class ProductBean extends Bean<Product>{

    @PersistenceContext(name="dae_project")//Peristance context usa o nome da bd do persistance.xml
    EntityManager em;
   
    public void create(ProductDTO productDTO) {
        Client client = em.find(Client.class, productDTO.getClientUsername());
        if(client == null){
            System.out.println("NÂO ENCONTROU CLIENTE ");
            return;
        }
        System.out.println("ENCONTROU CLIENTE: " + client.getName());
        Product product = new Product(productDTO.getName(), productDTO.getDescription(), productDTO.getBaseVersion(), client);
        System.out.println("CRIOU PRODUCT : " + product.getDescription());
        client.addProduct(product);
        em.persist(product);
        em.merge(client);
    }

    public List<ProductDTO> getAll() {
        List<ProductDTO> productDTOs = new ArrayList<>();
        List<Product> products = new ArrayList<>();
        products = em.createNamedQuery("getAllProducts").getResultList();
        for(Product p : products){
            productDTOs.add(new ProductDTO(p.getId(), 
                                            p.getName(), 
                                            p.getDescription(), 
                                            p.getBaseVersion(), 
                                            p.getOwner().getUsername()));
        }
        return productDTOs;
    }
    
    
    public List<ProductDTO> getClientProducts(String username){
        List<ProductDTO> products = new ArrayList<>();
        try {
            Client client = em.find(Client.class, username);
            if (client == null) {
               
            }
            for(Product product : client.getProducts()){
                products.add(new ProductDTO(product.getId(), 
                        product.getName(), 
                        product.getDescription(), 
                        product.getBaseVersion(), 
                        product.getOwner().getUsername()));
            }
            
        } catch (Exception e) {
        }
        return products;
    }
    
    
    
}
