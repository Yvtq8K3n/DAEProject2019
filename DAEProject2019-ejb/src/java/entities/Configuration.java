/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

/**
 *
 * @author Joao Marquez
 */
@Entity
@Table(name="Configurations")
public class Configuration implements Serializable {
    
    public enum Status {
        ACTIVE,INACTIVE,SUSPEND
    }

    @Id
    @Column(name="ID")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    
    @NotNull(message = "Template must have a description")
    private String description;
    
    @Enumerated(EnumType.STRING)
    private Status status;
    
    @NotNull(message = "Configuration must have base version")
    private String baseVersion;
    
    @NotNull(message = "Configuration must have base Contract Data")
    private String contractData;
    
    @ManyToMany(mappedBy="configurations")
    private List<Product> products;
    
    @ManyToMany(mappedBy="configurations")
    private List<ProductCatalog> productsCatalog;
    
    @OneToMany(cascade=CascadeType.REMOVE)
    private List<Module> modules;
    
    @OneToMany(mappedBy = "configuration", cascade=CascadeType.REMOVE)
    private List<Comment> comments;
    
    @ElementCollection(targetClass=String.class)
    private List<String> hardware;
    
    @ElementCollection(targetClass=String.class)
    private List<String> cloudServices;
    
    @ElementCollection(targetClass=String.class)
    private List<String> activeLicences;
    
    @ElementCollection(targetClass=String.class)
    private List<String> params;
    
    @ElementCollection(targetClass=String.class)
    private List<String> extensions;
    
    @Column(name="demo") //Set column name
    @ElementCollection(targetClass=String.class)
    private List<String> demo;
    
    
    

    public Configuration(){ 
         modules = new ArrayList<>();
         products= new ArrayList<>();
         productsCatalog= new ArrayList<>();
         comments =  new ArrayList<>();
         hardware = new ArrayList<>();
         cloudServices = new ArrayList<>();
         activeLicences = new ArrayList<>();
         params = new ArrayList<>();
         extensions = new ArrayList<>();
         demo = new ArrayList<>();
    }

    public Configuration(String description, Status status, String baseVersion, String contractData) {
        this();
        this.description = description;
        this.status = status;
        this.baseVersion = baseVersion;
        this.contractData = contractData;
    }
    
    
    
    public void addModule(Module module){
        modules.add(module);
    }
    
    public void rmeoveModule(Module module){
        modules.remove(module);
    }
    
    public void addComment(Comment comment){
        comments.add(comment);
    }
    
     public void removeComment(Comment comment){
        comments.remove(comment);
    }
     
    public void addProduct(Product product){
        this.products.add(product);
    }
    
    public void removeProduct(Product product){
        this.products.remove(product);
    }
         
    public void addHardware(String hardware){
        this.hardware.add(hardware);
    }
    
    public void removeHardware(String hardware){
        this.hardware.remove(hardware);
    }
    
    public void addCloudService(String cloudService){
        this.cloudServices.add(cloudService);
    }
    
    public void removeCloudService(String cloudService){
        this.cloudServices.remove(cloudService);
    }
    
    public void addActiveLicence(String activeLicence){
        this.activeLicences.add(activeLicence);
    }
    
    public void removeActiveLicence(String activeLicence){
        this.activeLicences.remove(activeLicence);
    }
    
    public void addParam(String param){
        this.params.add(param);
    }
    
    public void removeParam(String param){
        this.params.remove(param);
    }
    
    public void addExtension(String extension){
        this.extensions.add(extension);
    }
    
    public void removeExtension(String extension){
        this.extensions.remove(extension);
    }
    
    public void addDemo(String demo){
        this.demo.add(demo);
    }
    
    public void removeDemo(String demo){
        this.demo.remove(demo);
    }
    
     //GETTERS AND SETTERS

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public String getBaseVersion() {
        return baseVersion;
    }

    public void setBaseVersion(String baseVersion) {
        this.baseVersion = baseVersion;
    }

    public String getContractData() {
        return contractData;
    }

    public void setContractData(String contractData) {
        this.contractData = contractData;
    }
    
}
