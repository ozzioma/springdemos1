package ozzydev.springdemos.models.postgres;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.experimental.FieldNameConstants;
import org.hibernate.annotations.Where;
//import org.teiid.spring.annotations.SelectQuery;
import ozzydev.springdemos.config.GsonIgnore;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Collection;

@GsonIgnore
@Data
@FieldNameConstants
@Entity
@Table(name = "productcategory")
//@Where(clause = "address like '%MD%'")
//@SelectQuery("select * from products.ProductCategory")
public class ProductCategory
{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long id;

    @NotNull
    @Column(name = "name", length = 255, unique = true)
    private String name;

    @NotNull
    @Column(name = "description", length = 255)
    private String description;

    //@JsonIgnore
    //@GsonIgnore
    @OneToMany(mappedBy = "category", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private Collection<Product> products;

}

