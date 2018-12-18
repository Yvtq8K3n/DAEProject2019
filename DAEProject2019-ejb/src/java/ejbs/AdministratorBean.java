/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejbs;

import dtos.AdministratorDTO;
import entities.Administrator;
import exceptions.EntityDoesNotExistException;
import exceptions.EntityExistsException;
import exceptions.MyConstraintViolationException;
import exceptions.Utils;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.annotation.security.RolesAllowed;
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


/**
 *
 * @author Joao Marquez
 */
@Stateless
@Path("/administrators")
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
            Administrator adminstrator = em.find(Administrator.class, administratorDTO.getUsername());
            if (adminstrator != null) {
                throw new EntityExistsException("A administrator with that username already exists.");
            }
            
            em.persist(new Administrator(
                    administratorDTO.getUsername(), 
                    administratorDTO.getPassword(),
                    administratorDTO.getName(), 
                    administratorDTO.getEmail(), 
                    administratorDTO.getOccupation())
            );
            
            return Response.ok().build();
        }catch (EntityExistsException e) {
            throw e;
        }catch (ConstraintViolationException e){
            throw new MyConstraintViolationException(Utils.getConstraintViolationMessages(e));
        }catch (Exception e) {
            return Response.status(400).entity("A problem has occurred while attempting to create an administrator.").build();
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
            
            return Response.ok().build();
        }catch (EntityDoesNotExistException e) {
            throw e;
        }catch (ConstraintViolationException e){
            throw new MyConstraintViolationException(Utils.getConstraintViolationMessages(e));
        }catch (Exception e) {
            return Response.status(400).entity("A problem has occurred while attempting to remove an administrator.").build();
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
