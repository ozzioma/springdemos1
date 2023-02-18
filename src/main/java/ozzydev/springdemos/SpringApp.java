package ozzydev.springdemos;

import com.ap.greenpole3.domain.entities.Depository;
import com.github.javafaker.Faker;
import com.google.gson.Gson;
import com.speedment.jpastreamer.application.JPAStreamer;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ozzydev.springdemos.models.mysql.CustomerType;
import ozzydev.springdemos.models.mysql.DemoCustomer;
import ozzydev.springdemos.models.mysql.DemoCustomer$;
import ozzydev.springdemos.models.postgres.Product;
import ozzydev.springdemos.models.postgres.Product$;
import ozzydev.springdemos.models.postgres.ProductCategory;
import ozzydev.springdemos.repos.mysql.CommitLogRepo;
import ozzydev.springdemos.repos.mysql.CustomerRepo;
import ozzydev.springdemos.repos.mysql.TxnRepo;
import ozzydev.springdemos.repos.postgres.ProductCategoryRepo;
import ozzydev.springdemos.repos.postgres.ProductRepo;

import java.time.LocalDate;
import java.time.LocalDateTime;


@SpringBootApplication
@ServletComponentScan
//@EnableTransactionManagement
@Log4j2
public class SpringApp implements CommandLineRunner {

    @Autowired
    private DataSeeder seeder;

    @Autowired
    private QueryDemo1 queryDemo1;

    @Autowired
    @Qualifier("primaryJpaStreamer")
    private JPAStreamer streamer1;


    @Autowired
    @Qualifier("secondaryJpaStreamer")
    private JPAStreamer streamer2;

    public static void main(String[] args) {
        SpringApplication.run(SpringApp.class, args);
    }


    @Override
    public void run(String... args) throws Exception {


        //seeder.LoadCustomers();
//        seeder.LoadProducts();

        //testQueries();
        //serviceDemo.testQueries();
        //serviceDemo.testQueries2();
        //queryDemo1.testQueries3();
        //queryDemo1.testQueries4();
        // queryDemo1.demo111();


        var data1=streamer1.stream(DemoCustomer.class).filter(DemoCustomer$.firstName.contains("ap"));
        data1.forEach(p->log.info("{} {} {}",p.getFirstName(),p.getLastName(),p.getEmail()));

        var data2=streamer2.stream(ProductCategory.class);
        data2.forEach(p->log.info(p.getName()));

        var data3=streamer2.stream(Product.class).filter(Product$.name.containsIgnoreCase("rub"));
        data3.forEach(p->log.info(p.getCategory().getName()));

        Depository depository;


    }


}



