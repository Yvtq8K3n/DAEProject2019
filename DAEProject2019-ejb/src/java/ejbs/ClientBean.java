/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejbs;

import entities.Administrator;
import entities.Client;
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
}
