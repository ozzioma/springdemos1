package ozzydev.springdemos.models.mysql;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sap.olingo.jpa.metadata.core.edm.annotation.EdmEnumeration;
import lombok.Data;
import lombok.experimental.FieldNameConstants;
import org.hibernate.annotations.Where;
import org.springframework.format.annotation.DateTimeFormat;
import ozzydev.springdemos.config.LocalDateConverter;
import ozzydev.springdemos.config.LocalDateTimeConverter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@Entity
@Data
@FieldNameConstants
@Table(name = "democustomer")
//@Where(clause = "address like '%MD%'")
public class DemoCustomer
{

    public DemoCustomer()
    {
        //super();
        transactions = new HashSet<>();
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long id;

    @NotNull
    @Column(name = "firstname", length = 255)
    private String firstName;

    @NotNull
    @Column(name = "lastname", length = 255)
    private String lastName;

    @NotNull
    @Column(name = "email", length = 255, unique = true)
    private String email;

    @NotNull
    @Column(name = "phone", length = 50, unique = true)
    private String phone;

    //@Null
    @Column(name = "address", length = 512)
    private String address;

    //@NotNull
    @Column(name = "dob")
    @Convert(converter = LocalDateConverter.class)
    //@DateTimeFormat(pattern = "dd-MM-yyyy")
    //@JsonFormat(pattern = "dd-MM-yyyy")
    private LocalDate dob;

    //@NotNull
    @Column(name = "regDate")
    @Convert(converter = LocalDateTimeConverter.class)
    //@DateTimeFormat(pattern = "dd-MM-yyyy hh:mm:ss")
    //@JsonFormat(pattern = "dd-MM-yyyy hh:mm:ss")
    private LocalDateTime registrationDate;

    //@NotNull
    @Column(name = "isRegCompleted")
    private Boolean isRegistrationCompleted;

    //@NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "customertype", length = 50)
    private CustomerType customerType;

    //@JsonIgnore
    @OneToMany(mappedBy = "customer", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<DemoTxn> transactions;
}
