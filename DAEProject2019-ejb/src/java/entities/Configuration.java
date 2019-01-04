package entities;

import java.io.Serializable;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDate;
import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "Configuration")
@NamedQuery(
    name = "getAllConfigurations",
    query = "SELECT c FROM Configuration c ORDER BY c.name"
)
public class Configuration extends Software implements Serializable {    
    
    public enum Status {
        ACTIVE,
        INACTIVE,
        SUSPEND;
    }

    @ManyToOne
    @JoinColumn(name = "CLIENT_CODE")
    private @Getter @Setter Client owner;
    
    @Enumerated(EnumType.STRING)
    private @Getter @Setter Status status;
    private @Getter @Setter String baseVersion;
    
    private @Getter @Setter LocalDate contractDate;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval=true)
    private @Getter @Setter List<Module> modules;
    
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval=true)
    private @Getter @Setter List<Parameter> parameters;
    
    @OneToMany(cascade=CascadeType.ALL, orphanRemoval=true)
    private @Getter @Setter List<Artifact> artifacts;
    
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval=true)
    private @Getter @Setter List<Comment> comments;

    public Configuration() {
        this.modules = new ArrayList<>();
        this.artifacts = new ArrayList<>();
        this.comments = new ArrayList<>();
        this.parameters = new ArrayList<>();
    }

    public Configuration(String name, String description, Status status, String baseVersion, Client client, LocalDate contractDate) {
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
}
