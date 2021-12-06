package ozzydev.springdemos;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import ozzydev.springdemos.config.GsonExclusionStrategy;
import ozzydev.springdemos.models.mysql.CustomerType;
import ozzydev.springdemos.models.mysql.DemoCustomer;
import ozzydev.springdemos.models.postgres.Product;
import ozzydev.springdemos.query.*;
import ozzydev.springdemos.repos.mysql.CommitLogRepo;
import ozzydev.springdemos.repos.mysql.CustomerRepo;
import ozzydev.springdemos.repos.mysql.CustomerRepo2;
import ozzydev.springdemos.repos.mysql.TxnRepo;
import ozzydev.springdemos.repos.postgres.ProductCategoryRepo;
import ozzydev.springdemos.repos.postgres.ProductRepo;
import ozzydev.springdemos.repos.postgres.ProductRepo2;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static ozzydev.springdemos.query.QueryFilter.*;
import static ozzydev.springdemos.query.JpaCrudRepository.*;

@Service
public class QueryDemo1
{

    @PersistenceContext(unitName = "primary")
    private EntityManager primaryEntityManager;

    @Autowired
    private CustomerRepo customerRepo;

    @Autowired
    private CustomerRepo2 customerRepo2;

    @Autowired
    private CommitLogRepo logRepo;

    @Autowired
    private TxnRepo txnRepo;

    @Autowired
    private ProductCategoryRepo productCategoryRepo;

    @Autowired
    private ProductRepo productRepo;

    @Autowired
    private ProductRepo2 productRepo2;

    final static Logger logger = LoggerFactory.getLogger(QueryDemo1.class);

    @Transactional
    public void testQueries()
    {

        Gson gson = new Gson();

        Specification<DemoCustomer> nameLike = (root, query, criteriaBuilder) -> criteriaBuilder.like(root.get(DemoCustomer.Fields.firstName), "%De%");
        var data = customerRepo.findAll(nameLike);
        if (data.isEmpty())
        {
            System.out.println("no customers found");
        }
        else
        {
            //Session sessin = (Session)primaryEntityManager.unwrap(Session.class);
            //sessin.close();

            System.out.println("total customers found->" + data.size());
            data.forEach(customer -> System.out.println(gson.toJson(customer)));
            //data.forEach(customer -> System.out.println(customer.toString()));
            //            for (var customer : data)
            //            {
            //                System.out.println(customer);
            //            }
        }


    }

    @Transactional
    public void testQueries2()
    {
        Gson gson = new Gson();
        List<Long> inVals = new ArrayList<>();
        inVals.add(Long.valueOf(33));
        inVals.add(Long.valueOf(34));
        inVals.add(Long.valueOf(35));

        QueryFilter idFilter = QueryFilter.builder().field(DemoCustomer.Fields.id).operator(QueryOperator.IN)
                //.values(List.of(Long.valueOf(33),Long.valueOf(34),Long.valueOf(35)))
                //.values(List.of("33","34","35"))
                .values(List.of(33, 34, 35)).build();

        QueryFilter emailFilter = QueryFilter.builder().field(DemoCustomer.Fields.email).operator(QueryOperator.CONTAINS).values(List.of("jame")).build();

        //List<QueryFilter> filters = new ArrayList<>();
        //filters.addAll(List.of(idFilter));

        var whereIdSpec = customerRepo.and(List.of(idFilter));
        var whereEmailSpecs = customerRepo.and(List.of(emailFilter));

        //var data = customerRepo.findAll(filters);
        //var data = customerRepo.findAll(whereIdSpec.or(whereEmailSpecs));
        var data = customerRepo.findAll(whereIdSpec.or(whereEmailSpecs));

        if (data.isEmpty())
        {
            System.out.println("no customers found");
        }
        else
        {

            System.out.println("total customers found->" + data.size());
            data.forEach(customer -> System.out.println(gson.toJson(customer)));
            //data.forEach(customer -> System.out.println(customer.toString()));
            //            for (var customer : data)
            //            {
            //                System.out.println(customer);
            //            }
        }

    }


