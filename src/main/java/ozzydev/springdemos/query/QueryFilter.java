package ozzydev.springdemos.query;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
//@Builder
@AllArgsConstructor
public class QueryFilter //<T>
{
    private String field;
    private QueryOperator operator;
    //private String value;
    private List<Object> values = new ArrayList<>();

    public static QueryFilter.QueryFilterBuilder builder()
    {
        return new QueryFilter.QueryFilterBuilder();
    }


    public static QueryFilter build(String field, QueryOperator operator, Object... values)
    {
        return QueryFilter.builder().field(field)
                .operator(operator)
                .values(List.of(values)).build();
    }

    public static QueryFilter.QueryFilterBuilder where(String field)
    {
        return builder().field(field);
    }


    public static class QueryFilterBuilder
    {
        private String field;
        private QueryOperator operator;
        private List<Object> values;

        QueryFilterBuilder()
        {
        }

        public QueryFilter.QueryFilterBuilder field(final String field)
        {
            this.field = field;
            return this;
        }

        public QueryFilter.QueryFilterBuilder operator(final QueryOperator operator)
        {
            this.operator = operator;
            return this;
        }

        public QueryFilter.QueryFilterBuilder values(final List<Object> values)
        {
            this.values = values;
            return this;
        }

        public QueryFilter equal(final Object value)
        {
            this.operator = QueryOperator.EQ;
            this.values = List.of(value);
            return build();
        }

        public QueryFilter notEqual(final Object value)
        {
            this.operator = QueryOperator.NE;
            this.values = List.of(value);
            return build();
        }

        public QueryFilter isTrue()
        {
            this.operator = QueryOperator.IS_TRUE;
            return build();
        }

        public QueryFilter isFalse()
        {
            this.operator = QueryOperator.IS_FALSE;
            return build();
        }

        public QueryFilter isNull()
        {
            this.operator = QueryOperator.IS_NULL;
            return build();
        }

        public QueryFilter isNull(final String field)
        {
            this.field = field;
            this.operator = QueryOperator.IS_NULL;
            return build();
        }

        public QueryFilter isNotNull()
        {
            this.operator = QueryOperator.NOT_NULL;
            return build();
        }

        public QueryFilter isNotNull(final String field)
        {
            this.field = field;
            this.operator = QueryOperator.NOT_NULL;
            return build();
        }


        public QueryFilter isGreaterThan(final Object value)
        {
            this.operator = QueryOperator.GT;
            this.values = List.of(value);
            return build();
        }

        public QueryFilter isNotGreaterThan(final Object value)
        {
            this.operator = QueryOperator.NGT;
            this.values = List.of(value);
            return build();
        }

        public QueryFilter isGreaterThanOrEquals(final Object value)
        {
            this.operator = QueryOperator.GTE;
            this.values = List.of(value);
            return build();
        }

        public QueryFilter isNotGreaterThanOrEquals(final Object value)
        {
            this.operator = QueryOperator.NGTE;
            this.values = List.of(value);
            return build();
        }

        public QueryFilter isLessThan(final Object value)
        {
            this.operator = QueryOperator.LT;
            this.values = List.of(value);
            return build();
        }

        public QueryFilter isNotLessThan(final Object value)
        {
            this.operator = QueryOperator.LT;
            this.values = List.of(value);
            return build();
        }

        public QueryFilter isLessThanOrEquals(final Object value)
        {
            this.operator = QueryOperator.LTE;
            this.values = List.of(value);
            return build();
        }

        public QueryFilter isNotLessThanOrEquals(final Object value)
        {
            this.operator = QueryOperator.LTE;
            this.values = List.of(value);
            return build();
        }

        public QueryFilter contains(final String value)
        {
            this.operator = QueryOperator.CONTAINS;
            this.values = List.of(value);
            return build();
        }

        public QueryFilter like(final String value)
        {
            this.operator = QueryOperator.LIKE;
            this.values = List.of(value);
            return build();
        }

        public QueryFilter notLike(final String value)
        {
            this.operator = QueryOperator.NOT_LIKE;
            this.values = List.of(value);
            return build();
        }


        public QueryFilter startsWith(final String value)
        {
            this.operator = QueryOperator.STARTS_WITH;
            this.values = List.of(value);
            return build();
        }

        public QueryFilter endsWith(final String value)
        {
            this.operator = QueryOperator.ENDS_WITH;
            this.values = List.of(value);
            return build();
        }


        public QueryFilter isBetween(final Object first, final Object second)
        {
            this.operator = QueryOperator.BETWEEN;
            this.values = List.of(first, second);
            return build();
        }

        public QueryFilter isNotBetween(final Object first, final Object second)
        {
            this.operator = QueryOperator.NOT_BETWEEN;
            this.values = List.of(first, second);
            return build();
        }


        public QueryFilter build()
        {
            return new QueryFilter(this.field, this.operator, this.values);
        }

        public String toString()
        {
            return "QueryFilter.QueryFilterBuilder(field=" + this.field + ", operator=" + this.operator + ", values=" + this.values + ")";
        }
    }


}


