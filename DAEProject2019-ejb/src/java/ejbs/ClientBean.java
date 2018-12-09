/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejbs;

import entities.Client;
import entities.Product;
import exceptions.EntityDoesNotExistException;
import exceptions.EntityExistsException;
import exceptions.MyConstraintViolationException;
import exceptions.Utils;
import java.util.ArrayList;
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
public class ClientBean{

    @PersistenceContext(name="DAEProject2019")//Peristance context usa o nome da bd do persistance.xml
    EntityManager em;
   
    public void create(Client clientDTO)
    throws EntityExistsException, MyConstraintViolationException{
        
        try{
            Client client = em.find(Client.class, clientDTO.getUsername());
            if (client != null) {
                throw new EntityExistsException("A user with that username already exists.");
            }
            
            em.persist(new Client(
                clientDTO.getUsername(), 
                clientDTO.getPassword(), 
                clientDTO.getName(), 
                clientDTO.getEmail(), 
                clientDTO.getAddress(),
                clientDTO.getContact())
            );
        }catch (EntityExistsException e) {
            throw e;
        }catch (ConstraintViolationException e){
            throw new MyConstraintViolationException(Utils.getConstraintViolationMessages(e));
        }catch (Exception e) {
            throw new EJBException(e.getMessage());
        }  
    }
    
    
    public void remove(String username) throws EntityDoesNotExistException, MyConstraintViolationException{
        try{
            Client client = em.find(Client.class, username);
            if (client == null) {
                throw new EntityDoesNotExistException("A user with that username doesnt exists.");
            }
              
            em.remove(client);
        }catch (EntityDoesNotExistException e) {
            throw e;
        }catch (ConstraintViolationException e){
            throw new MyConstraintViolationException(Utils.getConstraintViolationMessages(e));
        }catch (Exception e) {
            throw new EJBException(e.getMessage());
        }
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
