package entities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Entity
@Table(name = "ProductCatalog")
@NamedQuery(name = "getAllProductCatalog", query = "SELECT p FROM ProductCatalog p")
public class ProductCatalog extends Template implements Serializable {    
     
    @ManyToMany
    @JoinTable(
        joinColumns=@JoinColumn(referencedColumnName="ID"),
        inverseJoinColumns=@JoinColumn(name="CONF_ID", referencedColumnName="ID")
    )
    protected List<Configuration> configurations;
    
    public ProductCatalog() {
    }

    public ProductCatalog(String name, String description) {
        this.name = name;
        this.description = description;
        this.configurations = new ArrayList<>();
    }
    
    public ProductCatalog(String name, String description, String baseVersion, List<Configuration> configuration) {
        this.name = name;
        this.description = description;
        this.configurations = configuration;
    }
    
    public void addConfiguration(Configuration configuration){
        configurations.add(configuration);
    }
 
    public Configuration removeConfiguration(Configuration configuration){
        configurations.remove(configuration);
        return configuration;
    }
    
    public List<Configuration> getConfigurations() {
        return configurations;
    }

    public void setConfigurations(List<Configuration> configurations) {
        this.configurations = configurations;
    }
}
