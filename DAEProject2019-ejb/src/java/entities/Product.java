package entities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "Product")
public class Product implements Serializable {
    
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;
    
    @NotNull(message = "Template must have a name")
    private String name;
    
    @NotNull(message = "Template must have a description")
    private String description;
    
    @NotNull(message = "Product must have a version")
    private String baseVersion;
     
    //RELATION
    private List<Configuration> configurations;
    
    protected Product() {
        this.configurations = new ArrayList<>();
    }

    public Product(String name, String description, String baseVersion) {
        this.name = name;
        this.description = description;
        this.baseVersion = baseVersion;
        this.configurations = new ArrayList<>();
    }
    
    public Product(String name, String description, String baseVersion, List<Configuration> configurations) {
        this.name = name;
        this.description = description;
        this.baseVersion = baseVersion;
        this.configurations = configurations;
    }

    public void addConfiguration(Configuration configuration){
        configurations.add(configuration);
    }
 
    public Configuration removeConfiguration(Configuration configuration){
        configurations.remove(configuration);
        return configuration;
    }
}
