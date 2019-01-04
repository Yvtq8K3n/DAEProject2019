/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejbs;

import dtos.UserDTO;
import entities.User;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.annotation.security.PermitAll;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;


/**
 *
 * @author Joao Marquez
 */
@Stateless
@Path("/users")
@PermitAll
public class UserBean extends Bean<User>{

    @PersistenceContext(name="dae_project")//Peristance context usa o nome da bd do persistance.xml
    EntityManager em;
  
    @GET
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public Collection<UserDTO> getAll(){
        List<User> users =
            em.createNamedQuery("getAllUsers").getResultList();
        
        return toDTOs(users, UserDTO.class);       
    }
    
    static UserDTO convertDTO(User user){
        UserDTO DTO = new UserDTO(
            user.getUsername(),
            user.getPassword(),
            user.getName(),
            user.getEmail(),
            user.getGroup().toString()
        );   
        return DTO;
    }
    
    static List<UserDTO> convertDTOs(List<User> users){
        List<UserDTO> usersDTO = new ArrayList<>();
        users.forEach((user) -> {
            usersDTO.add(convertDTO(user));
        });
        return usersDTO;
    }
}
