package ozzydev.springdemos.repos.postgres;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ozzydev.springdemos.models.postgres.ProductMasterView;

@Repository
public interface ProductMasterViewRepo extends JpaRepository<ProductMasterView, Long> {
    Boolean existsByCode(String code);

    Boolean existsByName(String name);
}
