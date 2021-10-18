package ozzydev.springdemos.transaction;

public class TransactionScopeException extends RuntimeException
{

    private static final long serialVersionUID = 1L;

    public TransactionScopeException(Throwable throwable)
    {
        super(throwable);
    }

}


