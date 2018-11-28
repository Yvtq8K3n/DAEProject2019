package web;

import ejbs.ProductCatalogBean;
import entities.Template;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import javax.faces.component.UIComponent;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;

/**
 *
 * @author Joao Marquez
 */
@Named(value = "guestManager")
@SessionScoped
public class GuestManager implements Serializable {
    
    private static final Logger logger = Logger.getLogger("web.GuestManager");
    private UIComponent component;
    private Client client;
    
    private Template selectedTemplate;
 
    
    
    //Gonna disappear after rest    
    @EJB
    private ProductCatalogBean productCatalogBean;
    
    
    @PostConstruct
    public void Init(){
        client = ClientBuilder.newClient();
    };
    
    
    
    /**
    *
    * Methods
     * @return 
    */
    
    public List<Template> getAllTemplates(){
        List<Template> templates = new ArrayList<>();
        templates.addAll(productCatalogBean.getAll());
        
        return templates;
    }

    //Case fizermos isto ira ser necessario ter um DTO com uma dummy List<Configuration> vazia
    public Template getSelectedTemplate() {
        return selectedTemplate;
    }

    public void setSelectedTemplate(Template selectedTemplate) {
        this.selectedTemplate = selectedTemplate;
    }   
}