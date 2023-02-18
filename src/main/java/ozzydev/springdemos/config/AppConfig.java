package ozzydev.springdemos.config;

import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.sap.olingo.jpa.processor.core.api.JPAODataPagingProvider;
import com.sap.olingo.jpa.processor.core.api.JPAODataRequestContext;
import com.sap.olingo.jpa.processor.core.api.JPAODataServiceContext;
import com.sap.olingo.jpa.processor.core.api.JPAODataSessionContextAccess;
import com.sap.olingo.jpa.processor.core.api.example.JPAExampleCUDRequestHandler;
import com.speedment.jpastreamer.application.JPAStreamer;
import org.apache.olingo.commons.api.ex.ODataException;
import org.apache.olingo.server.api.debug.DefaultDebugSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
//import springfox.documentation.builders.PathSelectors;
//import springfox.documentation.builders.RequestHandlerSelectors;
//import springfox.documentation.spi.DocumentationType;
//import springfox.documentation.spring.web.plugins.Docket;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Scope;
import org.springframework.orm.jpa.EntityManagerFactoryInfo;
//import org.teiid.spring.autoconfigure.MultiDataSourceTransactionManagement;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceContext;
import javax.sql.DataSource;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;

import static org.springframework.web.context.WebApplicationContext.SCOPE_REQUEST;


@Configuration
public class AppConfig //extends MultiDataSourceTransactionManagement
{

    //@Autowired
    // AutowireCapableBeanFactory beanFactory;

    //    @Bean
    //    public Docket api()
    //    {
    //        return new Docket(DocumentationType.SWAGGER_2)
    //                .select()
    //                .apis(RequestHandlerSelectors.any())
    //                .paths(PathSelectors.any())
    //                .build();
    //    }


    @Bean
    public OpenAPI customOpenAPI(@Value("${application-description}") String appDesciption, @Value("${application-version}") String appVersion) {
        return new OpenAPI()
                .info(new Info()
                        .title("Ozzy API")
                        .version(appVersion)
                        .description(appDesciption)
                        .termsOfService("http://ozzy.io/terms/")
                        .license(new License().name("Apache 2.0").url("http://ozzy.org")));
    }

    private static final String dateFormat = "yyyy-MM-dd";
    private static final String dateFormat2 = "dd-MM-yyyy";
    private static final String dateTimeFormat = "yyyy-MM-dd HH:mm:ss";
    private static final String dateTimeFormat2 = "dd-MM-yyyy HH:mm:ss";

    @Bean
    public Jackson2ObjectMapperBuilderCustomizer jsonCustomizer() {
        return builder ->
        {
            builder.simpleDateFormat(dateTimeFormat2);
            builder.serializers(new LocalDateSerializer(DateTimeFormatter.ofPattern(dateFormat2)));
            builder.serializers(new LocalDateTimeSerializer(DateTimeFormatter.ofPattern(dateTimeFormat2)));
        };
    }

    //    @Bean
    //    public ServletRegistrationBean odataServletBean()
    //    {
    //        ServletRegistrationBean srb = new ServletRegistrationBean();
    //        final MySQLODataServlet servlet = new MySQLODataServlet();
    //        beanFactory.autowireBean(servlet);
    //        srb.setServlet(servlet);
    //        srb.setUrlMappings(Arrays.asList("/odata/mysql/*"));
    //        srb.setLoadOnStartup(1);
    //        return srb;
    //    }

    //    @Bean
    //    public String postresODataServiceContext()
    //    {
    //        var dataServiceContext = JPAODataServiceContext.with()
    //                .setEntityManagerFactory(entityManager.getEntityManagerFactory())
    //                //.setPUnit("primary")
    //                .setPUnit("ozzydev.springdemos.models.postgres")
    //                .setTypePackage(new String[]{"ozzydev.springdemos.models.postgres"}).build();
    //    }


    //@Value("${odata.jpa.punit_name}")
    private String punitPrimary = "customers";
    private String punitSecondary = "products";
    //@Value("${odata.jpa.root_packages}")
    private String rootPackagesPrimary = "ozzydev.springdemos.models.mysql";
    private String rootPackagesSecondary = "ozzydev.springdemos.models.postgres";

    //    @PersistenceContext(unitName = "primary")
//    private EntityManager primaryEntityManager;
    @Bean
    @Qualifier("primaryODataSessionContext")
    public JPAODataSessionContextAccess sessionContextPrimary(
            @Autowired @Qualifier("primaryEmf") final EntityManagerFactory emf,
            @Autowired @Qualifier("primaryPagingProvider") final JPAODataPagingProvider pagingProvider) throws ODataException {

//        EntityManagerFactoryInfo info = (EntityManagerFactoryInfo) emf;
//        var ds = info.getDataSource();

        return JPAODataServiceContext.with()
                .setPUnit(punitPrimary)
                .setEntityManagerFactory(emf)
                .setTypePackage(rootPackagesPrimary)
                .setPagingProvider(pagingProvider)
                .setRequestMappingPath("customers/v1")
                .setUseAbsoluteContextURL(true)
                .build();
    }


    @Bean
    @Qualifier("primaryJpaStreamer")
    public JPAStreamer primaryJpaStreamer( @Autowired @Qualifier("primaryEmf") final EntityManagerFactory emf) {
        return JPAStreamer.of(emf);
    }

    @Bean
    @Qualifier("secondaryODataSessionContext")
    public JPAODataSessionContextAccess sessionContextSecondary(
            @Autowired @Qualifier("secondaryEmf") final EntityManagerFactory emf,
            @Autowired @Qualifier("secondaryPagingProvider") final JPAODataPagingProvider pagingProvider) throws ODataException {

//        EntityManagerFactoryInfo info = (EntityManagerFactoryInfo) emf;
//        var ds = info.getDataSource();

        return JPAODataServiceContext.with()
                .setPUnit(punitSecondary)
                .setEntityManagerFactory(emf)
                .setTypePackage(rootPackagesSecondary)
                .setPagingProvider(pagingProvider)
                .setRequestMappingPath("products/v1")
                .setUseAbsoluteContextURL(true)
                .build();
    }


    @Bean
    @Qualifier("secondaryJpaStreamer")
    public JPAStreamer secondaryJpaStreamer( @Autowired @Qualifier("secondaryEmf") final EntityManagerFactory emf) {
        return JPAStreamer.of(emf);
    }

    @Bean
    @Scope(scopeName = SCOPE_REQUEST)
    public JPAODataRequestContext requestContext() {

        return JPAODataRequestContext.with()
                .setCUDRequestHandler(new JPAExampleCUDRequestHandler())
                .setDebugSupport(new DefaultDebugSupport())
                .build();
    }


//    @ConfigurationProperties(prefix = "spring.datasource.customers")
//    @Bean
//    public DataSource customer() {
//        return DataSourceBuilder.create().build();
//    }
//
//    @ConfigurationProperties(prefix = "spring.datasource.products")
//    @Bean
//    public DataSource products() {
//        return DataSourceBuilder.create().build();
//    }
}

