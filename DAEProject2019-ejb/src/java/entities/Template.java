package entities;

import java.io.Serializable;
import java.util.List;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.validation.constraints.NotNull;

@MappedSuperclass
public abstract class Template implements Serializable {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    protected Long id;
    
    @NotNull(message = "Template must have a name")
    protected String name;
    
    @NotNull(message = "Template must have a description")
    protected String description;
                
    abstract void  addConfiguration(Configuration configuration);
    abstract Configuration removeConfiguration(Configuration configuration);
    abstract List<Configuration> getConfigurations();
    abstract void setConfigurations(List<Configuration> configurations);
 
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
}
