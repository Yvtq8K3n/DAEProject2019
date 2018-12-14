/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejbs;

import entities.Client;
import entities.Comment;
import entities.Configuration;
import javax.ejb.EJBException;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;


/**
 *
 * @author Joao Marquez
 */
@Stateless //Distinge que é um ejb (componente que não gere instancia nem ciclo de vida)
//Faz pedidos mas não guardam de quem esta a fazer
//Faz com que ´não tenha de ter uma instancia para cada utilizador
public class CommentBean extends Bean<Comment>{

    @PersistenceContext(name="DAEProject2019")//Peristance context usa o nome da bd do persistance.xml
    EntityManager em;
   
    public Comment create(Long id, long idConf, String msg) {
        Comment parent = null;
       
              
        if (id !=null){
            parent = em.find(Comment.class, id);
        }
        
        if (parent !=null){
            System.out.println("COMMENT_BEAN CREATE parentid: "+ parent.getId());
        }
        
        //if (idConf == null) throw new EJBException();
        Configuration configuration = em.find(Configuration.class, idConf);
        if (configuration == null) System.out.println("ERROR INVALID ID_CONFIG!");
        
        Comment comment =  new Comment(parent, configuration ,"Initial comment");
        em.persist(comment);
        
        return comment;
    }
}
