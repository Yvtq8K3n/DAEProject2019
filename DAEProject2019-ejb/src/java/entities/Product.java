package entities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "Product")
public class Product extends Template  implements Serializable {    
    
    @NotNull(message = "Product must have a version")
    private String baseVersion;
    
    protected Product() {
    }

    public Product(String name, String description, String baseVersion) {
        super(name, description, new ArrayList<>());
        this.baseVersion = baseVersion; 
    }
    
    public Product(String name, String description, String baseVersion, List<Configuration> configurations) {
        super(name, description, configurations);
        this.baseVersion = baseVersion; 
    }
}
