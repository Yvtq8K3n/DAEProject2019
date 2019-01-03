package entities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "Configuration")
@NamedQuery(
    name = "getAllConfigurations",
    query = "SELECT c FROM Configuration c ORDER BY c.name"
)
public class Configuration extends Software implements Serializable {    
    
    public enum Status {
        ACTIVE,INACTIVE,SUSPEND
    }
    
    @NotNull(message = "Product must have a version")
    private String baseVersion;
    
    @ManyToOne
    @JoinColumn(name = "CLIENT_CODE")
    private Client owner;
    
    @Enumerated(EnumType.STRING)
    private Status status;
    
    @NotNull(message = "Configuration must have base Contract Data")
    private String contractDate;
    
    @ElementCollection(targetClass=String.class)
    private List<String> cloudServices;
    
    @ElementCollection(targetClass=String.class)
    private List<String> activeLicences;
    
    @ElementCollection(targetClass=String.class)
    protected List<String> params;
    
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval=true)
    private List<Module> modules;
    
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval=true)
    private List<Parameter> parameters;
    
    @OneToMany(cascade=CascadeType.ALL, orphanRemoval=true)
    private List<Artifact> artifacts;
    
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval=true)
    private List<Comment> comments;

    public Configuration() {
        this.hardware = new ArrayList<>();
        this.extensions = new ArrayList<>();
        this.modules = new ArrayList<>();
        this.cloudServices = new ArrayList<>();
        this.activeLicences = new ArrayList<>();
        this.params = new ArrayList<>();
        this.artifacts = new ArrayList<>();
        this.comments = new ArrayList<>();
        this.parameters = new ArrayList<>();
    }

    public Configuration(String name, String description, Status status, String baseVersion, Client client, String contractDate) {
        this();
        this.name = name;
        this.description = description;
        this.status = status;
        this.baseVersion = baseVersion;
        this.owner = client;
        this.contractDate = contractDate;
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
    
    public void addComment(Comment comment) {
       comments.add(comment);
    }
    public Comment removeComment(Comment comment) {
        comments.remove(comment);
        return comment;
    }
    
    public void addParameter(Parameter parameter) {
       parameters.add(parameter);
    }
    
    public Parameter removeParameter(Parameter parameter) {
        parameters.remove(parameter);
        return parameter;
    }
    

    
    public String getBaseVersion() {
        return baseVersion;
    }
    public void setBaseVersion(String baseVersion) {
        this.baseVersion = baseVersion;
    }

    public Client getOwner() {
        return owner;
    }
    public void setOwner(Client owner) {
        this.owner = owner;
    } 

    public Status getStatus() {
        return status;
    }
    public void setStatus(Status status) {
        this.status = status;
    }

    public String getContractDate() {
        return contractDate;
    }
    public void setContractDate(String contractDate) {
        this.contractDate = contractDate;
    }

    public List<Module> getModules() {
        return modules;
    }
    public void setModules(List<Module> modules) {
        this.modules = modules;
    }

    public List<String> getCloudServices() {
        return cloudServices;
    }
    public void setCloudServices(List<String> cloudServices) {
        this.cloudServices = cloudServices;
    }

    public List<String> getActiveLicences() {
        return activeLicences;
    }
    public void setActiveLicences(List<String> activeLicences) {
        this.activeLicences = activeLicences;
    }

    public List<String> getParams() {
        return params;
    }
    public void setParams(List<String> params) {
        this.params = params;
    }

    public List<Artifact> getArtifacts() {
        return artifacts;
    }
    public void setArtifacts(List<Artifact> artifacts) {
        this.artifacts = artifacts;
    }

    public List<Comment> getComments() {
        return comments;
    }
    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }
    
    
}
