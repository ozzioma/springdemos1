package ozzydev.springdemos.transaction;

import javax.persistence.EntityManager;


@FunctionalInterface
public interface TransactionCallback<T, R>
{

    public R execute(T t);

}