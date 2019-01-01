/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejbs;

import dtos.ConfigurationDTO;
import dtos.ArtifactDTO;
import dtos.CommentDTO;
import dtos.ModuleDTO;
import entities.Artifact;
import entities.Client;
import entities.Comment;
import entities.Module;
import entities.Configuration;
import entities.Software;
import exceptions.EntityDoesNotExistException;
import exceptions.EntityExistsException;
import exceptions.MyConstraintViolationException;
import exceptions.Utils;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.annotation.security.DeclareRoles;
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
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;


/**
 *
 * @author Joao Marquez
 */

@Stateless
@Path("/configurations")
@DeclareRoles({"Administrator", "Client"})
public class ConfigurationBean extends Bean<Configuration>{

    @PersistenceContext(name="dae_project")//Peristance context usa o nome da bd do persistance.xml
    EntityManager em;
   
    @POST
    @RolesAllowed("Administrator")
    @Consumes(MediaType.APPLICATION_XML)
    @Produces(MediaType.APPLICATION_XML)
    public Response create(ConfigurationDTO confDTO) {
        try{
            if (confDTO.getOwner()== null)
                throw new EntityDoesNotExistException("Invalid Username");
            
            Client client = em.find(Client.class, confDTO.getOwner());
            if(client == null) 
                throw new EntityDoesNotExistException("Client not found.");
            
            Configuration conf = new Configuration(
                    confDTO.getName(), 
                    confDTO.getDescription(),
                    confDTO.getBaseVersion(), 
                    client, 
                    confDTO.getContractDate()
            );
            client.addProduct(conf);
            
            em.persist(client);
        return Response.status(Response.Status.CREATED).entity("Configuration was successfully created.").build();
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
    public Response remove(@PathParam("id") String id) 
        throws EntityDoesNotExistException, MyConstraintViolationException{
        try{
            if (id == null) 
                throw new EntityDoesNotExistException("Invalid Configuration");
            Configuration configuration = em.find(Configuration.class, Long.valueOf(id));
            if(configuration == null) 
                throw new EntityDoesNotExistException("Configuration not found.");
            System.out.println("ola");
            
            Client client = configuration.getOwner();
            if (client == null) 
                throw new EntityDoesNotExistException("Invalid Owner");         
            
            client.removeProduct(configuration);
            
            em.persist(client); 
           return Response.status(Response.Status.OK).entity("Configuration was successfully deleted.").build();
        }catch (EntityDoesNotExistException e) {
            return Response.status(Response.Status.NOT_FOUND).entity(e.getMessage()).build();
        }catch (ConstraintViolationException e){
            return Response.status(Response.Status.BAD_REQUEST).entity(Utils.getConstraintViolationMessages(e)).build();
        }catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST).entity("An unexpected error has occurred.").build();
        }
    }
    

    @POST
    @RolesAllowed("Administrator")
    @Path("{id}/artifacts")
    @Consumes(MediaType.APPLICATION_XML)
    @Produces(MediaType.APPLICATION_XML)
    public Response addArtifact(@PathParam("id") Long id, ArtifactDTO artifactDTO){
        try {
            if (id == null)
                throw new EntityDoesNotExistException("Invalid configuration");
            
            Configuration configuration = em.find(Configuration.class, id);
            if(configuration == null) 
                throw new EntityDoesNotExistException("Configuration not found.");

            Artifact artifact = new Artifact(
                artifactDTO.getFilepath(), 
                artifactDTO.getDesiredName(), 
                artifactDTO.getMimeType()
            );
            
            configuration.addArtifact(artifact);
            em.persist(configuration);
            
        return Response.status(Response.Status.CREATED).entity("Artifact was successfully created.").build();
        }catch (EntityDoesNotExistException e) {
            return Response.status(Response.Status.CONFLICT).entity(e.getMessage()).build();
        }catch (ConstraintViolationException e){
            return Response.status(Response.Status.BAD_REQUEST).entity(Utils.getConstraintViolationMessages(e)).build();
        }catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST).entity("An unexpected error has occurred.").build();
        }
    }
    
    public void addModule(Long configurationId, Long moduleId){
        try {
            Configuration configuration = em.find(Configuration.class, configurationId);
            if (configuration == null) {
                System.out.println("ERRO NO METODO ADD_MODULE DE CONFIG_BEAN - NÃO ENCONTROU CONFIGURATION");
            }else{
                System.out.println("ENCONTROU CONFIGURATION " + configuration.getDescription());
            }
            Module module = em.find(Module.class, moduleId);
            if (module == null) {
                System.out.println("ERRO NO METODO ADD_MODULE DE CONFIG_BEAN - NÃO ENCONTROU MODULE");   
            }else{
                System.out.println("ENCONTROU MODULE " + module.getName());
            }     
            
            configuration.addModule(module);
            em.merge(configuration);
            
        } catch (Exception e) {
            System.out.println("ERRO NO METODO ADD_MODULE DE CONFIG_BEAN " + e.getMessage());
        }
    }

    @GET
    @Path("/{id}/comments")
    @RolesAllowed("Administrator")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public Response getComments(@PathParam("id") String id){
        try{
            if (id == null)
                throw new EntityDoesNotExistException("Invalid configuration");
            
            Configuration configuration = em.find(Configuration.class, Long.valueOf(id));
            if(configuration == null) 
                throw new EntityDoesNotExistException("Configuration not found.");
                
            System.out.println("Amount of Configuarations:"+configuration.getComments().size());
 
            Collection<CommentDTO> commentsDTO 
                = CommentBean.convertCommentDTOs(configuration.getComments());  
            GenericEntity<List<CommentDTO>> entity =
                new GenericEntity<List<CommentDTO>>(new ArrayList<>(commentsDTO)) {};      

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
    @Path("/{id}/modules")
    @RolesAllowed({"Administrator","Client"})
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public Response getModules(@PathParam("id") String id){
        try{
            if (id == null)
                throw new EntityDoesNotExistException("Invalid configuration");
            
            Configuration configuration = em.find(Configuration.class, Long.valueOf(id));
            if(configuration == null) 
                throw new EntityDoesNotExistException("Configuration not found.");

            Collection<ModuleDTO> modulesDTO
                    = toDTOs(configuration.getModules(), ModuleDTO.class);
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
    
    @GET
    @Path("/{id}/artifacts")
    @RolesAllowed({"Administrator","Client"})
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public Response getArtifacts(@PathParam("id") Long id){
        try{
            if (id == null)
                throw new EntityDoesNotExistException("Invalid configuration");
            
             Configuration configuration = em.find(Configuration.class, id);
            if(configuration == null) 
                throw new EntityDoesNotExistException("Configuration not found.");

            Collection<ArtifactDTO> artifactsDTO
                    = toDTOs(configuration.getArtifacts(), ArtifactDTO.class);
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

    
    @GET
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    @Path("all")
    public Collection<ConfigurationDTO> getAll(){
        Collection<Configuration> configurations = em.createNamedQuery("getAllConfigurations").getResultList();

        return toDTOs(configurations, ConfigurationDTO.class);
    }

}
