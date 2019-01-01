/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dtos;

import java.io.Serializable;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Joao Marquez
 */
@XmlRootElement(name = "Comment")
@XmlAccessorType(XmlAccessType.FIELD)
public class CommentDTO implements Serializable{
    
    private Long id;
    private Long parent;
    private String message;
    private List<CommentDTO> child;
    private Long configuration;
    private String author;
    
    public CommentDTO(){
    }

    public CommentDTO(Long id, Long parent, List<CommentDTO> child, Long configuration, String message, String author) {
        this.id = id;
        this.message = message;
        this.parent = parent;
        this.child = child;
        this.configuration = configuration;
        this.author = author;
    }  
    
    public void reset() {
       setId(null);
       setMessage(null);
       setParent(null);
       setChild(null);
       setConfiguration(null);
       setAuthor(null);
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

    public Long getParent() {
        return parent;
    }

    public void setParent(Long parent) {
        this.parent = parent;
    }

    public List<CommentDTO> getChild() {
        return child;
    }

    public void setChild(List<CommentDTO> child) {
        this.child = child;
    }

    public Long getConfiguration() {
        return configuration;
    }

    public void setConfiguration(Long configuration) {
        this.configuration = configuration;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }
}
