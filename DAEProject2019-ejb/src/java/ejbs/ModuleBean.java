/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejbs;

import dtos.ModuleDTO;
import entities.Module;
import entities.Template;
import exceptions.Utils;
import javax.ejb.Stateless;
import javax.ejb.LocalBean;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.validation.ConstraintViolationException;
import javax.ws.rs.core.Response;

/**
 *
 * @author admin
 */
@Stateless
@LocalBean
public class ModuleBean{
   
   @PersistenceContext(name="dae_project")
   EntityManager em;
   
   public Response create(ModuleDTO moduleDTO){
        try{
            Module module = new Module(
                moduleDTO.getName(), 
                moduleDTO.getVersion()
            );
            
            em.persist(module);   
            return Response.status(Response.Status.CREATED).entity("Module was successfully created.").build();
        }catch (ConstraintViolationException e){
            return Response.status(Response.Status.BAD_REQUEST).entity(Utils.getConstraintViolationMessages(e)).build();
        }catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST).entity("An unexpected error has occurred.").build();
        }
   }
}
