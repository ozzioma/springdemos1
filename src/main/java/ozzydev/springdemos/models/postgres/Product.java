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

@Data
@FieldNameConstants
@Entity
@Table(name = "product")
//@SelectQuery("select * from products.Product")
public class Product
{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long id;

    @NotNull
    @Column(name = "code", length = 32, unique = true)
    private String code;

    @NotNull
    @Column(name = "name", length = 255, unique = true)
    private String name;

    @NotNull
    @Column(name = "salesPrice", precision = 4)
    private Double salesPrice;

    @NotNull
    @Column(name = "vendorPrice", precision = 4)
    private Double purchasePrice;

    @NotNull
    @Column(name = "description", length = 255)
    private String description;

    @NotNull
    @Column(name = "categoryId")
    private Long categoryId;

   // @JsonIgnore
    //@GsonIgnore
    //@ManyToOne(cascade = {CascadeType.MERGE, CascadeType.PERSIST}, optional = false, fetch = FetchType.LAZY)
    @ManyToOne(cascade = {CascadeType.MERGE}, optional = true, fetch = FetchType.LAZY)
    @JoinColumn(name = "categoryId", insertable = false, updatable = false)
    private ProductCategory category;
}
