/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author Joao Marquez
 */
@Entity
@Table(name = "USERS_GROUPS")
public class UserGroup{
    
    public enum GROUP{
        Client,
        Administrator;
    }
    
    @Enumerated(EnumType.STRING)
    @Column(name="GROUP_NAME")
    private @Getter @Setter GROUP groupName;
    
    @Id
    @OneToOne
    @JoinColumn(name = "USERNAME")
    private @Getter @Setter User user;

    public UserGroup() {
    }

    public UserGroup(GROUP groupName, User user) {
        this.groupName = groupName;
        this.user = user;
    }
}