    @Transactional
    public void testQueries3()
    {
        Gson gson = new Gson();
        List<Long> inVals = new ArrayList<>();
        inVals.add(Long.valueOf(33));
        inVals.add(Long.valueOf(34));
        inVals.add(Long.valueOf(35));

        JpaQuerySpecification<DemoCustomer> spec1 = new JpaQuerySpecification<DemoCustomer>()
        {
            @Override
            public Predicate toPredicate(Root<DemoCustomer> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) throws UnsupportedOperationException
            {
                return criteriaBuilder.like(root.get(DemoCustomer.Fields.firstName), "%De%");
            }
        };

        JpaQuerySpecification<DemoCustomer> spec2 = (root, query, criteriaBuilder) -> criteriaBuilder.like(root.get(DemoCustomer.Fields.firstName), "%De%");

        AndSpecification<DemoCustomer> spec3 = new AndSpecification<DemoCustomer>()
        {
            @Override
            public Predicate toPredicate(Root<DemoCustomer> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) throws UnsupportedOperationException
            {
                return criteriaBuilder.like(root.get(DemoCustomer.Fields.firstName), "%De%");
            }
        };

        QueryFilter spec4 = QueryFilter.builder()
                .field(DemoCustomer.Fields.firstName)
                .operator(QueryOperator.LIKE)
                .values(List.of("De"))
                .build();

        QueryFilter spec5 = where(DemoCustomer.Fields.firstName).like("De");
        QueryFilter spec6 = where("firstName").like("De");
        QueryFilter spec7 = col("firstName").like("De");

        QueryFilter idFilter = QueryFilter.builder()
                .field(DemoCustomer.Fields.id)
                .operator(QueryOperator.IN)
                //.values(List.of(Long.valueOf(33),Long.valueOf(34),Long.valueOf(35)))
                //.values(List.of("33","34","35"))
                .values(List.of(33, 34, 35))
                .build();

        QueryFilter idFilter2 = QueryFilter.builder()
                .field(DemoCustomer.Fields.id)
                .operator(QueryOperator.IN)
                .values(List.of(43, 44, 45)).build();

        QueryFilter emailFilter = QueryFilter.builder()
                .field(DemoCustomer.Fields.email)
                .operator(QueryOperator.CONTAINS)
                .values(List.of("jame")).build();

        QueryFilter emailFilter2 = QueryFilter.builder().field(DemoCustomer.Fields.email)
                .operator(QueryOperator.CONTAINS)
                .values(List.of("elena")).build();

        QueryFilter typeFilter1 = QueryFilter.builder().field(DemoCustomer.Fields.customerType)
                .operator(QueryOperator.EQ)
                .values(List.of(CustomerType.GOLD)).build();

        var typeFilter12 = QueryFilter.build(DemoCustomer.Fields.dob, QueryOperator.EQ, List.of(CustomerType.GOLD));
        var typeFilter13 = new QueryFilter(DemoCustomer.Fields.dob, QueryOperator.EQ, List.of(CustomerType.GOLD));
        var typeFilter14 = where("").equal(123);

        var date1 = LocalDate.of(1999, 11, 24);
        var date2 = LocalDate.now();
        logger.info("local date1 param value->" + date1);
        logger.info("local date1 param stringvalue->" + date1.toString());

        logger.info("local date2 param value->" + date2);
        logger.info("local date2 param stringvalue->" + date2.toString());

        var dobFilter1 = where("dob").isGreaterThan(LocalDate.now().minusYears(44));
        QueryFilter dobFilter11 = QueryFilter.builder().field(DemoCustomer.Fields.dob)
                .operator(QueryOperator.GT)
                .values(List.of(date2)).build();

        QueryFilter dobFilter2 = QueryFilter.builder().field(DemoCustomer.Fields.dob)
                .operator(QueryOperator.BETWEEN)
                .values(List.of(
                        LocalDate.of(1995, 11, 24),
                        LocalDate.of(1999, 11, 24)
                               )).build();


        //List<QueryFilter> filters = new ArrayList<>();
        //filters.addAll(List.of(idFilter));

        //var whereIdSpec = customerRepo.and(List.of(idFilter));
        //var whereEmailSpecs = customerRepo.and(List.of(emailFilter));

        //var data = customerRepo.findAll(filters);

        //var whereFilters = customerRepo2.or(idFilter, emailFilter);
        //var whereFilters = customerRepo2.where(emailFilter).or(idFilter);
        //var data = customerRepo2.findAll(whereFilters);
        //var data = customerRepo2.findAll();
        var data = customerRepo2
                .where(emailFilter, idFilter2)
                //.or(idFilter, emailFilter2)
                //.and(typeFilter1, dobFilter1)
                .or(typeFilter1, dobFilter1)
                .findAll();

        var dd = where("dob").isGreaterThan(date1);
        //        var data2 = customerRepo2
        //                .findAll(
        //                        col("dob").isGreaterThan(date1),
        //                        col("dob").isGreaterThan(date1),
        //                        col("dob").isGreaterThan(date1),
        //                        col("dob").isGreaterThan(date1)
        //                        );
        //
        //        var data3 = customerRepo2
        //                .where(
        //                        col("dob").isGreaterThan(date1),
        //                        col("dob").isGreaterThan(date1),
        //                        col("dob").isGreaterThan(date1),
        //                        col("dob").isGreaterThan(date1)
        //                      )
        //                .and(
        //                        col("dob").isGreaterThan(date1),
        //                        col("dob").isGreaterThan(date1),
        //                        col("dob").isGreaterThan(date1),
        //                        col("dob").isGreaterThan(date1)
        //                    )
        //                .or(
        //                        col("dob").isGreaterThan(date1),
        //                        col("dob").isGreaterThan(date1),
        //                        col("dob").isGreaterThan(date1),
        //                        col("dob").isGreaterThan(date1)
        //                   )
        //                .findAll();

        if (data.isEmpty())
        {
            System.out.println("no customers found");
        }
        else
        {

            System.out.println("total customers found->" + data.size());
            data.forEach(customer -> System.out.println(gson.toJson(customer)));
            //data.forEach(customer -> System.out.println(customer.toString()));
            //            for (var customer : data)
            //            {
            //                System.out.println(customer);
            //            }
        }

    }


