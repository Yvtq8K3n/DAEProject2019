/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dtos;

import java.io.Serializable;
import java.util.ArrayList;
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
public class ParameterDTO implements Serializable{
    
    private Long id;
    private String title;
    private String description;
    private String status;
    private String baseVersion;
    private String contractData;
    
    public ParameterDTO(){
    }

    public ParameterDTO(Long id,String title, String description, String status, String baseVersion, String contractData) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.status = status;
        this.baseVersion = baseVersion;
        this.contractData = contractData;
    }

    public void reset() {
        setId(null);
        setTitle(null);
        setDescription(null);
        setStatus(null);
        setBaseVersion(null);
        setContractData(null);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
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
