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
@XmlRootElement(name = "CommentCustom")
@XmlAccessorType(XmlAccessType.FIELD)
public class CommentCustomDTO extends CommentDTO implements Serializable{
    
    private Boolean replyActive;
    
    public CommentCustomDTO(){
    }

    public CommentCustomDTO(Long id, Long parent, List<CommentCustomDTO> child, Long configuration, String message, String author) {
        super(id, parent, (List<CommentDTO>) (List<? extends CommentDTO>)child, configuration, message, author);
        this.replyActive = false;
    }  

    public void reset() {
       super.reset();
       this.setReplyActive(false);
    }

    public Boolean getReplyActive() {
        return replyActive;
    }

    public void setReplyActive(Boolean replyActive) {
        this.replyActive = replyActive;
    }
    
    

}
