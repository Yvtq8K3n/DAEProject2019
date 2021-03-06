/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejbs;

import dtos.ClientDTO;
import dtos.ConfigurationDTO;
import entities.Client;
import entities.User;
import exceptions.EntityDoesNotExistException;
import exceptions.EntityExistsException;
import exceptions.MyConstraintViolationException;
import exceptions.Utils;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.annotation.security.DeclareRoles;
import javax.annotation.security.RolesAllowed;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.validation.ConstraintViolationException;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.GenericEntity;

/**
 *
 * @author Joao Marquez
 */
@Stateless
@DeclareRoles({"Administrator","Client"})
@Path("/clients")
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
            User user = em.find(User.class, clientDTO.getUsername());
            if (user != null) {
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
    
    @PUT
    @RolesAllowed("Administrator")
    @Consumes(MediaType.APPLICATION_XML)
    @Produces(MediaType.APPLICATION_XML)
    public Response update(ClientDTO clientDTO) {
        try{
  
            if (clientDTO.getUsername()== null)
                throw new EntityDoesNotExistException("Invalid Client");
            Client client = em.find(Client.class, clientDTO.getUsername());
            if(client == null) 
                throw new EntityDoesNotExistException("Client not found.");
            
            client.setName(clientDTO.getName());
            client.setEmail(clientDTO.getEmail());
            client.setContact(clientDTO.getContact());
            client.setAddress(clientDTO.getAddress());
                
            em.persist(client);
        return Response.status(Response.Status.OK).entity("Client was successfully edited.").build();
        }catch (EntityDoesNotExistException e) {
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
    @Path("/{username}/configurations")
    @RolesAllowed({"Administrator","Client"})
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public Response getClientConfigurations(@PathParam("username") String username){
        try{
            if (username == null)
                throw new EntityDoesNotExistException("Invalid Username");
            
            Client client = em.find(Client.class, username);
            if(client == null) 
                throw new EntityDoesNotExistException("Client not found.");

            Collection<ConfigurationDTO> configurationDTO
                    = ConfigurationBean.convertDTOs(client.getConfigurations());
            
            GenericEntity<List<ConfigurationDTO>> entity =
                new GenericEntity<List<ConfigurationDTO>>(new ArrayList<>(configurationDTO)) {};      

            return Response.status(Response.Status.OK).entity(entity).build();
        }catch (EntityDoesNotExistException e) {
            return Response.status(Response.Status.CONFLICT).entity(e.getMessage()).build();
        }catch (ConstraintViolationException e){
            return Response.status(Response.Status.BAD_REQUEST).entity(Utils.getConstraintViolationMessages(e)).build();
        }catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST).entity("An unexpected error has occurred.").build();
        } 
    }
    

    @GET
    @RolesAllowed("Client")
    @Path("/{username}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public Response getClient(@PathParam("username") String username)
    throws EntityDoesNotExistException{
        try {
            if (username == null)
                throw new EntityDoesNotExistException("Invalid Username");
            
            Client client = em.find(Client.class, username);
            if(client == null) 
                throw new EntityDoesNotExistException("Client not found.");
            
            ClientDTO clientDTO = toDTO(client, ClientDTO.class);
            
            GenericEntity<ClientDTO> entity =
                new GenericEntity<ClientDTO>(clientDTO) {}; 
            
            return Response.status(Response.Status.OK).entity(entity).build();
        }catch (EntityDoesNotExistException e) {
            return Response.status(Response.Status.CONFLICT).entity(e.getMessage()).build();
        }catch (ConstraintViolationException e){
            return Response.status(Response.Status.BAD_REQUEST).entity(Utils.getConstraintViolationMessages(e)).build();
        }catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST).entity("An unexpected error has occurred.").build();
        }
    }

    @GET
    @RolesAllowed("Administrator")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public Response getAll(){
        try{
            List<Client> clients = 
                    em.createNamedQuery("getAllClients").getResultList();
            
            Collection<ClientDTO> clientsDTO
                    = toDTOs(clients, ClientDTO.class);
            
            GenericEntity<List<ClientDTO>> entity =
                new GenericEntity<List<ClientDTO>>(new ArrayList<>(clientsDTO)) {};     

            return Response.status(Response.Status.OK).entity(entity).build();
        }catch (ConstraintViolationException e){
            return Response.status(Response.Status.BAD_REQUEST).entity(Utils.getConstraintViolationMessages(e)).build();
        }catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST).entity("An unexpected error has occurred.").build();
        } 
    }
}
