package ozzydev.springdemos.transaction;

import javax.persistence.EntityManager;

@FunctionalInterface
public interface TransactionCallback2<R>
{

    public R execute(EntityManager t);

}
