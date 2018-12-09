/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejbs;

import entities.Administrator;
import entities.Client;
import exceptions.EntityDoesNotExistException;
import exceptions.EntityExistsException;
import exceptions.MyConstraintViolationException;
import exceptions.Utils;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJBException;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.validation.ConstraintViolationException;


/**
 *
 * @author Joao Marquez
 */
@Stateless //Distinge que é um ejb (componente que não gere instancia nem ciclo de vida)
//Faz pedidos mas não guardam de quem esta a fazer
//Faz com que ´não tenha de ter uma instancia para cada utilizador
public class AdministratorBean{

    @PersistenceContext(name="DAEProject2019")//Peristance context usa o nome da bd do persistance.xml
    EntityManager em;
   
    public void create(Administrator administratorDTO) 
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
    
    
    public List<Administrator> getAll(){
        List<Administrator> administrators = new ArrayList<>();
        administrators = em.createNamedQuery("getAllAdministrators").getResultList();

        return administrators;
    }
}
