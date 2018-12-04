/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejbs;

import entities.Administrator;
import entities.Client;
import entities.Product;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJBException;
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
public class ClientBean{

    @PersistenceContext(name="DAEProject2019")//Peristance context usa o nome da bd do persistance.xml
    EntityManager em;
   
    public void create(String username, String password, String name, String email, String address, String contact) {
        Client client = new Client(username, password, name, email, address, contact);
        em.persist(client);
    }
    
    public List<Client> getAll(){
        List<Client> clients = new ArrayList<>();
        clients = em.createNamedQuery("getAllClients").getResultList();

        return clients;
    }
    
    public Client getClient(String username){
        try {
            Client client = em.find(Client.class, username);
            if (client == null) {
                throw new EJBException();
            }
            
            return client;
        } catch (EJBException e) {
            throw new EJBException("ERROR: CANT FIND ENTETY" + e.getMessage());
        }
    }

    void addProduct(String username, Long productId) {
        try {
            Client client = em.find(Client.class, username);
            if (client == null) {
                System.out.println("ERROR: can't find client in addProduct of clientBean");
            }
            Product product = em.find(Product.class, productId);
            if (product == null) {
                System.out.println("ERROR: can't find product in addProduct of clientBean");
            }
            
            client.addProduct(product);
            em.merge(client);
            
        } catch (EJBException e) {
            System.out.println("ERROR: addProduct in CLIENT_BEAN" + e.getMessage());
        }
    }
}
