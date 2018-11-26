/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejbs;

import entities.Administrator;
import entities.Configuration;
import entities.Template;
import java.util.ArrayList;
import java.util.List;
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
public class TemplateBean{

    @PersistenceContext(name="DAEProject2019")//Peristance context usa o nome da bd do persistance.xml
    EntityManager em;
   
    public void create(String name, String description) {
        Template template = new Template(name, description);
        em.persist(template);
    }
    
    public List<Configuration> getConfigurations(Long id){
        Template template = em.find(Template.class, id);
        if (template == null) System.out.println("ERROR");//Temporary
        
        return template.getConfigurations();
    }
    
    public void addConfiguration(long id, long idConf){
        System.out.println("Temp:"+getAll().get(0).getId());
        System.out.println((getAll().get(0).getId()==id?true:false));
        Template template = em.find(Template.class, id);
        if (template == null) System.out.println("ERROR INVALID ID!");//Temporary
        
        Configuration configuration = em.find(Configuration.class, idConf);
        if (configuration == null) System.out.println("ERROR INVALID ID_CONFIG!");
        
        template.addConfiguration(configuration);
    }
    
    public List<Template> getAll(){
        List<Template> templates = new ArrayList<>();
        templates = em.createNamedQuery("getAllTemplates").getResultList();

        return templates;
    }
}
