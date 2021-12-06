package ozzydev.springdemos.transaction;

@FunctionalInterface
public interface TransactionVoidCallback<T>
{

    void execute(T t);

}


