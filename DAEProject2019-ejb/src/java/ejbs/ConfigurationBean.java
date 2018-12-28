/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejbs;

import dtos.ConfigurationDTO;
import entities.Client;
import entities.Module;
import entities.Configuration;
import entities.Software;
import exceptions.EntityDoesNotExistException;
import exceptions.EntityExistsException;
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
import javax.ws.rs.POST;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;


/**
 *
 * @author Joao Marquez
 */
@Stateless //Distinge que é um ejb (componente que não gere instancia nem ciclo de vida)
//Faz pedidos mas não guardam de quem esta a fazer
//Faz com que ´não tenha de ter uma instancia para cada utilizador
public class ConfigurationBean extends Bean<Configuration>{

    @PersistenceContext(name="dae_project")//Peristance context usa o nome da bd do persistance.xml
    EntityManager em;
   
    @POST
    @RolesAllowed("Administrator")
    @Consumes(MediaType.APPLICATION_XML)
    @Produces(MediaType.APPLICATION_XML)
    public Response create(ConfigurationDTO confDTO) {
        try{
            if (confDTO.getClientUsername() == null)
                throw new EntityDoesNotExistException("Invalid Username");
            
            Client client = em.find(Client.class, confDTO.getClientUsername());
            if(client == null) 
                throw new EntityDoesNotExistException("Client not found.");
            
            Configuration conf = new Configuration(
                    confDTO.getName(), 
                    confDTO.getDescription(),
                    confDTO.getBaseVersion(), 
                    client, 
                    confDTO.getContractDate()
            );
            em.persist(conf);
        return Response.status(Response.Status.CREATED).entity("Configuration was successfully created.").build();
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
    
    public Collection<ConfigurationDTO> getProductConfigurations(Long productId)
     throws EntityDoesNotExistException{
        try {
            Configuration product = em.find(Configuration.class, productId);
            if(product == null){
                throw new EntityDoesNotExistException("Product with id: " + productId + " does not exist!!!");
            }
            List<Configuration> configurations = new ArrayList<>();
            return toDTOs(configurations, ConfigurationDTO.class);
        }catch (EntityDoesNotExistException e) {
            throw e;
        }catch (Exception e) {
            throw new EJBException(e.getMessage());
        }
    }
    
    public Collection<ConfigurationDTO> getAll(){
        List<Configuration> configurations =
               em.createNamedQuery("getAllConfigurations").getResultList();

        return toDTOs(configurations, ConfigurationDTO.class);
    }
}
