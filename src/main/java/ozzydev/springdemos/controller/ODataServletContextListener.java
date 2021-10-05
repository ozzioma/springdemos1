//package ozzydev.springdemos.controller;
//
//import org.apache.olingo.commons.api.ex.ODataException;
//import org.springframework.beans.factory.annotation.Autowired;
//
//import javax.persistence.Access;
//import javax.persistence.EntityManager;
//import javax.persistence.PersistenceContext;
//import javax.servlet.ServletContextEvent;
//import javax.servlet.ServletContextListener;
//import javax.sql.DataSource;
//
//public class ODataServletContextListener implements ServletContextListener
//{
//    private static final String PUNIT_NAME = "primary";
//    @PersistenceContext(unitName = "primary")
//    private EntityManager entityManager;
//
//    @Autowired
//    private DataSource primaryDS;
//
//    @Override
//    public void contextInitialized(ServletContextEvent servletContextEvent)
//    {
//        System.out.println("MyServletContextListener Context Initialized");
//
//        //final DataSource ds = DataSourceHelper.createDataSource(DataSourceHelper.DB_HSQLDB);
//        try
//        {
//            final JPAODataCRUDContextAccess serviceContext = JPAODataServiceContext.with()
//                    .setPUnit(PUNIT_NAME)
//                    .setDataSource(primaryDS)
//                    .setTypePackage("tutorial.operations", "tutorial.model")
//                    .build();
//
//            servletContextEvent.getServletContext().setAttribute("ServiceContext", serviceContext);
//        }
//        catch (ODataException ex)
//        {
//            ex.printStackTrace();
//        }
//
//    }
//
//    @Override
//    public void contextDestroyed(ServletContextEvent servletContextEvent)
//    {
//        System.out.println("MyServletContextListener Context Destroyed");
//        servletContextEvent.getServletContext().setAttribute("ServiceContext", null);
//    }
//
//}