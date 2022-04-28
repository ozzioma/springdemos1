package ozzydev.springdemos;

import com.github.javafaker.Faker;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
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
import ozzydev.springdemos.models.postgres.Product;
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
public class SpringApp implements CommandLineRunner
{

    @Autowired
    private DataSeeder seeder;

    @Autowired
    private QueryDemo1 queryDemo1;

    public static void main(String[] args)
    {
        SpringApplication.run(SpringApp.class, args);
    }


    @Override
    public void run(String... args) throws Exception
    {



//        seeder.LoadCustomers();
//        seeder.LoadProducts();

        //testQueries();
        //serviceDemo.testQueries();
        //serviceDemo.testQueries2();
        //queryDemo1.testQueries3();
        //queryDemo1.testQueries4();
        queryDemo1.demo111();


    }




}



