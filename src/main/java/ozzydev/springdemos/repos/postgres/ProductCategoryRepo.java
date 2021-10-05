package ozzydev.springdemos.repos.postgres;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ozzydev.springdemos.models.postgres.ProductCategory;

@Repository
public interface ProductCategoryRepo extends JpaRepository<ProductCategory, Long>
{

    Boolean existsByName(String name);
}
