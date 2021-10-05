package ozzydev.springdemos;

import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import ozzydev.springdemos.models.mysql.DemoCustomer;
import ozzydev.springdemos.query.QueryFilter;
import ozzydev.springdemos.query.QueryOperator;
import ozzydev.springdemos.repos.mysql.CommitLogRepo;
import ozzydev.springdemos.repos.mysql.CustomerRepo;
import ozzydev.springdemos.repos.mysql.TxnRepo;
import ozzydev.springdemos.repos.postgres.ProductCategoryRepo;
import ozzydev.springdemos.repos.postgres.ProductRepo;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.ArrayList;
import java.util.List;

@Service
public class ServiceDemo
{

    @PersistenceContext(unitName = "primary")
    private EntityManager primaryEntityManager;

    @Autowired
    private CustomerRepo customerRepo;

    @Autowired
    private CommitLogRepo logRepo;

    @Autowired
    private TxnRepo txnRepo;

    @Autowired
    private ProductCategoryRepo productCategoryRepo;

    @Autowired
    private ProductRepo productRepo;


    @Transactional
    public void testQueries()
    {

        Gson gson = new Gson();

        Specification<DemoCustomer> nameLike =
                (root, query, criteriaBuilder) ->
                        criteriaBuilder.like(root.get(DemoCustomer.Fields.firstName), "%De%");
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

        QueryFilter idFilter = QueryFilter.builder()
                .field(DemoCustomer.Fields.id)
                .operator(QueryOperator.IN)
                //.values(List.of(Long.valueOf(33),Long.valueOf(34),Long.valueOf(35)))
                //.values(List.of("33","34","35"))
                .values(List.of(33, 34, 35))
                .build();

        QueryFilter emailFilter = QueryFilter.builder()
                .field(DemoCustomer.Fields.email)
                .operator(QueryOperator.CONTAINS)
                .values(List.of("jame"))
                .build();

        //List<QueryFilter> filters = new ArrayList<>();
        //filters.addAll(List.of(idFilter));

        var whereIdSpec = customerRepo.and(List.of(idFilter));
        var whereEmailSpecs = customerRepo.and(List.of(emailFilter));

        //var data = customerRepo.findAll(filters);
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

}
