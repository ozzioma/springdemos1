package ozzydev.springdemos.config;

import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.sap.olingo.jpa.processor.core.api.JPAODataServiceContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
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

import java.time.format.DateTimeFormatter;
import java.util.Arrays;


@Configuration
public class AppConfig
{

    @Autowired
    AutowireCapableBeanFactory beanFactory;

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
    public OpenAPI customOpenAPI(@Value("${application-description}") String appDesciption, @Value("${application-version}") String appVersion)
    {
        return new OpenAPI()
                .info(new Info()
                        .title("Ozzy API")
                        .version(appVersion)
                        .description(appDesciption)
                        .termsOfService("http://ozzy.io/terms/")
                        .license(new License().name("Apache 2.0").url("http://ozzy.org")));
    }

    private static final String dateFormat = "yyyy-MM-dd";
    private static final String dateTimeFormat = "yyyy-MM-dd HH:mm:ss";

    @Bean
    public Jackson2ObjectMapperBuilderCustomizer jsonCustomizer()
    {
        return builder ->
        {
            builder.simpleDateFormat(dateTimeFormat);
            builder.serializers(new LocalDateSerializer(DateTimeFormatter.ofPattern(dateFormat)));
            builder.serializers(new LocalDateTimeSerializer(DateTimeFormatter.ofPattern(dateTimeFormat)));
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


}

