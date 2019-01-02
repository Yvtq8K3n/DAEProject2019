/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejbs;

import dtos.CommentDTO;
import entities.Client;
import entities.Comment;
import entities.Configuration;
import entities.User;
import exceptions.EntityDoesNotExistException;
import exceptions.Utils;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.security.DeclareRoles;
import javax.annotation.security.RolesAllowed;
import javax.ejb.EJBException;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.validation.ConstraintViolationException;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;


/**
 *
 * @author Joao Marquez
 */
@Stateless
@Path("/comments")
@DeclareRoles({"Administrator", "Client"})
public class CommentBean extends Bean<Comment>{

    @PersistenceContext(name="dae_project")//Peristance context usa o nome da bd do persistance.xml
    EntityManager em;
   
    @POST
    @RolesAllowed({"Administrator","Client"})
    @Consumes(MediaType.APPLICATION_XML)
    @Produces(MediaType.APPLICATION_XML)
    public Response create(CommentDTO commentDTO) {
        try{
            if (commentDTO.getConfiguration() == null)
                throw new EntityDoesNotExistException("Invalid Configuration");
            Configuration configuration = em.find(Configuration.class, commentDTO.getConfiguration());
            if(configuration == null) 
                throw new EntityDoesNotExistException("Configuration not found.");
            
            if (commentDTO.getAuthor() == null)
                throw new EntityDoesNotExistException("Invalid Autho");
            User author = em.find(User.class, commentDTO.getAuthor());
            if(author == null) 
                throw new EntityDoesNotExistException("Author not found.");

            if (commentDTO.getParent() == null){
                configuration.addComment(
                    new Comment(
                        null,
                        configuration,
                        commentDTO.getMessage(),
                        author
                ));
                em.persist(configuration);
            }else{
                Comment parent = em.find(Comment.class, commentDTO.getParent());
                
                parent.addChildren(new Comment(
                       null,
                       configuration,
                       commentDTO.getMessage(),
                       author
                    ));
                System.out.println("sucscesas");
                em.persist(parent);      
            }  
        return Response.status(Response.Status.CREATED).entity("Comment was successfully created.").build();
        }catch (EntityDoesNotExistException e) {
            return Response.status(Response.Status.CONFLICT).entity(e.getMessage()).build();
        }catch (ConstraintViolationException e){
            return Response.status(Response.Status.BAD_REQUEST).entity(Utils.getConstraintViolationMessages(e)).build();
        }catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST).entity("An unexpected error has occurred.").build();
        } 
    }
    
    static CommentDTO convertDTO(Comment comment){
        comment.getChildren().size();
                
        CommentDTO DTO = new CommentDTO(
            comment.getId(),
            (comment.getParent() != null)?comment.getParent().getId(): null,
            convertCommentDTOs(comment.getChildren()),
            comment.getConfiguration().getId(),
            comment.getMessage(),
            comment.getAuthor().getUsername()
        );   
        return DTO;
    }
    
    static List<CommentDTO> convertCommentDTOs(List<Comment> comments){
        List<CommentDTO> commentsDTO = new ArrayList<>();
        comments.forEach((comment) -> {
            commentsDTO.add(convertDTO(comment));
        });
        return commentsDTO;
    }
}
