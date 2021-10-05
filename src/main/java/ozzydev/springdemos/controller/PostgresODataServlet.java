package ozzydev.springdemos.controller;

import com.sap.olingo.jpa.processor.core.api.JPAODataCRUDHandler;
import com.sap.olingo.jpa.processor.core.api.JPAODataServiceContext;
import org.apache.olingo.commons.api.ex.ODataException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import java.io.IOException;

@WebServlet(urlPatterns = "/odata/postgres/*", loadOnStartup = 1)
public class PostgresODataServlet extends HttpServlet
{
    @PersistenceContext(unitName = "secondary")
    private EntityManager entityManager;

    @Autowired
    @Qualifier("secondaryDS")
    private DataSource secondaryDS;

    @Override
  //protected void doGet(HttpServletRequest servletRequest, HttpServletResponse servletResponse) throws ServletException, IOException
    protected void service(HttpServletRequest servletRequest, HttpServletResponse servletResponse) throws ServletException, IOException
    {
        try
        {

            var dataServiceContext = JPAODataServiceContext.with()
                    .setEntityManagerFactory(entityManager.getEntityManagerFactory())
                    //.setPUnit("primary")
                    .setPUnit("ozzydev.springdemos.models.postgres")
                    //.setPUnit("Ozzy.CodePad.Console")
                    .setTypePackage(new String[]{"ozzydev.springdemos.models.postgres"})
                    .build();

            JPAODataCRUDHandler handler = new JPAODataCRUDHandler(dataServiceContext);
            handler.process(servletRequest, servletResponse);

        }
        catch (RuntimeException e)
        {
            throw new ServletException(e);
        }
        catch (ODataException e)
        {
            throw new ServletException(e);
        }

    }

}

