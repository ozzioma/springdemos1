package ozzydev.teiid.teiiddemo1.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
//import springfox.documentation.builders.PathSelectors;
//import springfox.documentation.builders.RequestHandlerSelectors;
//import springfox.documentation.spi.DocumentationType;
//import springfox.documentation.spring.web.plugins.Docket;


import org.teiid.spring.autoconfigure.MultiDataSourceTransactionManagement;

import javax.sql.DataSource;


@Configuration
public class AppDataSources extends MultiDataSourceTransactionManagement
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


    private static final String dateFormat = "yyyy-MM-dd";
    private static final String dateFormat2 = "dd-MM-yyyy";
    private static final String dateTimeFormat = "yyyy-MM-dd HH:mm:ss";
    private static final String dateTimeFormat2 = "dd-MM-yyyy HH:mm:ss";

//    @Bean
//    public Jackson2ObjectMapperBuilderCustomizer jsonCustomizer() {
//        return builder ->
//        {
//            builder.simpleDateFormat(dateTimeFormat2);
//            builder.serializers(new LocalDateSerializer(DateTimeFormatter.ofPattern(dateFormat2)));
//            builder.serializers(new LocalDateTimeSerializer(DateTimeFormatter.ofPattern(dateTimeFormat2)));
//        };
//    }

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



    @ConfigurationProperties(prefix = "spring.datasource.customers")
    @Bean
    public DataSource customer() {
        return DataSourceBuilder.create().build();
    }

    @ConfigurationProperties(prefix = "spring.datasource.products")
    @Bean
    public DataSource products() {
        return DataSourceBuilder.create().build();
    }
}

