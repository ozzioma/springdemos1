package ozzydev.springdemos.repos.postgres;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ozzydev.springdemos.models.postgres.Product;
import ozzydev.springdemos.query.JpaCrudRepository;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

//@Component
@Repository
@Transactional(propagation = Propagation.REQUIRED)
public class ProductRepo2 extends JpaCrudRepository<Product, Long>
{

    //@PersistenceContext(unitName = "secondary")
    //private EntityManager em;

    //@Autowired
    //    public ProductRepo2()
    //    {
    //        super(Product.class);
    //        this.entityManager = em;
    //
    //        //super(Product.class);
    //        //super.entityManager=entityManager2;
    //    }

    //@Autowired
    public ProductRepo2(@Qualifier("secondaryEmf") EntityManagerFactory entityManagerFactory)
    {
        super(Product.class, entityManagerFactory);

    }

}
