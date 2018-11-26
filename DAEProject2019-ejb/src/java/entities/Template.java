package entities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@Table(name = "Templates")
@NamedQuery(name = "getAllTemplates", query = "SELECT t FROM Template t")
public class Template implements Serializable {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;
    
    @NotNull(message = "Template must have a name")
    private String name;
    
    @NotNull(message = "Template must have a description")
    private String description;
    

    @ManyToMany
    @JoinTable(
        joinColumns=@JoinColumn(referencedColumnName="ID"),
        inverseJoinColumns=@JoinColumn(name="CONF_ID", referencedColumnName="ID")
    )
    private List<Configuration> configurations;
    
    public Template() {
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
    
    public void addConfiguration(Configuration configuration){
        configurations.add(configuration);
    }
 
    public Configuration removeConfiguration(Configuration configuration){
        configurations.remove(configuration);
        return configuration;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public List<Configuration> getConfigurations() {
        return configurations;
    }

    public void setConfigurations(List<Configuration> configurations) {
        this.configurations = configurations;
    }
}
