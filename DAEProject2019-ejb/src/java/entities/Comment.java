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
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

/**
 *
 * @author admin
 */
@Entity
@Table(name ="Comments")
public class Comment implements Serializable{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    
    @NotNull(message = "Message must not be empty")
    String message;
    
    @ManyToOne
    private Comment parent;

    @OneToMany(mappedBy="parent", cascade = CascadeType.ALL, orphanRemoval=true)
    private List<Comment> children;
    
    @ManyToOne
    @JoinColumn(name="config_id")
    private Configuration configuration;
    
    @ManyToOne
    @JoinColumn(name="author_id")
    private User author;

    public Comment() {
        this.children = new ArrayList<>();
    }
    
    public Comment(Comment parent, Configuration configuration, String message, User author) {
        this();
        this.parent = parent;
        this.message = message;
        this.configuration=configuration;
        this.author = author;
    }  
    
    public void setParent(Comment parent){
        this.parent = parent;
    }
    
    public void addChildren(Comment children){
        this.children.add(children);
    }
    
    public void removeChildren(Comment children){
        this.children.remove(children);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<Comment> getChildren() {
        return children;
    }

    public void setChildren(List<Comment> children) {
        this.children = children;
    }
    
    public Configuration getConfiguration() {
        return configuration;
    }

    public void setConfiguration(Configuration configuration) {
        this.configuration = configuration;
    }

    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
    }

    public Comment getParent() {
        return parent;
    }
}
