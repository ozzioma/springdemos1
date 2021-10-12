package ozzydev.springdemos.repos.postgres;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import ozzydev.springdemos.models.postgres.Product;
import ozzydev.springdemos.query.JpaCrudRepository;

import javax.persistence.EntityManager;

@Component
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
    public ProductRepo2(@Qualifier("secondaryEm") EntityManager entityManager)
    {
        super(Product.class, entityManager);
        //this.em=em;
        //super(Product.class);
        //super.entityManager=entityManager2;
    }

}
