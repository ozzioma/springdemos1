package ozzydev.springdemos.models.mysql;

import lombok.Data;
//import org.teiid.spring.annotations.SelectQuery;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.time.LocalDateTime;
import java.util.Date;
import java.sql.Timestamp;

@Entity
@Data
@Table(name = "demolog")
//@SelectQuery("select * from customers.DemoCommitLog")
public class DemoCommitLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long id;

    @Null
    @Column(name = "txntype",length = 255)
    private String txnType;

    @Null
    @Column(name = "txncode",length = 255)
    private String txnCode;

    @Null
    @Column(name = "txnid",length = 255)
    private String txnId;

    @Null
    @Column(name = "txnref",length = 255)
    private String txnRef;

    @NotNull
    @NotBlank
    @Column(name = "payload",length = 2048)
    private String payload;

    @Null
    @Column(name = "entitytype",length = 255)
    private String entityType;

    @Null
    @Column(name = "entityversion",length = 32)
    private String entityVersion;

    @NotNull
    @Column(name = "msgtype",length = 255)
    private String messageType;

    @Null
    @Column(name = "msgversion",length = 32)
    private String messageVersion;

    @Null
    @Column(name = "proctype",length = 255)
    private String processorType;

    @Null
    @Column(name = "procversion",length = 32)
    private String processorVersion;

    @NotNull
    @Column(name = "eventTime")
    //@Temporal(TemporalType.TIMESTAMP)
    @Past
    private LocalDateTime eventTime;

    @NotNull
    @Column(name = "ingestTime")
    //@Temporal(TemporalType.TIMESTAMP)
    @Past
    private LocalDateTime ingestTime;

    @NotNull
    @Column(name = "processTime")
    @Future
    //@Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime processTime;

    @Null
    @Column(name = "processedflag")
    private Boolean processedFlag;

    @Null
    @Column(name = "retrycount")
    private Integer retryCount;

    @Null
    //@Temporal(TemporalType.TIMESTAMP)
    @Column(name = "lastretry")
    private LocalDateTime lastRetry;

    @Null
    @Column(name = "successmsg",length = 10)
    private String successMsg;

    @Null
    @Column(name = "failmsg",length = 1024)
    private String failMsg;

    @Null
    @Column(length = 1024)
    private String description;

    @Null
    @Column(length = 255)
    private String channel;



}