/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejbs;

import dtos.ArtifactDTO;
import dtos.ConfigurationDTO;
import dtos.ModuleDTO;
import dtos.TemplateDTO;
import entities.Artifact;
import entities.Client;
import entities.Configuration;
import entities.Module;
import entities.Template;
import exceptions.EntityDoesNotExistException;
import exceptions.EntityExistsException;
import exceptions.MyConstraintViolationException;
import exceptions.Utils;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.annotation.security.PermitAll;
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
@Path("/templates")
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
            return Response.status(Response.Status.CREATED).entity(String.valueOf(cat.getId())).build();
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
    public Response update(TemplateDTO templateDTO) {
        try{
            if (templateDTO.getId() == null)
                throw new EntityDoesNotExistException("Invalid template");
            
            Template template = em.find(Template.class, templateDTO.getId());
            if(template == null) 
                throw new EntityDoesNotExistException("Template not found.");
            
            template.setName(templateDTO.getName());
            template.setDescription(templateDTO.getDescription());
      
            em.persist(template);
        return Response.status(Response.Status.OK).entity("Configuration was successfully edited.").build();
        }catch (EntityDoesNotExistException e) {
            return Response.status(Response.Status.CONFLICT).entity(e.getMessage()).build();
        }catch (ConstraintViolationException e){
            return Response.status(Response.Status.BAD_REQUEST).entity(Utils.getConstraintViolationMessages(e)).build();
        }catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST).entity("An unexpected error has occurred.").build();
        } 
    }
    
    @DELETE
    @Path("/{id}")
    @RolesAllowed({"Administrator"})
    @Consumes(MediaType.APPLICATION_XML)
    @Produces(MediaType.APPLICATION_XML)
    public Response remove(@PathParam("id") Long id) 
        throws EntityDoesNotExistException, MyConstraintViolationException{
        try{
            if (id == null) 
                throw new EntityDoesNotExistException("Invalid Template");
            Template template = em.find(Template.class, id);
            if(template == null) 
                throw new EntityDoesNotExistException("Template not found.");
            
            em.remove(template); 
           return Response.status(Response.Status.OK).entity("Template was successfully deleted.").build();
        }catch (EntityDoesNotExistException e) {
            return Response.status(Response.Status.NOT_FOUND).entity(e.getMessage()).build();
        }catch (ConstraintViolationException e){
            return Response.status(Response.Status.BAD_REQUEST).entity(Utils.getConstraintViolationMessages(e)).build();
        }catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST).entity("An unexpected error has occurred.").build();
        }
    }   
    
    //Artifacts
    @GET
    @Path("/{templateId}/artifacts")
    @PermitAll
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public Response getArtifacts(@PathParam("templateId") Long templateId){
        try{
            if (templateId == null)
                throw new EntityDoesNotExistException("Invalid template");
            
            Template template = em.find(Template.class, templateId);
            if(template == null) 
                throw new EntityDoesNotExistException("Template not found.");

            Collection<ArtifactDTO> artifactsDTO
                    = toDTOs(template.getArtifacts(), ArtifactDTO.class);
            GenericEntity<List<ArtifactDTO>> entity =
                new GenericEntity<List<ArtifactDTO>>(new ArrayList<>(artifactsDTO)) {};      

            return Response.status(Response.Status.OK).entity(entity).build();
        }catch (EntityDoesNotExistException e) {
            return Response.status(Response.Status.CONFLICT).entity(e.getMessage()).build();
        }catch (ConstraintViolationException e){
            return Response.status(Response.Status.BAD_REQUEST).entity(Utils.getConstraintViolationMessages(e)).build();
        }catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST).entity("An unexpected error has occurred.").build();
        } 
    }
    @POST
    @Path("{templateId}/artifacts")
    @RolesAllowed("Administrator")
    @Consumes(MediaType.APPLICATION_XML)
    @Produces(MediaType.APPLICATION_XML)
    public Response createArtifact(@PathParam("templateId") Long templateId, ArtifactDTO artifactDTO){
        try {
            if (templateId == null)
                throw new EntityDoesNotExistException("Invalid template");
            
            Template template = em.find(Template.class, templateId);
            if(template == null) 
                throw new EntityDoesNotExistException("Template not found.");

            Artifact artifact = new Artifact(
                artifactDTO.getFilepath(), 
                artifactDTO.getDesiredName(), 
                artifactDTO.getMimeType()
            );
            
            template.addArtifact(artifact);
            em.persist(template);
            
        return Response.status(Response.Status.CREATED).entity("Artifact was successfully created.").build();
        }catch (EntityDoesNotExistException e) {
            return Response.status(Response.Status.CONFLICT).entity(e.getMessage()).build();
        }catch (ConstraintViolationException e){
            return Response.status(Response.Status.BAD_REQUEST).entity(Utils.getConstraintViolationMessages(e)).build();
        }catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST).entity("An unexpected error has occurred.").build();
        }
    }
    @DELETE
    @Path("/{templateId}/artifacts/{id}")
    @RolesAllowed("Administrator")
    @Consumes(MediaType.APPLICATION_XML)
    @Produces(MediaType.APPLICATION_XML)
    public Response removeArtifact(@PathParam("templateId") Long templateId, @PathParam("id") Long id){
        try{
            if (templateId == null)
                throw new EntityDoesNotExistException("Invalid Template");
            Template template = em.find(Template.class, templateId);
            if(template == null) 
                throw new EntityDoesNotExistException("Template not found.");
            
            if (id == null) 
                throw new EntityDoesNotExistException("Invalid Artifact");
            Artifact artifact = em.find(Artifact.class, id);
            if (artifact == null) {
                throw new EntityDoesNotExistException("A artifact with that id doesnt exists.");
            }
            
            template.removeArtifact(artifact);
            em.persist(template);
            em.remove(artifact);
            
            return Response.status(Response.Status.OK).entity("Artifact was successfully deleted.").build();
        }catch (EntityDoesNotExistException e) {
            return Response.status(Response.Status.NOT_FOUND).entity(e.getMessage()).build();
        }catch (ConstraintViolationException e){
            return Response.status(Response.Status.BAD_REQUEST).entity(Utils.getConstraintViolationMessages(e)).build();
        }catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST).entity("An unexpected error has occurred.").build();
        }
    }
    
    //Modules
    @GET
    @Path("/{templateId}/modules")
    @PermitAll
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public Response getModules(@PathParam("templateId") Long templateId){
        try{
            if (templateId == null)
                throw new EntityDoesNotExistException("Invalid template");
            
            Template template = em.find(Template.class, templateId);
            if(template == null) 
                throw new EntityDoesNotExistException("Template not found.");

            Collection<ModuleDTO> modulesDTO
                    = toDTOs(template.getModules(), ModuleDTO.class);
            GenericEntity<List<ModuleDTO>> entity =
                new GenericEntity<List<ModuleDTO>>(new ArrayList<>(modulesDTO)) {};      

            return Response.status(Response.Status.OK).entity(entity).build();
        }catch (EntityDoesNotExistException e) {
            return Response.status(Response.Status.CONFLICT).entity(e.getMessage()).build();
        }catch (ConstraintViolationException e){
            return Response.status(Response.Status.BAD_REQUEST).entity(Utils.getConstraintViolationMessages(e)).build();
        }catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST).entity("An unexpected error has occurred.").build();
        } 
    }
    @POST
    @Path("{templateId}/modules")
    @RolesAllowed("Administrator")
    @Consumes(MediaType.APPLICATION_XML)
    @Produces(MediaType.APPLICATION_XML)
    public Response createModule(@PathParam("templateId") Long templateId, ModuleDTO moduleDTO){
        try{
            if (templateId == null)
                throw new EntityDoesNotExistException("Invalid template");
            
            Template template = em.find(Template.class, templateId);
            if(template == null) 
                throw new EntityDoesNotExistException("Template not found.");
    
            template.addModule(new Module(
                moduleDTO.getName(), 
                moduleDTO.getVersion()
            ));
            
            em.persist(template);   
            return Response.status(Response.Status.CREATED).entity("Module was successfully created.").build();
        }catch (ConstraintViolationException e){
            return Response.status(Response.Status.BAD_REQUEST).entity(Utils.getConstraintViolationMessages(e)).build();
        }catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST).entity("An unexpected error has occurred.").build();
        }
    }
    @DELETE
    @Path("/{templateId}/modules/{id}")
    @RolesAllowed("Administrator")
    @Consumes(MediaType.APPLICATION_XML)
    @Produces(MediaType.APPLICATION_XML)
    public Response removeModule(@PathParam("templateId") Long templateId, @PathParam("id") Long id){
        try{
            if (templateId == null)
                throw new EntityDoesNotExistException("Invalid template");
            Template template = em.find(Template.class, templateId);
            if(template == null) 
                throw new EntityDoesNotExistException("Template not found.");
            
            if (id == null) 
                throw new EntityDoesNotExistException("Invalid Module");
            Module module = em.find(Module.class, id);
            if (module == null) {
                throw new EntityDoesNotExistException("A module with that id doesnt exists.");
            }
            
            template.removeModule(module);
            em.persist(template);
            em.remove(module);
            
            return Response.status(Response.Status.OK).entity("Module was successfully deleted.").build();
        }catch (EntityDoesNotExistException e) {
            return Response.status(Response.Status.NOT_FOUND).entity(e.getMessage()).build();
        }catch (ConstraintViolationException e){
            return Response.status(Response.Status.BAD_REQUEST).entity(Utils.getConstraintViolationMessages(e)).build();
        }catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST).entity("An unexpected error has occurred.").build();
        }
    }
    
    //Base On
    @POST
    @Path("{templateId}/baseOn/Configuration")
    @RolesAllowed("Administrator")
    @Consumes(MediaType.APPLICATION_XML)
    @Produces(MediaType.APPLICATION_XML)
    public Response createConfigurationBasedOnTemplate(
            @PathParam("templateId") Long templateId, ConfigurationDTO configurationDTO){
        try{
            if (templateId == null)
                throw new EntityDoesNotExistException("Invalid template");
            
            Template template = em.find(Template.class, templateId);
            if(template == null) 
                throw new EntityDoesNotExistException("Template not found.");
    
            if (configurationDTO.getOwner() == null)
                throw new EntityDoesNotExistException("Invalid Owner.");
            Client owner = em.find(Client.class, configurationDTO.getOwner());
            
            Configuration newConfiguration = new Configuration(
                configurationDTO.getName(),
                configurationDTO.getDescription(),
                Configuration.Status.ACTIVE,
                configurationDTO.getBaseVersion(),
                owner,
                configurationDTO.getContractDate()
            );
            
            List<Module> modules = new ArrayList<>();
            template.getModules().forEach((module) -> {
                modules.add(new Module(
                    module.getName(), 
                    module.getVersion()
                ));
            });
            newConfiguration.setModules(modules);
            
            List<Artifact> artifacts = new ArrayList<>();
            template.getArtifacts().forEach((arti) -> {
                artifacts.add(new Artifact(
                        arti.getFilepath(),
                        arti.getDesiredName(),
                        arti.getMimeType()
                ));
            });
            newConfiguration.setArtifacts(artifacts);
            
            owner.addConfiguration(newConfiguration);
            em.persist(owner);
            
            ConfigurationDTO newConfigurationDTO = ConfigurationBean.convertDTO(newConfiguration);          
            return Response.status(Response.Status.CREATED).entity(newConfigurationDTO).build();
        }catch (ConstraintViolationException e){
            return Response.status(Response.Status.BAD_REQUEST).entity(Utils.getConstraintViolationMessages(e)).build();
        }catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST).entity("An unexpected error has occurred.").build();
        }
    }
    
    
    @GET
    @PermitAll
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public Response getAll(){
        try{
            List<Template> templates = 
                    em.createNamedQuery("getAllTemplates").getResultList();
            
            Collection<TemplateDTO> templatesDTO
                    = toDTOs(templates, TemplateDTO.class);
            
            GenericEntity<List<TemplateDTO>> entity =
                new GenericEntity<List<TemplateDTO>>(new ArrayList<>(templatesDTO)) {};     

            return Response.status(Response.Status.OK).entity(entity).build();
        }catch (ConstraintViolationException e){
            return Response.status(Response.Status.BAD_REQUEST).entity(Utils.getConstraintViolationMessages(e)).build();
        }catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST).entity("An unexpected error has occurred.").build();
        } 
    }    
    
   
}
