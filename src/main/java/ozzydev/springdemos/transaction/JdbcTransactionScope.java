package ozzydev.springdemos.transaction;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

public class JdbcTransactionScope implements TransactionScope<Connection>
{

    private final DataSource dataSource;

    public JdbcTransactionScope(DataSource dataSource)
    {
        this.dataSource = dataSource;
    }


    @Override
    public <R> R executeAndReturn(TransactionCallback<Connection, R> callback) throws TransactionScopeException
    {
        Connection connection = null;
        try
        {

            connection = dataSource.getConnection();

            connection.setAutoCommit(false);
            R result = callback.execute(connection);
            connection.commit();

            return result;

        }
        catch (Exception e)
        {
            if (connection != null)
            {
                try
                {
                    connection.rollback();
                }
                catch (SQLException e2)
                {
                    e2.printStackTrace();
                }
            }
            throw new TransactionScopeException(e);
        }
        finally
        {
            if (connection != null)
            {
                try
                {
                    connection.close();
                }
                catch (SQLException e)
                {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public void execute(TransactionVoidCallback<Connection> callback) throws TransactionScopeException
    {
        Connection connection = null;
        try
        {

            connection = dataSource.getConnection();

            connection.setAutoCommit(false);
            callback.execute(connection);
            connection.commit();

        }
        catch (Exception e)
        {
            if (connection != null)
            {
                try
                {
                    connection.rollback();
                }
                catch (SQLException e2)
                {
                    e2.printStackTrace();
                }
            }
            throw new TransactionScopeException(e);
        }
        finally
        {
            if (connection != null)
            {
                try
                {
                    connection.close();
                }
                catch (SQLException e)
                {
                    e.printStackTrace();
                }
            }
        }
    }
}
