package entities;

import java.io.Serializable;
import java.util.List;
import javax.persistence.ElementCollection;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.validation.constraints.NotNull;

@MappedSuperclass
public abstract class Software implements Serializable {

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    protected Long id;
    
    @NotNull(message = "Template must have a name")
    protected String name;
    
    @NotNull(message = "Template must have a description")
    protected String description;
    
    @ElementCollection(targetClass=String.class)
    protected List<String> hardware;
    
    @ElementCollection(targetClass=String.class)
    protected List<String> extensions;
    
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

    public List<String> getHardware() {
        return hardware;
    }

    public void setHardware(List<String> hardware) {
        this.hardware = hardware;
    }

    public List<String> getExtensions() {
        return extensions;
    }

    public void setExtensions(List<String> extensions) {
        this.extensions = extensions;
    }
}
