package ozzydev.springdemos.transaction;

@FunctionalInterface
public interface TransactionVoidCallback<T>
{

    public void execute(T t);

}


