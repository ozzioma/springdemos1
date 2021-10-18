package ozzydev.springdemos.transaction;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import javax.persistence.EntityManagerFactory;

public class HibernateTransactionScope<T> implements TransactionScope<Session>
{

    private SessionFactory factory;

    public HibernateTransactionScope(SessionFactory factory)
    {
        this.factory = factory;
    }

    @Override
    public <R> R executeAndReturn(TransactionCallback<Session, R> callback) throws TransactionScopeException
    {
        Session session = null;
        Transaction tx = null;
        try
        {

            session = factory.openSession();
            if (!session.isJoinedToTransaction())
                tx = session.beginTransaction();
            else
                tx = session.getTransaction();

            R result = callback.execute(session);
            tx.commit();

            return result;

        }
        catch (Exception e)
        {
            if (tx != null && tx.isActive())
            {
                tx.rollback();
            }
            throw new TransactionScopeException(e);
        }
        finally
        {
            if (session != null)
            {
                session.close();
            }
        }
    }

    @Override
    public void execute(TransactionVoidCallback<Session> callback) throws TransactionScopeException
    {

        Session session = null;
        Transaction tx = null;
        try
        {

            session = factory.openSession();
            if (!session.isJoinedToTransaction())
                tx = session.beginTransaction();
            else
                tx = session.getTransaction();

            callback.execute(session);
            tx.commit();

        }
        catch (Exception e)
        {
            if (tx != null && tx.isActive())
            {
                tx.rollback();
            }
            throw new TransactionScopeException(e);
        }
        finally
        {
            if (session != null)
            {
                session.close();
            }
        }

    }
}


