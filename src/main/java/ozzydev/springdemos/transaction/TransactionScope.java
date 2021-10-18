package ozzydev.springdemos.transaction;

import javax.persistence.EntityManager;

public interface TransactionScope<T>
{
   // public <R> R executeAndReturn2(TransactionCallback2<R> callback) throws TransactionScopeException;

    public <R> R executeAndReturn(TransactionCallback<T, R> callback) throws TransactionScopeException;

    public void execute(TransactionVoidCallback<T> callback) throws TransactionScopeException;

}

