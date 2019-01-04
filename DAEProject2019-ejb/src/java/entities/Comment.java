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
import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author admin
 */
@Entity
@Table(name ="Comments")
public class Comment implements Serializable{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private @Getter @Setter  Long id;
    
    @NotNull(message = "Message must not be empty")
    private @Getter @Setter String message;
    
    @ManyToOne
    private @Getter @Setter Comment parent;

    @OneToMany(mappedBy="parent", cascade = CascadeType.ALL, orphanRemoval=true)
    private @Getter @Setter List<Comment> children;
    
    @ManyToOne
    @JoinColumn(name="config_id")
    private @Getter @Setter Configuration configuration;
    
    @ManyToOne
    @JoinColumn(name="author_id")
    private @Getter @Setter User author;

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
   
    public void addChildren(Comment children){
        this.children.add(children);
    }   
    public void removeChildren(Comment children){
        this.children.remove(children);
    }
}
