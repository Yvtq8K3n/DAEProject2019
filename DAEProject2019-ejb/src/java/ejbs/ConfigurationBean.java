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
import dtos.ParameterDTO;
import entities.Artifact;
import entities.Client;
import entities.Comment;
import entities.Module;
import entities.Configuration;
import entities.Parameter;
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
                    Configuration.Status.valueOf(confDTO.getStatus().toString()),
                    confDTO.getBaseVersion(), 
                    client, 
                    confDTO.getContractDate()
            );
            client.addConfiguration(conf);
            
            em.persist(client);
        return Response.status(Response.Status.CREATED).entity(String.valueOf(conf.getId())).build();
        }catch (EntityDoesNotExistException e) {
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
    public Response update(ConfigurationDTO confDTO) {
        try{
            System.out.println("I was executed");
            if (confDTO.getId()== null)
                throw new EntityDoesNotExistException("Invalid Configuration");
            
            Configuration conf = em.find(Configuration.class, confDTO.getId());
            if(conf == null) 
                throw new EntityDoesNotExistException("Configuration not found.");
            
            conf.setName(confDTO.getName());
            conf.setDescription(confDTO.getDescription());
            conf.setStatus(Configuration.Status.valueOf(confDTO.getStatus().toString()));
            conf.setBaseVersion(confDTO.getBaseVersion());
            conf.setContractDate(confDTO.getContractDate());
            
            
            em.persist(conf);
            System.out.println("WHIT SUCCESS");
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
                throw new EntityDoesNotExistException("Invalid Configuration");
            Configuration configuration = em.find(Configuration.class, id);
            if(configuration == null) 
                throw new EntityDoesNotExistException("Configuration not found.");
            
            Client client = configuration.getOwner();
            if (client == null) 
                throw new EntityDoesNotExistException("Invalid Owner");         
            
            client.removeConfiguration(configuration);
            
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
    
    
    //Artifacts
    @GET
    @Path("/{confId}/artifacts")
    @RolesAllowed({"Administrator","Client"})
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public Response getArtifacts(@PathParam("confId") Long confId){
        try{
            if (confId == null)
                throw new EntityDoesNotExistException("Invalid configuration");
            
             Configuration configuration = em.find(Configuration.class, confId);
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
    @POST
    @Path("{confId}/artifacts")
    @RolesAllowed("Administrator")
    @Consumes(MediaType.APPLICATION_XML)
    @Produces(MediaType.APPLICATION_XML)
    public Response createArtifact(@PathParam("confId") Long confId, ArtifactDTO artifactDTO){
        try {
            if (confId == null)
                throw new EntityDoesNotExistException("Invalid configuration");
            
            Configuration configuration = em.find(Configuration.class, confId);
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
    @DELETE
    @Path("/{confId}/artifacts/{id}")
    @RolesAllowed("Administrator")
    @Consumes(MediaType.APPLICATION_XML)
    @Produces(MediaType.APPLICATION_XML)
    public Response removeArtifact(@PathParam("confId") Long confId, @PathParam("id") Long id){
        try{
            if (confId == null)
                throw new EntityDoesNotExistException("Invalid configuration");
            Configuration configuration = em.find(Configuration.class, confId);
            if(configuration == null) 
                throw new EntityDoesNotExistException("Configuration not found.");
            
            if (id == null) 
                throw new EntityDoesNotExistException("Invalid Artifact");
            Artifact artifact = em.find(Artifact.class, id);
            if (artifact == null) {
                throw new EntityDoesNotExistException("A artifact with that id doesnt exists.");
            }
            
            configuration.removeArtifact(artifact);
            em.persist(configuration);
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
    @Path("/{confId}/modules")
    @RolesAllowed({"Administrator","Client"})
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public Response getModules(@PathParam("confId") Long confId){
        try{
            if (confId == null)
                throw new EntityDoesNotExistException("Invalid configuration");
            
            Configuration configuration = em.find(Configuration.class, confId);
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
    @Path("/{confId}/parameters")
    @RolesAllowed({"Administrator","Client"})
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public Response getParameters(@PathParam("confId") Long confId){
        try{
            if (confId == null)
                throw new EntityDoesNotExistException("Invalid configuration");
            
            Configuration configuration = em.find(Configuration.class, confId);
            if(configuration == null) 
                throw new EntityDoesNotExistException("Configuration not found.");

            Collection<ParameterDTO> parameterDTOs = new ArrayList<>();
            for(Parameter parameter : configuration.getParameters()){
                parameterDTOs.add(new ParameterDTO(
                        parameter.getId(),
                        ParameterDTO.MaterialType.valueOf(parameter.getMaterialType().toString()), 
                        parameter.getName(), 
                        parameter.getDescription(), 
                        parameter.getValidDate()));
            }
            GenericEntity<List<ParameterDTO>> entity =
                new GenericEntity<List<ParameterDTO>>(new ArrayList<>(parameterDTOs)) {};      

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
    @Path("{confId}/modules")
    @RolesAllowed("Administrator")
    @Consumes(MediaType.APPLICATION_XML)
    @Produces(MediaType.APPLICATION_XML)
    public Response createModule(@PathParam("confId") Long confId, ModuleDTO moduleDTO){
        try{
            if (confId == null)
                throw new EntityDoesNotExistException("Invalid configuration");
            
            Configuration configuration = em.find(Configuration.class, confId);
            if(configuration == null) 
                throw new EntityDoesNotExistException("Configuration not found.");
    
            configuration.addModule(new Module(
                moduleDTO.getName(), 
                moduleDTO.getVersion()
            ));
            
            em.persist(configuration);   
            return Response.status(Response.Status.CREATED).entity("Module was successfully created.").build();
        }catch (ConstraintViolationException e){
            return Response.status(Response.Status.BAD_REQUEST).entity(Utils.getConstraintViolationMessages(e)).build();
        }catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST).entity("An unexpected error has occurred.").build();
        }
    }
    
    @POST
    @Path("{confId}/parameters")
    @RolesAllowed("Administrator")
    @Consumes(MediaType.APPLICATION_XML)
    @Produces(MediaType.APPLICATION_XML)
    public Response createParameter(@PathParam("confId") Long confId, ParameterDTO parameterDTO){
        try{
            System.out.println("DENTRO 1");
            if (confId == null)
                throw new EntityDoesNotExistException("Invalid configuration");
            
            System.out.println("DENTRO 2");
            Configuration configuration = em.find(Configuration.class, confId);
            if(configuration == null) 
                throw new EntityDoesNotExistException("Configuration not found.");
            System.out.println("DENTRO 3");
            configuration.addParameter(new Parameter(
                    Parameter.MaterialType.valueOf(parameterDTO.getMaterialType().toString()),
                    parameterDTO.getName(), 
                    parameterDTO.getDescription(), 
                    parameterDTO.getValidDate()
            ));
            System.out.println("DENTRO 4");
            em.persist(configuration);
            System.out.println("DENTRO 5");
            return Response.status(Response.Status.CREATED).entity("Parameter was successfully created.").build();
        }catch (ConstraintViolationException e){
            return Response.status(Response.Status.BAD_REQUEST).entity(Utils.getConstraintViolationMessages(e)).build();
        }catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST).entity("An unexpected error has occurred.").build();
        }
    }
    
    @DELETE
    @Path("/{confId}/modules/{id}")
    @RolesAllowed("Administrator")
    @Consumes(MediaType.APPLICATION_XML)
    @Produces(MediaType.APPLICATION_XML)
    public Response removeModule(@PathParam("confId") Long confId, @PathParam("id") Long id){
        try{
            if (confId == null)
                throw new EntityDoesNotExistException("Invalid configuration");
            Configuration configuration = em.find(Configuration.class, confId);
            if(configuration == null) 
                throw new EntityDoesNotExistException("Configuration not found.");
            
            if (id == null) 
                throw new EntityDoesNotExistException("Invalid Module");
            Module module = em.find(Module.class, id);
            if (module == null) {
                throw new EntityDoesNotExistException("A module with that id doesnt exists.");
            }
            
            configuration.removeModule(module);
            em.persist(configuration);
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
    
    @DELETE
    @Path("/{confId}/parameters/{id}")
    @RolesAllowed("Administrator")
    @Consumes(MediaType.APPLICATION_XML)
    @Produces(MediaType.APPLICATION_XML)
    public Response removeParameter(@PathParam("confId") Long confId, @PathParam("id") Long id){
        try{
            if (confId == null)
                throw new EntityDoesNotExistException("Invalid configuration");
            Configuration configuration = em.find(Configuration.class, confId);
            if(configuration == null) 
                throw new EntityDoesNotExistException("Configuration not found.");
            
            if (id == null) 
                throw new EntityDoesNotExistException("Invalid Parameter");
            Parameter parameter = em.find(Parameter.class, id);
            if (parameter == null) {
                throw new EntityDoesNotExistException("A module with that id doesnt exists.");
            }
            
            configuration.removeParameter(parameter);
            em.persist(configuration);
            em.remove(parameter);
            
            return Response.status(Response.Status.OK).entity("Parameter was successfully deleted.").build();
        }catch (EntityDoesNotExistException e) {
            return Response.status(Response.Status.NOT_FOUND).entity(e.getMessage()).build();
        }catch (ConstraintViolationException e){
            return Response.status(Response.Status.BAD_REQUEST).entity(Utils.getConstraintViolationMessages(e)).build();
        }catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST).entity("An unexpected error has occurred.").build();
        }
    }
    

    //Comments
    @GET
    @Path("/{id}/comments")
    @RolesAllowed({"Administrator","Client"})
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public Response getComments(@PathParam("id") String id){
        try{
            if (id == null)
                throw new EntityDoesNotExistException("Invalid configuration");
            
            Configuration configuration = em.find(Configuration.class, Long.valueOf(id));
            if(configuration == null) 
                throw new EntityDoesNotExistException("Configuration not found.");
                
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
    @RolesAllowed("Administrator")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public Response getAll(){
        try{
            List<Configuration> configurations = 
                    em.createNamedQuery("getAllConfigurations").getResultList();
            
            Collection<ConfigurationDTO> configurationsDTO
                    = ConfigurationBean.convertDTOs(configurations);

            GenericEntity<List<ConfigurationDTO>> entity =
                new GenericEntity<List<ConfigurationDTO>>(new ArrayList<>(configurationsDTO)) {};     

            return Response.status(Response.Status.OK).entity(entity).build();
        }catch (ConstraintViolationException e){
            return Response.status(Response.Status.BAD_REQUEST).entity(Utils.getConstraintViolationMessages(e)).build();
        }catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST).entity("An unexpected error has occurred.").build();
        } 
    }
    
    //Base On
    @POST
    @Path("{confId}/baseOn/Configuration")
    @RolesAllowed("Administrator")
    @Consumes(MediaType.APPLICATION_XML)
    @Produces(MediaType.APPLICATION_XML)
    public Response createConfigurationBasedOnTemplate(
            @PathParam("confId") Long confId, ConfigurationDTO configurationDTO){
        try{
            if (confId == null)
                throw new EntityDoesNotExistException("Invalid configuration");
            
            Configuration configuration = em.find(Configuration.class, confId);
            if(configuration == null) 
                throw new EntityDoesNotExistException("Configuration not found.");
    
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
            configuration.getModules().forEach((module) -> {
                modules.add(new Module(
                    module.getName(), 
                    module.getVersion()
                ));
            });
            newConfiguration.setModules(modules);
            
            List<Artifact> artifacts = new ArrayList<>();
            configuration.getArtifacts().forEach((arti) -> {
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
    
    
    static ConfigurationDTO convertDTO(Configuration configuration){
        ConfigurationDTO DTO = new ConfigurationDTO(
            configuration.getId(),
            configuration.getName(),
            configuration.getDescription(),
            configuration.getBaseVersion(),
            (configuration.getOwner()!= null)?configuration.getOwner().getUsername(): null,
            ConfigurationDTO.Status.valueOf(configuration.getStatus().toString()),
            configuration.getContractDate()
        );   
        return DTO;
    }
    
    static List<ConfigurationDTO> convertDTOs(List<Configuration> configurations){
        List<ConfigurationDTO> configurationsDTO = new ArrayList<>();
        configurations.forEach((configuration) -> {
            configurationsDTO.add(convertDTO(configuration));
        });
        return configurationsDTO;
    }
}
