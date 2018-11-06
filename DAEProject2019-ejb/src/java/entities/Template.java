package entities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "Template")
public class Template implements Serializable {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;
    
    @NotNull(message = "Template must have a name")
    private String name;
    
    @NotNull(message = "Template must have a description")
    private String description;
    
    //RELATION
    private List<Configuration> configurations;
    
    protected Template() {
        this.configurations = new ArrayList<>();
    }

    public Template(String name, String description) {
        this.name = name;
        this.description = description;
        this.configurations = new ArrayList<>();
    }
    
    public Template(String name, String description, List<Configuration> configurations) {
        this.name = name;
        this.description = description;
        this.configurations = configurations;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
    
    public void addConfiguration(Configuration configuration){
        configurations.add(configuration);
    }
 
    public Configuration removeConfiguration(Configuration configuration){
        configurations.remove(configuration);
        return configuration;
    }
}
