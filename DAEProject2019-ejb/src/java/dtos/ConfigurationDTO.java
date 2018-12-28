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
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Olek
 */
@XmlRootElement(name = "Configuration")
@XmlAccessorType(XmlAccessType.FIELD)
public class ConfigurationDTO implements Serializable{
    
    public enum Status {
        ACTIVE,INACTIVE,SUSPEND
    }
    
    private Long id;
    private String title;
    private String description;
    private Status status;
    private String baseVersion;
    private String contractData;
    private List<String> hardware;
    private List<String> cloudServices;
    private List<String> activeLicenses;
    private List<String> params;
    private List<String> extensions;
    private List<String> demo;
}
