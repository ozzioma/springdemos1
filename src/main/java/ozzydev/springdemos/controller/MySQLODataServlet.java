package ozzydev.springdemos.controller;

import com.sap.olingo.jpa.metadata.api.JPAEdmProvider;
import com.sap.olingo.jpa.processor.core.api.JPAODataCRUDHandler;
import com.sap.olingo.jpa.processor.core.api.JPAODataServiceContext;
import org.apache.olingo.commons.api.edmx.EdmxReference;
import org.apache.olingo.commons.api.ex.ODataException;
import org.apache.olingo.server.api.OData;
import org.apache.olingo.server.api.ODataHttpHandler;
import org.apache.olingo.server.api.ServiceMetadata;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import java.io.IOException;
import java.util.ArrayList;

import static org.springframework.web.bind.annotation.RequestMethod.*;

@WebServlet(urlPatterns = "/odata/mysql/*", loadOnStartup = 1)
public class MySQLODataServlet extends HttpServlet
{

    @PersistenceContext(unitName = "primary")
    private EntityManager entityManager;

    @Autowired
    @Qualifier("primaryDS")
    private DataSource primaryDS;

    private static final int BUFFER_SIZE = 10;

    @Override
    //@RequestMapping(method = {GET, POST, PATCH, DELETE})
    //@RequestMapping(method = {POST})
    protected void service(HttpServletRequest servletRequest, HttpServletResponse servletResponse) throws ServletException, IOException
    {
        try
        {

//            JPAEdmProvider metadataProvider = new JPAEdmProvider("ozzydev.springdemos.models",
//                    entityManager.getEntityManagerFactory(),
//                    null, new String[]{"ozzydev.springdemos.models"});
//
//            OData odata = OData.newInstance();
//            ServiceMetadata edm = odata.createServiceMetadata(metadataProvider, new ArrayList<EdmxReference>());
//            ODataHttpHandler handler = odata.createHandler(edm);
//            handler.process(servletRequest, servletResponse);


            var dataServiceContext = JPAODataServiceContext.with()
                    .setEntityManagerFactory(entityManager.getEntityManagerFactory())
                    //.setPUnit("primary")
                    .setPUnit("ozzydev.springdemos.models")
                    .setTypePackage(new String[]{"ozzydev.springdemos.models"}).build();

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


