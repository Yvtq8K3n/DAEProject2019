package entities;

import java.io.Serializable;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@MappedSuperclass
public abstract class Software implements Serializable {

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    protected @Getter @Setter Long id;
    
    @NotNull(message = "Template must have a name")
    protected @Getter @Setter String name;
    
    @NotNull(message = "Template must have a description")
    protected @Getter @Setter String description;
}
