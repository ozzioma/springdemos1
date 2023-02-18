package ozzydev.springdemos.models.postgres;

import com.sap.olingo.jpa.metadata.core.edm.annotation.EdmIgnore;
import lombok.Data;
import lombok.experimental.FieldNameConstants;
//import org.teiid.spring.annotations.SelectQuery;
import ozzydev.springdemos.config.GsonIgnore;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@GsonIgnore
//@EdmIgnore
@Data
@FieldNameConstants
@Entity
@Table(name = "ProductsMasterView")
//@SelectQuery("select * from products.ProductMasterView")
public class ProductMasterView {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long id;

    @NotNull
    @Column(name = "code", length = 32)
    private String code;

    @NotNull
    @Column(name = "name", length = 255)
    private String name;

    @NotNull
    @Column(name = "salesPrice", precision = 4)
    private Double salesPrice;

    @EdmIgnore
    @NotNull
    @Column(name = "vendorPrice", precision = 4)
    private Double purchasePrice;

    @NotNull
    @Column(name = "description", length = 255)
    private String description;

    @NotNull
    @Column(name = "categoryId")
    private Long categoryId;


    @NotNull
    @Column(name = "categoryName", length = 255)
    private String categoryName;

    @NotNull
    @Column(name = "categoryDescription", length = 255)
    private String categoryDescription;
}
