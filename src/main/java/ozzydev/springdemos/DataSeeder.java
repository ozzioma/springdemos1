package ozzydev.springdemos;

import com.github.javafaker.Faker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
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

@Component
public class DataSeeder
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


    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void LoadCustomers()
    {
        Faker faker = new Faker();

        if (customerRepo.count() > 0)
        {
            return;
        }

        for (int count = 0; count <= 500; count++)
        {
            DemoCustomer customer = new DemoCustomer();
            customer.setAddress(faker.address().fullAddress());
            customer.setEmail(faker.internet().emailAddress());
            customer.setPhone(faker.phoneNumber().phoneNumber());
            customer.setFirstName(faker.name().firstName());
            customer.setLastName(faker.name().lastName());
            customer.setIsRegistrationCompleted(faker.random().nextBoolean());

            customer.setCustomerType(faker.options().option(CustomerType.class));
            customer.setDob(LocalDate.of(
                    faker.random().nextInt(1960, 2010),
                    faker.random().nextInt(1, 12),
                    faker.random().nextInt(1, 28)));

            customer.setRegistrationDate(LocalDateTime.of(
                    faker.random().nextInt(2000, 2020),
                    faker.random().nextInt(1, 12),
                    faker.random().nextInt(1, 28),
                    faker.random().nextInt(0, 23),
                    faker.random().nextInt(0, 59)));

            //faker.date().past(20, TimeUnit.DAYS)
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


        for (int count = 0; count <= 20; count++)
        {
            String categoryName = faker.commerce().material();
            var checkCatName = productCategoryRepo.existsByName(categoryName);
            if (!checkCatName)
            {

                ProductCategory category = new ProductCategory();
                category.setName(categoryName);
                category.setDescription(faker.lorem().paragraph(1));

                productCategoryRepo.save(category);

                for (int count2 = 0; count2 <= 50; count2++)
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
