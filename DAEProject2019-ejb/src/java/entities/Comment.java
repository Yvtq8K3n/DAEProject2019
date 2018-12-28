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

    @OneToMany(mappedBy="parent", cascade = CascadeType.ALL)
    private List<Comment> children;
    
    @ManyToOne
    @JoinColumn(name="product_id")
    private Product product;

    public Comment() {
        children = new ArrayList<>();
    }
    
    public Comment(Comment parent, Product configuration, String message) {
        this.parent = parent;
        this.message = message;
        this.product=configuration;
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
    
    public Product getConfiguration() {
        return product;
    }

    public void setConfiguration(Product product) {
        this.product = product;
    }
    
}
