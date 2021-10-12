package ozzydev.springdemos.config;


import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(basePackages = "ozzydev.springdemos.repos.postgres",
        entityManagerFactoryRef = "secondaryEntityManagerFactoryBean",
        //entityManagerFactoryRef = "secondaryEmf",
        transactionManagerRef = "secondaryTransactionManager")
public class SecondaryDbConfig
{

    @Autowired
    Environment env;

    @Bean
    //@Primary
    @ConfigurationProperties(prefix = "app.datasource.secondary")
    public DataSourceProperties secondaryDataSourceProperties()
    {
        return new DataSourceProperties();
    }

    @Bean
    //@Primary
    @Qualifier("secondaryDS")
    public DataSource secondaryDataSource()
    {
        return secondaryDataSourceProperties().initializeDataSourceBuilder().type(HikariDataSource.class).build();
    }

    @Bean
    //@Primary
    public LocalContainerEntityManagerFactoryBean secondaryEntityManagerFactoryBean()
    {
        LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
        em.setDataSource(secondaryDataSource());
        em.setPackagesToScan("ozzydev.springdemos.models.postgres");

        HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        em.setJpaVendorAdapter(vendorAdapter);
        em.setPersistenceUnitName("secondary");

        HashMap<String, Object> properties = new HashMap<>();
        properties.put("hibernate.hbm2ddl.auto", env.getProperty("app.datasource.secondary.jpa.hibernate.ddl-auto"));
        properties.put("hibernate.dialect", env.getProperty("hibernate.dialect.secondary"));
        properties.put("hibernate.show_sql", env.getProperty("app.datasource.secondary.hibernate.show_sql"));
        properties.put("hibernate.format_sql", env.getProperty("app.datasource.secondary.hibernate.format_sql"));
        em.setJpaPropertyMap(properties);
        return em;
    }

    @Bean
    //@Primary
    public PlatformTransactionManager secondaryTransactionManager()
    {
        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(secondaryEntityManagerFactoryBean().getObject());
        return transactionManager;
    }


    @Bean(name = "secondaryEmf")
    @Qualifier("secondaryEmf")
    public EntityManagerFactory secondaryEntityManagerFactory()
    {
        return secondaryEntityManagerFactoryBean().getObject();
    }

    @Bean(name = "secondaryEm")
    @Qualifier("secondaryEm")
    public EntityManager secondaryEntityManager()
    {
        return secondaryEntityManagerFactory().createEntityManager();
    }

}