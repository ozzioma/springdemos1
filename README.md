# Data service utilities

This repo contains prototype implementations showing how to expose OData v4 services in Java, compose JPA queries declaratively
and wrap database query and statement executions in a TransactionScope.

# Stack Details
  - Spring Boot 
  - MySQL, Postgresql and SQL Server multiple datasource configuration
  - Java 11
  
  # Features overview
  - How to configure multiple datasources and databases in a Spring Boot app
  - How to expose OData v4 endpoints using https://github.com/SAP/olingo-jpa-processor-v4
  - Transaction wrapper for implementing Unit of Work pattern for JPA queries and statements. 
    This wrapper implements a TransactionScope for database queries and statements executed through JPA, Hibernate and raw JDBC.
  - QueryBuilder for composing JPA queries and reducing boilerplate for Spring Data JPA repositories
  
  
  # QueryBuilder overview
  This is a generic Repository pattern implementation that reduces the boilerplate associated with Spring Data JPA repositories.
  For example
  
  ``
     
     
          @Repository
          @Transactional(propagation = Propagation.REQUIRED)
          public class ProductRepo2 extends JpaCrudRepository<Product, Long>
          {

             public ProductRepo2(@Qualifier("secondaryEmf") EntityManagerFactory entityManagerFactory)
             {
                   super(Product.class, entityManagerFactory);
              }

          }
      
      
      //SELECT * FROM PRODUCTS WHERE CODE LIKE '%37%' AND NAME like '%Chair%'
       var data = productRepo2
                .findAll(col(Product.Fields.code).like("37"),
                        col("name").like("Chair"));
                        
       //SELECT * FROM PRODUCTS WHERE CODE LIKE '%37%' OR NAME like '%Chair%'
       var data2 = productRepo2
                .orFindAll(col(Product.Fields.code).like("37"),
                        col("name").like("Chair"));

       //SELECT * FROM PRODUCTS WHERE CODE LIKE '%37%' OR NAME like '%Chair%'
       var data3 = productRepo2
                .where(col(Product.Fields.code).like("37"))
                .or(col("name").like("Chair"))
                .findAll();

        if (data.isEmpty())
        {
            System.out.println("no products found");
        }
        else
        {

            System.out.println("total products found->" + data.size());
            data.forEach(product ->
            {
                try
                {
                    System.out.println(gson.toJson(product));
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            });

        }
        
        
        
        
        QueryFilter filter1 = QueryFilter.builder()
                .field(DemoCustomer.Fields.firstName)
                .operator(QueryOperator.LIKE)
                .values(List.of("De"))
                .build();

        QueryFilter filter2 = where(DemoCustomer.Fields.firstName).like("De");
        QueryFilter filter3 = where("firstName").like("De");
        QueryFilter filter4 = col("firstName").like("De");

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
                               
                               
        var whereFilters = customerRepo2.or(idFilter, emailFilter);
        var whereFilters = customerRepo2.where(emailFilter).or(idFilter);
        var data = customerRepo2.findAll(whereFilters);
        var data = customerRepo2.findAll();
        var data = customerRepo2
                .where(emailFilter, idFilter2)
                .or(idFilter, emailFilter2)
                .and(typeFilter1, dobFilter1)
                .or(typeFilter1, dobFilter1)
                .findAll();

                
                
  
  ``
  
  
