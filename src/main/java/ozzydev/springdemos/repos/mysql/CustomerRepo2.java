package ozzydev.springdemos.repos.mysql;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import ozzydev.springdemos.models.mysql.DemoCustomer;
import ozzydev.springdemos.query.JpaCrudRepository;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import java.util.List;
import java.util.Optional;

@Component
public class CustomerRepo2 extends JpaCrudRepository<DemoCustomer, Long>
{

    public CustomerRepo2(@Qualifier("primaryEmf") EntityManagerFactory entityManagerFactory)
    {
        super(DemoCustomer.class, entityManagerFactory);
    }


}
