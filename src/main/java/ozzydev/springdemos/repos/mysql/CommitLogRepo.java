package ozzydev.springdemos.repos.mysql;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ozzydev.springdemos.models.mysql.DemoCommitLog;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Repository
public interface CommitLogRepo extends JpaRepository<DemoCommitLog, Long>
{

    List<DemoCommitLog> findByMessageType(String messageType);
    List<DemoCommitLog> findByTxnCode(String txnCode);
    List<DemoCommitLog> findByTxnId(String txnId);
    List<DemoCommitLog> findByEventTime(@NotNull @Past LocalDateTime eventTime);
    List<DemoCommitLog> findByProcessedFlag(boolean processedFlag);


}