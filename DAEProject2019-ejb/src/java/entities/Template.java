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
@Table(name = "Template")
@NamedQuery(name = "getAllTemplates", query = "SELECT t FROM Template t")
public class Template extends Software implements Serializable {    
    
    @ManyToMany
    @JoinTable(
        joinColumns=@JoinColumn(referencedColumnName="ID"),
        inverseJoinColumns=@JoinColumn(name="CONF_ID", referencedColumnName="ID")
    )
    private List<Module> modules;
            
    @ManyToMany
    @JoinTable(
        joinColumns=@JoinColumn(referencedColumnName="ID"),
        inverseJoinColumns=@JoinColumn(name="CONF_ID", referencedColumnName="ID")
    )
    private List<Artifact> artifacts;
    
    @ManyToMany
    @JoinTable(
        joinColumns=@JoinColumn(referencedColumnName="ID"),
        inverseJoinColumns=@JoinColumn(name="CONF_ID", referencedColumnName="ID")
    )
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
    public List<Module> getModule() {
        return modules;
    }
    public void setModule(List<Module> modules) {
        this.modules = modules;
    }
}
