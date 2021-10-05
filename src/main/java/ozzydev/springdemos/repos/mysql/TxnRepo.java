package ozzydev.springdemos.repos.mysql;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ozzydev.springdemos.models.mysql.DemoTxn;

@Repository
public interface TxnRepo extends JpaRepository<DemoTxn, Long>
{



}