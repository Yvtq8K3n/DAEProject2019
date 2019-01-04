/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejbs;

import dtos.AdministratorDTO;
import entities.Administrator;
import entities.User;
import exceptions.EntityDoesNotExistException;
import exceptions.EntityExistsException;
import exceptions.MyConstraintViolationException;
import exceptions.Utils;
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
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;


/**
 *
 * @author Joao Marquez
 */
@Stateless
@Path("/administrators")
@DeclareRoles({"Administrator", "Client"})
public class AdministratorBean extends Bean<Administrator>{

    @PersistenceContext(name="dae_project")//Peristance context usa o nome da bd do persistance.xml
    EntityManager em;
   
    @POST
    @RolesAllowed("Administrator")
    @Consumes(MediaType.APPLICATION_XML)
    @Produces(MediaType.APPLICATION_XML)
    public Response create(AdministratorDTO administratorDTO) 
        throws EntityExistsException, MyConstraintViolationException {
        
        try{
            User user = em.find(User.class, administratorDTO.getUsername());
            if (user != null) {
                throw new EntityExistsException("Username already taken.");
            }
            
            em.persist(new Administrator(
                    administratorDTO.getUsername(), 
                    administratorDTO.getPassword(),
                    administratorDTO.getName(), 
                    administratorDTO.getEmail(), 
                    administratorDTO.getOccupation())
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
    public Response update(AdministratorDTO administratorDTO) {
        try{
  
            if (administratorDTO.getUsername()== null)
                throw new EntityDoesNotExistException("Invalid Administrator");
            Administrator administrator = em.find(Administrator.class, administratorDTO.getUsername());
            if(administrator == null) 
                throw new EntityDoesNotExistException("Administrator not found.");
            
            administrator.setName(administratorDTO.getName());
            administrator.setEmail(administratorDTO.getEmail());
            administrator.setOccupation(administratorDTO.getOccupation());
                
            em.persist(administrator);
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
    @RolesAllowed("Administrator")
    @Consumes(MediaType.APPLICATION_XML)
    @Produces(MediaType.APPLICATION_XML)
    public Response remove(@PathParam("username") String username) 
        throws EntityDoesNotExistException, MyConstraintViolationException{
        try{
            Administrator administrator = em.find(Administrator.class, username);
            if (administrator == null) {
                throw new EntityDoesNotExistException("A user with that username doesnt exists.");
            }
            em.remove(administrator);
            
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
    @Path("administrator/{username}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public Response getClient(@PathParam("username") String username)
    throws EntityDoesNotExistException{
        try {
            if (username == null)
                throw new EntityDoesNotExistException("Invalid Username");
            
            Administrator administrator = em.find(Administrator.class, username);
            if(administrator == null) 
                throw new EntityDoesNotExistException("Administrator not found.");
            
            AdministratorDTO administratorDTO = toDTO(administrator, AdministratorDTO.class);
            
            GenericEntity<AdministratorDTO> entity =
                new GenericEntity<AdministratorDTO>(administratorDTO) {}; 
            
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
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public Collection<AdministratorDTO> getAll(){
        List<Administrator> administrators =
            em.createNamedQuery("getAllAdministrators").getResultList();

        return toDTOs(administrators, AdministratorDTO.class);
    }
}
