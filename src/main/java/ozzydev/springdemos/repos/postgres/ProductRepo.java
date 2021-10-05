package ozzydev.springdemos.repos.postgres;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ozzydev.springdemos.models.postgres.Product;
import ozzydev.springdemos.models.postgres.ProductCategory;

@Repository
public interface ProductRepo extends JpaRepository<Product, Long>
{
    Boolean existsByCode(String code);
    Boolean existsByName(String name);
}


