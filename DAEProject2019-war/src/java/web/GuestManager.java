package web;

import dtos.ProductDTO;
import ejbs.ProductCatalogBean;
import entities.Configuration;
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
import javax.faces.bean.ManagedBean;
import javax.faces.component.UIComponent;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;

/**
 *
 * @author Joao Marquez
 */
@ManagedBean
@SessionScoped
public class GuestManager implements Serializable {
    
    private static final Logger logger = Logger.getLogger("web.GuestManager");
    private UIComponent component;
    private Client client;
    
    private ProductDTO selectedProduct;
 
    private List<ProductDTO> templates;
    private List<ProductDTO> filterTemplates;
    private String searchTemplate;   
    
    //Gonna disappear after rest    
    @EJB
    private ProductCatalogBean productCatalogBean;
    
    public void GuestManager(){
        
    }
    
    
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
    
    public List<ProductDTO> getAllProductCatalog(){
        List<ProductDTO> templates = new ArrayList<>();
        templates.addAll(productCatalogBean.getAll());
        
        return templates;
    }
    
    public ProductDTO getSelectedProduct() {
        return selectedProduct;
    }
    public void setSelectedProduct(ProductDTO selectedProduct) {
        this.selectedProduct = selectedProduct;
    }   
    
    public List<Configuration> getConfigurations(){
        List<Configuration> configurations = new ArrayList<>();
        configurations.addAll(productCatalogBean.getConfigurations(selectedProduct.getId()));
            
        return configurations;
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
    
    public List<ProductDTO> getFilterTemplates(){
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

    void reset() {
        logger.warning("I WAS CALLED");
        templates  = getAllProductCatalog();
    }
}