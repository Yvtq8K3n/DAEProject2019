/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejbs;

import entities.Module;
import javax.ejb.Stateless;
import javax.ejb.LocalBean;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author admin
 */
@Stateless
@LocalBean
public class ModuleBean{
   
   @PersistenceContext(name="dae_project")
   EntityManager em;
   
   public void create(String name, String version){
       Module module = new Module(name, version);
       em.persist(module);
   }
}
