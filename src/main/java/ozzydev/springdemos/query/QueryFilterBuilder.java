package ozzydev.springdemos.query;

import ozzydev.springdemos.models.mysql.CustomerType;
import ozzydev.springdemos.models.mysql.DemoCustomer;

import java.util.Arrays;
import java.util.List;

//public interface QueryFilterBuilder
//{
//
//    default QueryFilter buildQuery(String field, QueryOperator operator, Object... values)
//    {
//        return QueryFilter.builder().field(field)
//                .operator(operator)
//                .values(List.of(values)).build();
//    }
//
//    QueryFilter build(String field, QueryOperator operator, Object... values) throws UnsupportedOperationException;
//}

//public class QueryFilterBuilder
//{
//
//    public static QueryFilter where(String field)
//    {
//        return QueryFilter.builder().field(field).build();
//    }
//
//    public static QueryFilter equal(Object value)
//    {
//        return QueryFilter.builder().operator(QueryOperator.EQ).values(List.of(value)).build();
//    }
//}
