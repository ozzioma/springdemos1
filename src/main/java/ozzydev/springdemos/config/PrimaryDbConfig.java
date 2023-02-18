package ozzydev.springdemos.config;


import com.sap.olingo.jpa.processor.core.api.JPAODataPagingProvider;
import com.sap.olingo.jpa.processor.core.api.example.JPAExamplePagingProvider;
import com.zaxxer.hikari.HikariDataSource;
import org.atteo.evo.inflector.English;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
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
import javax.persistence.metamodel.EntityType;
import javax.sql.DataSource;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Configuration
@EnableTransactionManagement
//@EnableJpaRepositories(basePackages = "ozzydev.springdemos.repos", entityManagerFactoryRef = "entityManager", transactionManagerRef = "transactionManager")
@EnableJpaRepositories(basePackages = "ozzydev.springdemos.repos.mysql", entityManagerFactoryRef = "primaryEntityManager",
        transactionManagerRef = "primaryTransactionManager")
public class PrimaryDbConfig {

    @Autowired
    Environment env;

    @Bean
    @Primary
    @ConfigurationProperties(prefix = "app.datasource.primary")
    public DataSourceProperties primaryDataSourceProperties() {
        return new DataSourceProperties();
    }

    @Bean
    @Primary
    @Qualifier("primaryDS")
    public DataSource primaryDataSource() {
        return primaryDataSourceProperties().initializeDataSourceBuilder().type(HikariDataSource.class).build();
    }

//    @Bean
//    //@Primary
//    //@Qualifier("primaryDS")
//    @ConfigurationProperties(prefix = "spring.datasource.customers")
//    public DataSource customers() {
//        return DataSourceBuilder.create().build();
//    }

    @Bean
    @Primary
    public LocalContainerEntityManagerFactoryBean primaryEntityManager() {
        LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
        em.setDataSource(primaryDataSource());
        em.setPackagesToScan("ozzydev.springdemos.models.mysql");

        HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        em.setJpaVendorAdapter(vendorAdapter);
        em.setPersistenceUnitName("primary");

        HashMap<String, Object> properties = new HashMap<>();
        properties.put("hibernate.hbm2ddl.auto", env.getProperty("app.datasource.primary.jpa.hibernate.ddl-auto"));
        properties.put("hibernate.dialect", env.getProperty("hibernate.dialect.primary"));
        properties.put("hibernate.show_sql", env.getProperty("app.datasource.primary.hibernate.show_sql"));
        properties.put("hibernate.format_sql", env.getProperty("app.datasource.primary.hibernate.format_sql"));
        //properties.put("hibernate.enable_lazy_load_no_trans", "true");
        em.setJpaPropertyMap(properties);
        return em;
    }

    @Bean
    @Primary
    public PlatformTransactionManager primaryTransactionManager() {
        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(primaryEntityManager().getObject());
        return transactionManager;
    }

    @Bean
    @Qualifier("primaryEmf")
    public EntityManagerFactory primaryEntityManagerFactory() {
        return primaryEntityManager().getObject();
    }

    @Bean
    @Qualifier("primaryEm")
    public EntityManager getEntityManager() {
        return primaryEntityManager().getObject().createEntityManager();
    }


//    private PagingProvider primaryPagingProvider() {
//        final Map<String, Integer> pageSizes = new HashMap<>();
//        pageSizes.put("Companies", 5);
//        pageSizes.put("AdministrativeDivisions", 10);
//
//        return new PagingProvider(pageSizes, BUFFER_SIZE);
//    }


    @Bean
    @Qualifier("primaryPagingProvider")
    public JPAODataPagingProvider primaryPagingProvider() {
        int BUFFER_SIZE = 10;
        final Map<String, Integer> pageSizes = new HashMap<>();
        //pageSizes.put("Companies", 5);
        //pageSizes.put("AdministrativeDivisions", 10);

        try {

            Set<EntityType<?>> entityTypes = primaryEntityManagerFactory().getMetamodel().getEntities();
            for (javax.persistence.metamodel.EntityType entityType : entityTypes) {
                //logger.info(entityType.getName());
                //logger.info(entityType.getJavaType().getCanonicalName());
                //logger.info("******************************");

                System.out.println("entity name->" + entityType.getName() +  " canonical Name->" + entityType.getJavaType().getCanonicalName());
                System.out.println("plural entity name->" + English.plural(entityType.getName()));

                pageSizes.put(English.plural(entityType.getName()), 20);
            }

//            DatabaseMetaData metaData = secondaryDataSource().getConnection().getMetaData();
//            ResultSet tables = metaData.getTables(null, null, null, new String[]{"TABLE"});
//            while (tables.next()) {
//                var tableName = tables.getString("TABLE_NAME");
//                var tableType = tables.getString("TABLE_TYPE");
//                var tableTypeName = tables.getString("TYPE_NAME");
//                System.out.println("table name->" + tableName + " table type->" + tableType + " type Name->" + tableTypeName);
//
//                pageSizes.put(tableName, 20);
//            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }


        return new JPAExamplePagingProvider(pageSizes, BUFFER_SIZE);
        //return new JPAODataPagingProvider(pageSizes, BUFFER_SIZE);
    }

}