    //@Transactional(propagation = Propagation.REQUIRED)
    public void testQueries4()
    {
        Gson gson;// = new Gson();
        gson = new GsonBuilder().setExclusionStrategies(new GsonExclusionStrategy()).create();

        ObjectMapper objectMapper = new ObjectMapper();

        //        var data = productRepo2.findAll(
        //                col(Product.Fields.code).like("37")
        //               // col("name").like("Coat")
        //                                       );

        logger.info("table name->" + customerRepo2.getTableName());
        logger.info("entity name->" + customerRepo2.getEntityName());

        var prod1 = productRepo2.findById(444L);
        var prod2 = productRepo2.findById(44L);

        var prodExists = productRepo2.existsById(444L);
        var prodExists2 = productRepo2.existsById(44L);

        logger.info("prod 1 exists->" + prod1.isPresent());
        logger.info("prod 2 exists->" + prod2.isPresent());

        logger.info("exists check 1->" + prodExists);
        logger.info("exists check 2->" + prodExists2);

        var data = productRepo2
                .findAll(col(Product.Fields.code).like("37"),
                        col("name").like("Chair"));

//        var data2 = productRepo2
//                .where(col(Product.Fields.code).like("37"))
//                .or(col("name").like("Chair"))
//                .findAll();

        if (data.isEmpty())
        {
            System.out.println("no products found");
        }
        else
        {

            System.out.println("total products found->" + data.size());
            //data.forEach(product -> System.out.println(gson.toJson(product)));
            data.forEach(product ->
            {
                try
                {
                    System.out.println(gson.toJson(product));
                    // System.out.println(objectMapper.writeValueAsString(product));
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            });

        }

    }


}
