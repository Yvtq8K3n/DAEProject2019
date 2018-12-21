/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejbs;

import dtos.ClientDTO;
import entities.Client;
import entities.Product;
import exceptions.EntityDoesNotExistException;
import exceptions.EntityExistsException;
import exceptions.MyConstraintViolationException;
import exceptions.Utils;
import java.util.Collection;
import java.util.List;
import javax.annotation.security.DeclareRoles;
import javax.annotation.security.PermitAll;
import javax.ejb.EJBException;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.validation.ConstraintViolationException;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.annotation.security.RolesAllowed;

/**
 *
 * @author Joao Marquez
 */
@Stateless
@Path("/clients")
@DeclareRoles({"Administrator", "Client"})
public class ClientBean extends Bean<Client>{

    @PersistenceContext(name="dae_project")//Peristance context usa o nome da bd do persistance.xml
    EntityManager em;
   
    @POST
    @RolesAllowed("Administrator")
    @Consumes(MediaType.APPLICATION_XML)
    @Produces(MediaType.APPLICATION_XML)
    public Response create(ClientDTO clientDTO)
        throws EntityExistsException, MyConstraintViolationException{
        try{
            Client client = em.find(Client.class, clientDTO.getUsername());
            if (client != null) {
                throw new EntityExistsException("Username already taken.");
            }
            em.persist(new Client(
                clientDTO.getUsername(), 
                clientDTO.getPassword(), 
                clientDTO.getName(),
                clientDTO.getEmail(), 
                clientDTO.getAddress(),
                clientDTO.getContact())
            );
            
            return Response.status(Response.Status.CREATED).entity("User was successfully created.").build();
        }catch (EntityExistsException e) {
            return Response.status(Response.Status.CONFLICT).entity(e.getMessage()).build();
        }catch (ConstraintViolationException e){
            return Response.status(Response.Status.BAD_REQUEST).entity(Utils.getConstraintViolationMessages(e)).build();
        }catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST).entity("An unexpected error has occurred.").build();
        }  
    }
    
    @DELETE
    @Path("/{username}")
    @RolesAllowed({"Administrator"})
    @Consumes(MediaType.APPLICATION_XML)
    @Produces(MediaType.APPLICATION_XML)
    public Response remove(@PathParam("username") String username) 
        throws EntityDoesNotExistException, MyConstraintViolationException{
        try{
            Client client = em.find(Client.class, username);
            if (client == null) {
                throw new EntityDoesNotExistException("A user with that username doesnt exists.");
            }
            em.remove(client);
            
           return Response.status(Response.Status.OK).entity("User was successfully deleted.").build();
        }catch (EntityDoesNotExistException e) {
            return Response.status(Response.Status.NOT_FOUND).entity(e.getMessage()).build();
        }catch (ConstraintViolationException e){
            return Response.status(Response.Status.BAD_REQUEST).entity(Utils.getConstraintViolationMessages(e)).build();
        }catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST).entity("An unexpected error has occurred.").build();
        }
    }
    
    @GET
    @RolesAllowed("Administrator")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public Collection<ClientDTO> getAll(){
        List<Client> clients =
            em.createNamedQuery("getAllClients").getResultList();

        return toDTOs(clients, ClientDTO.class);
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

    public void addProduct(String username, Long productId) {
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
