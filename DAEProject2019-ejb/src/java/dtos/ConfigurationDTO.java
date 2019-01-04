/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dtos;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Olek
 */
@XmlRootElement(name = "Configuration")
@XmlAccessorType(XmlAccessType.FIELD)
public class ConfigurationDTO extends TemplateDTO implements Serializable{
    
    public enum Status {
        ACTIVE,
        INACTIVE,
        SUSPEND;
    }
    
    private String baseVersion;
    private String owner;
    private Status status;
    private LocalDate contractDate;

    public ConfigurationDTO(){
    }
  
    public ConfigurationDTO(Long id, String name, String description, String baseVersion, String owner, Status status, LocalDate contractDate){
        super(id, name, description);
        this.baseVersion = baseVersion;
        this.owner = owner;
        this.status = status;
        this.contractDate = contractDate;
    }

    @Override
    public void reset() {
        super.reset();
        setBaseVersion(null);
        setOwner(null);
        setStatus(null);
        setContractDate(null);
    }

    public String getBaseVersion() {
        return baseVersion;
    }

    public void setBaseVersion(String baseVersion) {
        this.baseVersion = baseVersion;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public LocalDate getContractDate() {
        return contractDate;
    }

    public void setContractDate(String LocalDate) {
        this.contractDate = contractDate;
    }
    
    public List<String> getAllStatus(){
        return Stream.of(Status.values())
            .map(Enum::name).collect(Collectors.toList());
    }
}
