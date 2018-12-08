package web;

import ejbs.ProductCatalogBean;
import entities.Template;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;
import java.util.stream.Collectors;
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
 
    private List<Template> templates;
    private List<Template> filterTemplates;
    private String searchTemplate;   
    
    //Gonna disappear after rest    
    @EJB
    private ProductCatalogBean productCatalogBean;
    
    
    @PostConstruct
    public void Init(){
        client = ClientBuilder.newClient();
        refreshFilterTemplates();
    };
    
    
    
    /**
    *
    * Methods
     * @return 
    */
    
    public List<Template> getAllProductCatalog(){
        List<Template> templates = new ArrayList<>();
        templates.addAll(productCatalogBean.getAll());
        
        return templates;
    }
    
    public Template getSelectedTemplate() {
        return selectedTemplate;
    }
    public void setSelectedTemplate(Template selectedTemplate) {
        this.selectedTemplate = selectedTemplate;
    }   
    
    
    ////////////////////////////////////////////////////////////////////////////
    //The following code is for the Filter in Catalog View /////////////////////
    ////////////////////////////////////////////////////////////////////////////
    public String getSearchTemplate() {
        return searchTemplate;
    }
    public void setSearchTemplate(String searchTemplate) {
        this.searchTemplate = searchTemplate;
        refreshFilterTemplates();
    }
    
    public List<Template> getFilterTemplates(){
        return filterTemplates;
    }
    public void refreshFilterTemplates(){
        if (searchTemplate==null || searchTemplate.isEmpty()){
            templates  = getAllProductCatalog();
            filterTemplates = templates;
            return;
        }
        
        filterTemplates = templates.stream()
        .filter(p ->
            p.getName().toUpperCase().contains(searchTemplate.toUpperCase())
            || p.getDescription().toUpperCase().contains(searchTemplate.toUpperCase())
        ).collect(Collectors.toList());
    }
}