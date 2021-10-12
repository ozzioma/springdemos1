package ozzydev.springdemos.repos.mysql;

import org.springframework.stereotype.Component;
import ozzydev.springdemos.models.mysql.DemoCustomer;
import ozzydev.springdemos.query.JpaCrudRepository;

import java.util.List;
import java.util.Optional;

@Component
public class CustomerRepo2 extends JpaCrudRepository<DemoCustomer, Long>
{

    public CustomerRepo2()
    {
        super(DemoCustomer.class);
    }



}
