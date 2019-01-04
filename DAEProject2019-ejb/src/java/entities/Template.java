package entities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "Template")
@NamedQuery(name = "getAllTemplates", query = "SELECT t FROM Template t")
public class Template extends Software implements Serializable {    
    
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval=true)
    private @Getter @Setter List<Module> modules;
            
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval=true)
    private @Getter @Setter List<Artifact> artifacts;
    
    public Template() {
        this.modules = new ArrayList<>();
        this.artifacts = new ArrayList<>();
    }

    public Template(String name, String description) {
        this();
        this.name = name;
        this.description = description;     
    }
    
    public void addModule(Module module) {
       modules.add(module);
    }
    public Module removeModule(Module module) {
        modules.remove(module);
        return module;
    }
    
    public void addArtifact(Artifact artifact) {
       artifacts.add(artifact);
    }
    public Artifact removeArtifact(Artifact artifact) {
        artifacts.remove(artifact);
        return artifact;
    }
}
