package entities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "Product")
public class Product extends Template implements Serializable {    
    
    @NotNull(message = "Product must have a version")
    private String baseVersion;
    
    @ManyToOne
    @JoinColumn(name = "CLIENT_CODE")
    private Client owner;
    
    @ManyToMany
    @JoinTable(
        joinColumns=@JoinColumn(referencedColumnName="ID"),
        inverseJoinColumns=@JoinColumn(name="CONF_ID", referencedColumnName="ID")
    )
    protected List<Configuration> configurations;
    
    public Product() {
    }

    public Product(String name, String description, String baseVersion) {
        this.name = name;
        this.description = description;
        this.baseVersion = baseVersion;
        this.configurations = new ArrayList<>();
    }
    
    public Product(String name, String description, String baseVersion, List<Configuration> configuration) {
        this.name = name;
        this.description = description;
        this.baseVersion = baseVersion;
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
