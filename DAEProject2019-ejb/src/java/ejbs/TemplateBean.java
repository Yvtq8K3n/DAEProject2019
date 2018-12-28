/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejbs;

import dtos.ConfigurationDTO;
import dtos.TemplateDTO;
import entities.Module;
import entities.Template;
import exceptions.EntityDoesNotExistException;
import exceptions.EntityExistsException;
import exceptions.MyConstraintViolationException;
import exceptions.Utils;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.List;
import javax.annotation.security.RolesAllowed;
import javax.ejb.EJBException;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.validation.ConstraintViolationException;
import javax.ws.rs.Consumes;
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
@Path("/catalog")
public class TemplateBean extends Bean<Template>{

    @PersistenceContext(name="dae_project")//Peristance context usa o nome da bd do persistance.xml
    EntityManager em;
    
    @POST
    @RolesAllowed("Administrator")
    @Consumes(MediaType.APPLICATION_XML)
    @Produces(MediaType.APPLICATION_XML)
    public Response create(TemplateDTO templateDTO) 
            throws EntityExistsException, MyConstraintViolationException{
        
        try{
            Template cat = new Template(
                templateDTO.getName(), 
                templateDTO.getDescription()
            );
            
            em.persist(cat);   
            return Response.status(Response.Status.CREATED).entity("Template was successfully created.").build();
        }catch (ConstraintViolationException e){
            return Response.status(Response.Status.BAD_REQUEST).entity(Utils.getConstraintViolationMessages(e)).build();
        }catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST).entity("An unexpected error has occurred.").build();
        }  
    }
    
    @POST
    @Path("/{id}")
    @RolesAllowed("Administrator")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_XML)
    public Response addModule(@PathParam("id") Long id, List<Long> modules) 
            throws EntityDoesNotExistException, MyConstraintViolationException, IOException{
        try{
            if (id == null) throw new EntityDoesNotExistException("Template doesn't exists.");
            Template template = em.find(Template.class, id);
            if (template == null) {
                throw new EntityDoesNotExistException("Template doesn't exists.");
            }
            
            for (Long moduleId : modules){
                addModule(id, moduleId);
            }
            
            return Response.status(Response.Status.CREATED).entity("Configurations were successfully associated whit the Template.").build();
        }catch (EntityDoesNotExistException e) {
            return Response.status(Response.Status.NOT_FOUND).entity(e.getMessage()).build();
        }catch (ConstraintViolationException e){
            return Response.status(Response.Status.BAD_REQUEST).entity(Utils.getConstraintViolationMessages(e)).build();
        }catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST).entity("An unexpected error has occurred.").build();
        }
    }
       
    public void remove(Long id) 
            throws EntityDoesNotExistException, MyConstraintViolationException{
        try{
            Template template = em.find(Template.class, id);

            if (template == null) {
                throw new EntityDoesNotExistException("A template with that id doesnt exists.");
            }
              
            em.remove(template);
        }catch (EntityDoesNotExistException e) {
            throw e;
        }catch (ConstraintViolationException e){
            throw new MyConstraintViolationException(Utils.getConstraintViolationMessages(e));
        }catch (Exception e) {
            throw new EJBException(e.getMessage());
        }
    }
    
    private void addModule(Long id, Long idModule) throws EntityDoesNotExistException{
        if(id == null) throw new EntityDoesNotExistException("Template was not found");
        Template template = em.find(Template.class, id);
        if (template == null) throw new EntityDoesNotExistException("Template was not found");
        
        if(idModule == null) throw new EntityDoesNotExistException("Module was not found");
        Module module = em.find(Module.class, idModule);
        if (template == null) throw new EntityDoesNotExistException("Module was not found");
        
        template.addModule(module);
        em.persist(template);
    }
    
    
    /*public List<Configuration> getConfigurations(Long id){
        Template productCatalog = em.find(Template.class, id);
        if (productCatalog == null) System.out.println("ERROR");//Temporary
        
        List<Configuration> configurations = productCatalog.getConfigurations();
        if (configurations == null) System.out.println("ERROR");//Temporary
        return productCatalog.getConfigurations();
    }*/
    
    
    
    public Collection<ConfigurationDTO> getAll(){
        List<Template> templates = 
            em.createNamedQuery("getAllTemplates").getResultList();
        
        return toDTOs(templates, ConfigurationDTO.class);
    }
}
