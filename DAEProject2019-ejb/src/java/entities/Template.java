package entities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "Template")
@NamedQuery(name = "getAllTemplates", query = "SELECT t FROM Template t")
public class Template extends Software implements Serializable {    
    
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval=true)
    private List<Module> modules;
            
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval=true)
    private List<Artifact> artifacts;
    
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval=true)
    private List<SupportMaterial> supportMaterials;
    
    public Template() {
        this.hardware = new ArrayList<>();
        this.extensions = new ArrayList<>();
        this.modules = new ArrayList<>();
        this.supportMaterials = new ArrayList<>();
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
    
    public List<Module> getModule() {
        return modules;
    }
    public void setModule(List<Module> modules) {
        this.modules = modules;
    }

    public List<Module> getModules() {
        return modules;
    }

    public void setModules(List<Module> modules) {
        this.modules = modules;
    }

    public List<Artifact> getArtifacts() {
        return artifacts;
    }

    public void setArtifacts(List<Artifact> artifacts) {
        this.artifacts = artifacts;
    }
}
