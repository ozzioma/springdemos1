package ozzydev.springdemos.transaction;

import org.hibernate.SessionFactory;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;

public class JpaTransactionScope implements TransactionScope<EntityManager>
{

    private final EntityManagerFactory factory;

    public JpaTransactionScope(EntityManagerFactory factory)
    {
        this.factory = factory;
    }


    protected SessionFactory getSessionFactory()
    {
        return factory.unwrap(SessionFactory.class);
    }

    //    @Override
    //    public <R> R executeAndReturn2(TransactionCallback2<R> callback) throws TransactionScopeException
    //    {
    //        EntityManager entityManager = null;
    //        EntityTransaction tx = null;
    //
    //        try
    //        {
    //            entityManager = factory.createEntityManager();
    //            tx = entityManager.getTransaction();
    //
    //            tx.begin();
    //            R result = callback.execute(entityManager);
    //            tx.commit();
    //
    //            return result;
    //
    //        }
    //        catch (Exception e)
    //        {
    //            if (tx != null && tx.isActive())
    //            {
    //                tx.rollback();
    //            }
    //            throw new TransactionScopeException(e);
    //        }
    //        finally
    //        {
    //            if (entityManager != null)
    //            {
    //                entityManager.close();
    //            }
    //        }
    //    }

    @Override
    public <R> R executeAndReturn(TransactionCallback<EntityManager, R> callback) throws TransactionScopeException
    {
        EntityManager entityManager = null;
        EntityTransaction tx = null;

        try
        {
            entityManager = factory.createEntityManager();
            tx = entityManager.getTransaction();
            //if (!tx.isActive())
            if (!entityManager.isJoinedToTransaction())
                tx.begin();

            R result = callback.execute(entityManager);
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
            if (entityManager != null)
            {
                entityManager.close();
            }
        }
    }


    @Override
    public void execute(TransactionVoidCallback<EntityManager> callback) throws TransactionScopeException
    {

        EntityManager entityManager = null;
        EntityTransaction tx = null;

        try
        {

            entityManager = factory.createEntityManager();
            tx = entityManager.getTransaction();

            //if (!tx.isActive())
            if (!entityManager.isJoinedToTransaction())
                tx.begin();

            callback.execute(entityManager);
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
            if (entityManager != null)
            {
                entityManager.close();
            }
        }
    }


}


