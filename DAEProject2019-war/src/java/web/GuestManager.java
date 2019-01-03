package web;

import dtos.ConfigurationDTO;
import dtos.TemplateDTO;
import ejbs.TemplateBean;
import entities.Configuration;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.component.UIComponent;
import javax.inject.Named;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import util.MessageHandler;
import util.URILookup;

/**
 *
 * @author Joao Marquez
 */
@Named(value = "guestManager")
@ManagedBean
@SessionScoped
public class GuestManager implements Serializable {
    private static final int HTTP_CREATED = Response.Status.CREATED.getStatusCode();
    private static final int HTTP_OK = Response.Status.OK.getStatusCode();    
    
    private static final Logger logger = Logger.getLogger("web.GuestManager");
    private UIComponent component;
    
    private ConfigurationDTO selectedProduct;
 
    private List<ConfigurationDTO> templates;
    private String searchTemplate;   
    
    //Gonna disappear after rest    
    @EJB
    private TemplateBean productCatalogBean;
    
    public void GuestManager(){
        
    }
    
    
    @PostConstruct
    public void Init(){
    };
    
   
    
    /**
    *
    * Methods
     * @return 
    */
    

    
    public ConfigurationDTO getSelectedProduct() {
        return selectedProduct;
    }
    public void setSelectedProduct(ConfigurationDTO selectedProduct) {
        this.selectedProduct = selectedProduct;
    }
    
    public List<Configuration> getConfigurations(){
        List<Configuration> configurations = new ArrayList<>();
        //configurations.addAll(productCatalogBean.getConfigurations(selectedProduct.getId()));
            
        return configurations;
    }
    
    ////////////////////////////////////////////////////////////////////////////
    //The following code is for the Filter in Catalog View /////////////////////
    ////////////////////////////////////////////////////////////////////////////
    public List<TemplateDTO> getAllTemplates(){
        try {
            Invocation.Builder invocationBuilder = ClientBuilder.newClient()
                    .target(URILookup.getBaseAPI())
                    .path("/templates")
                    .request(MediaType.APPLICATION_XML);
            
            Response response = invocationBuilder.get(Response.class);
            if (response.getStatus() != HTTP_OK){
                String message = response.readEntity(String.class);
                throw new Exception(message);
            }
            
            List<TemplateDTO> templatesDTO =
                response.readEntity(new GenericType<List<TemplateDTO>>() {}); 
            
            return templatesDTO;
        } catch (Exception e) {
            MessageHandler.failMessage("Unexpected error!", e.getMessage());
            return null;
        }
    }
    
    public List<TemplateDTO> getAllTemplatesCatalog(){
        List<TemplateDTO> allTemplateDTO = getAllTemplates();
        
        //Apply Filter
        if (searchTemplate != null && !searchTemplate.isEmpty()){
            List<TemplateDTO> filterTemplates = allTemplateDTO.stream()
                .filter(p ->
                    p.getName().toUpperCase().contains(searchTemplate.toUpperCase())
                    || p.getDescription().toUpperCase().contains(searchTemplate.toUpperCase())
                ).collect(Collectors.toList());
            return filterTemplates;
        }
        
        return allTemplateDTO;
    }
   
        
    
    
    public String getSearchTemplate() {
        return searchTemplate;
    }
    public void setSearchTemplate(String searchTemplate) {
        this.searchTemplate = searchTemplate;
    }
    
    void reset() {
        //templates  = getAllProductCatalog();
    }
}