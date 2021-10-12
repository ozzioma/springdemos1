package ozzydev.springdemos.repos.mysql;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ozzydev.springdemos.models.mysql.DemoCustomer;
import ozzydev.springdemos.query.JpaCrudRepository;
import ozzydev.springdemos.query.JpaQueryFilterHandler;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

@Repository
public interface CustomerRepo extends JpaRepository<DemoCustomer, Long>, JpaQueryFilterHandler<DemoCustomer>
{

    List<DemoCustomer> findByEmail(String email);

    List<DemoCustomer> findByPhone(String phone);


    // custom query example and return a stream
    @Query("select c from DemoCustomer c where c.email = :email")
    Stream<DemoCustomer> findByEmailReturnStream(@Param("email") String email);

}

