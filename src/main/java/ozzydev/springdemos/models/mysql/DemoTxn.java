package ozzydev.springdemos.models.mysql;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Entity
@Data
@Table(name = "demotxn")
public class DemoTxn
{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long id;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "txntype", length = 50)
    private TransactionType txnType;

    @NotNull
    @Column(name = "amount")
    private Double amount;

    @NotNull
    //@Temporal(TemporalType.TIMESTAMP)
    @Column(name = "txndate")
    private LocalDateTime txnDate;

    @Null
    private String description;

    @Null
    private String channel;

    @Null
    private Boolean postedFlag;

    @Null
    @Column(name = "datePosted")
    private LocalDate datePosted;

    @Null
    private Boolean settledFlag;

    @Null
    @Column(name = "dateSettled")
    private LocalDate dateSettled;

    @Null
    @Column(name = "pickupTime")
    private LocalTime pickupTime;

    @NotNull
    @Column(name = "customerid")
    private Long customerId;


    @ManyToOne(cascade = {CascadeType.MERGE, CascadeType.PERSIST}, optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "customerid", insertable = false, updatable = false)
    private DemoCustomer customer;


//    @NotNull
//    //@Column(name = "customerid")
//    //@JoinColumn(name = "customerid")
//    @ManyToOne(optional = false, fetch = FetchType.LAZY)
//    @JoinColumn(name = "customerid", referencedColumnName = "Id", insertable = false, updatable = false)
//    private DemoCustomer customer;

}

