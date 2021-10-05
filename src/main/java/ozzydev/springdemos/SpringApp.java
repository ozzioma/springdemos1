package ozzydev.springdemos;

import com.github.javafaker.Faker;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ozzydev.springdemos.models.mysql.DemoCustomer;
import ozzydev.springdemos.models.postgres.Product;
import ozzydev.springdemos.models.postgres.ProductCategory;
import ozzydev.springdemos.repos.mysql.CommitLogRepo;
import ozzydev.springdemos.repos.mysql.CustomerRepo;
import ozzydev.springdemos.repos.mysql.TxnRepo;
import ozzydev.springdemos.repos.postgres.ProductCategoryRepo;
import ozzydev.springdemos.repos.postgres.ProductRepo;

import javax.sql.DataSource;


@SpringBootApplication
@ServletComponentScan
public class SpringApp implements CommandLineRunner
{

//    @Autowired
//    DataSource dataSource;

    @Autowired
    private CustomerRepo customerRepo;

    @Autowired
    private CommitLogRepo logRepo;

    @Autowired
    private TxnRepo txnRepo;

    @Autowired
    private ProductCategoryRepo productCategoryRepo;

    @Autowired
    private ProductRepo productRepo;

    @Autowired
    private ServiceDemo serviceDemo;

    public static void main(String[] args)
    {
        SpringApplication.run(SpringApp.class, args);
    }


    @Override
    public void run(String... args) throws Exception
    {

        //System.out.println("DATASOURCE = " + dataSource);

        // If you want to check the HikariDataSource settings
        //HikariDataSource newds = (HikariDataSource)dataSource;
        //System.out.println("DATASOURCE = " + newds.getMaximumPoolSize());

        //LoadCustomers();

        //LoadProducts();

        //testQueries();
        //serviceDemo.testQueries();
        serviceDemo.testQueries2();


    }

    @Transactional
    void testQueries()
    {

        Gson gson = new Gson();

        Specification<DemoCustomer> nameLike =
                (root, query, criteriaBuilder) ->
                        criteriaBuilder.like(root.get("firstName"), "%De%");
        var data = customerRepo.findAll(nameLike);
        if (data.isEmpty())
        {
            System.out.println("no customers found");
        }
        else
        {
            System.out.println("total customers found->" + data.size());
            data.forEach(customer -> System.out.println(gson.toJson(customer)));
            //data.forEach(customer -> System.out.println(customer.toString()));
//            for (var customer : data)
//            {
//                System.out.println(customer);
//            }
        }

        //Iterable<DemoCustomer> customers= customerRepo.findAll();
        //System.out.println(customers);


        //Iterable<DemoCommitLog> logs= logRepo.findByProcessedFlag(false);
        //System.out.println(logs);
    }


    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void LoadCustomers()
    {
        Faker faker = new Faker();

        if (customerRepo.count() > 0)
        {
            return;
        }

        for (int count = 1; count < 100; count++)
        {
            DemoCustomer customer = new DemoCustomer();
            customer.setAddress(faker.address().fullAddress());
            customer.setEmail(faker.internet().emailAddress());
            customer.setPhone(faker.phoneNumber().phoneNumber());
            customer.setFirstName(faker.name().firstName());
            customer.setLastName(faker.name().lastName());

            customerRepo.save(customer);
        }

    }


    @Transactional
    void LoadProducts()
    {
        if (productCategoryRepo.count() > 0)
        {
            return;
        }


        Faker faker = new Faker();


        for (int count = 0; count <= 10; count++)
        {
            String categoryName = faker.commerce().material();
            var checkCatName = productCategoryRepo.existsByName(categoryName);
            if (!checkCatName)
            {

                ProductCategory category = new ProductCategory();
                category.setName(categoryName);
                category.setDescription(faker.lorem().paragraph(1));

                productCategoryRepo.save(category);

                for (int count2 = 0; count2 <= 20; count2++)
                {
                    String productName = faker.commerce().productName();
                    Boolean checkProdName = productRepo.existsByName((productName));

                    if (!checkProdName)
                    {
                        Product product = new Product();
                        product.setCategoryId(category.getId());
                        product.setName(productName);
                        product.setCode(faker.code().isbn10());
                        product.setSalesPrice((double) faker.number().numberBetween(10, 55));
                        product.setPurchasePrice((double) faker.number().numberBetween(5, 35));
                        product.setDescription(faker.lorem().paragraph(1));

                        productRepo.save(product);
                    }

                }

            }

        }

    }
}

