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
import javax.ejb.EJBException;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.validation.ConstraintViolationException;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;


/**
 *
 * @author Joao Marquez
 */
@Stateless
@Path("/administrators")
public class AdministratorBean extends Bean<Administrator>{

    @PersistenceContext(name="dae_project")//Peristance context usa o nome da bd do persistance.xml
    EntityManager em;
   
    public void create(AdministratorDTO administratorDTO) 
        throws EntityExistsException, MyConstraintViolationException {
        
        try{
            Administrator adminstrator = em.find(Administrator.class, administratorDTO.getUsername());
            if (adminstrator != null) {
                throw new EntityExistsException("A user with that username already exists.");
            }
            
            em.persist(new Administrator(
                    administratorDTO.getUsername(), 
                    "secret",//if we do administratorDTO.getPassword() we get a hashed password;_; not the original
                    administratorDTO.getName(), 
                    administratorDTO.getEmail(), 
                    administratorDTO.getOccupation())
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
            Administrator administrator = em.find(Administrator.class, username);

            if (administrator == null) {
                throw new EntityDoesNotExistException("A user with that username doesnt exists.");
            }
              
            em.remove(administrator);
        }catch (EntityDoesNotExistException e) {
            throw e;
        }catch (ConstraintViolationException e){
            throw new MyConstraintViolationException(Utils.getConstraintViolationMessages(e));
        }catch (Exception e) {
            throw new EJBException(e.getMessage());
        }
    }
    
    @GET
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    @Path("all")
    public Collection<AdministratorDTO> getAll(){
        List<Administrator> administrators = new ArrayList<>();
        administrators = em.createNamedQuery("getAllAdministrators").getResultList();

        return toDTOs(administrators, AdministratorDTO.class);
    }
}
