package ozzydev.springdemos.query;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public interface CustomSpecificationHandler<T> extends JpaSpecificationExecutor<T>
{

    public default List<T> findAll(List<QueryFilter> filters)
    {
        if (filters.size() > 0)
        {
            return findAll(getSpecification(filters));
        }
        else
        {
            Pageable page = PageRequest.of(0, 20);
            Specification<T> idNotNull =
                    (root, query, criteriaBuilder) -> criteriaBuilder.isNotNull(root.get("id"));
            var data = findAll(idNotNull, page);
            return data.getContent();
        }
    }

    public default Specification<T> getSpecification(List<QueryFilter> filters)
    {
//        Specification<T> specification = Specification.where(createSpecification(filters.remove(0)));
//        for (QueryFilter input : filters)
//        {
//            specification = specification.and(createSpecification(input));
//        }

        Specification<T> specification = null;
        for (QueryFilter input : filters)
        {
            if (specification == null)
            {
                specification = Specification.where(createSpecification(input));
                if (filters.size() == 1)
                    return specification;
            }

            specification = specification.and(createSpecification(input));
        }

        return specification;
    }

    public default Specification<T> or(List<QueryFilter> filters)
    {
//        Specification<T> specification = Specification.where(createSpecification(filters.remove(0)));
//        for (QueryFilter input : filters)
//        {
//            specification = specification.or(createSpecification(input));
//        }

        Specification<T> specification = null;
        for (QueryFilter input : filters)
        {
            if (specification == null)
            {
                specification = Specification.where(createSpecification(input));
                if (filters.size() == 1)
                    return specification;
            }

            specification = specification.or(createSpecification(input));
        }
        return specification;
    }

    public default Specification<T> and(List<QueryFilter> filters)
    {
//        Specification<T> specification = Specification.where(createSpecification(filters.remove(0)));
//        for (QueryFilter input : filters)
//        {
//            specification = specification.and(createSpecification(input));
//        }

        Specification<T> specification = null;
        for (QueryFilter input : filters)
        {
            if (specification == null)
            {
                specification = Specification.where(createSpecification(input));
                if (filters.size() == 1)
                    return specification;
            }

            specification = specification.and(createSpecification(input));
        }
        return specification;
    }

    public default Specification<T> createSpecification(QueryFilter input)
    {
        if (input.getValues() == null || input.getValues().isEmpty())
        {
            throw new RuntimeException("Filter input value(s) list must have atleast one element");
        }

        switch (input.getOperator())
        {

            case IS_NULL:
                return new Specification<T>()
                {
                    @Override
                    public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder)
                    {
                        return criteriaBuilder.isNull(root.get(input.getField()));
                    }
                };

            case NOT_NULL:
                return (root, query, criteriaBuilder) ->
                        criteriaBuilder.isNotNull(root.get(input.getField()));

            case IS_TRUE:
                return (root, query, criteriaBuilder) ->
                        criteriaBuilder.isTrue(root.get(input.getField()));

            case IS_FALSE:
                return (root, query, criteriaBuilder) ->
                        criteriaBuilder.isFalse(root.get(input.getField()));

            case EQ:
                return (root, query, criteriaBuilder) ->
                        criteriaBuilder.equal(root.get(input.getField()),
                                castToRequiredType(root.get(input.getField()).getJavaType(),
                                        input.getValues().get(0).toString()));

            case NE:
                return (root, query, criteriaBuilder) ->
                        criteriaBuilder.notEqual(root.get(input.getField()),
                                castToRequiredType(root.get(input.getField()).getJavaType(),
                                        input.getValues().get(0).toString()));

//            case GT:
//                return (root, query, criteriaBuilder) ->
//                        criteriaBuilder.gt(root.get(input.getField()),
//                                 castToRequiredType(
//                                        root.get(input.getField()).getJavaType(),
//                                        input.getValues().get(0).toString()));

            case GT:
                return (root, query, criteriaBuilder) -> criteriaBuilder.greaterThan(root.get(input.getField()),
                        castToRequiredType(
                                root.get(input.getField()).getJavaType(),
                                input.getValues().get(0).toString()).toString());

//            case GTE:
//                return (root, query, criteriaBuilder) ->
//                        criteriaBuilder.ge(root.get(input.getField()),
//                                (Number) castToRequiredType(
//                                        root.get(input.getField()).getJavaType(),
//                                        input.getValues().get(0).toString()));


            case GTE:
                return (root, query, criteriaBuilder) -> criteriaBuilder.greaterThanOrEqualTo(root.get(input.getField()),
                        castToRequiredType(
                                root.get(input.getField()).getJavaType(),
                                input.getValues().get(0).toString()).toString());

            case LT:
                return (root, query, criteriaBuilder) ->
                        criteriaBuilder.lessThan(root.get(input.getField()),
                                castToRequiredType(
                                        root.get(input.getField()).getJavaType(),
                                        input.getValues().get(0).toString()).toString());

            case LTE:
                return (root, query, criteriaBuilder) ->
                        criteriaBuilder.lessThanOrEqualTo(root.get(input.getField()),
                                castToRequiredType(
                                        root.get(input.getField()).getJavaType(),
                                        input.getValues().get(0).toString()).toString());

            case LIKE:
            case CONTAINS:
                return (root, query, criteriaBuilder) ->
                        criteriaBuilder.like(root.get(input.getField()),
                                "%" + input.getValues().get(0).toString() + "%");

            case NOT_LIKE:
                return (root, query, criteriaBuilder) ->
                        criteriaBuilder.notLike(root.get(input.getField()),
                                "%" + input.getValues().get(0).toString() + "%");

            case STARTSWITH:
                return (root, query, criteriaBuilder) ->
                        criteriaBuilder.like(root.get(input.getField()),
                                "%" + input.getValues().get(0).toString());

            case ENDSWITH:
                return (root, query, criteriaBuilder) ->
                        criteriaBuilder.like(root.get(input.getField()),
                                input.getValues().get(0).toString() + "%");

            case IN:
                return (root, query, criteriaBuilder) ->
                        criteriaBuilder.in(root.get(input.getField())).value(
                                castToRequiredType(root.get(input.getField()).getJavaType(), input.getValues()));
//                {
//                    CriteriaBuilder.In<String> inClause = criteriaBuilder.in(root.get(input.getField()));
//                    for (Object value : input.getValues())
//                    {
//                        var castedValue = castToRequiredType(
//                                root.get(input.getField()).getJavaType(),
//                                input.getValues().get(0).toString());
//
//                        //inClause.value(value.toString());
//                        inClause.value(castedValue.toString());
//
//                    }
//                    return inClause;
//                };


            case NOT_IN:
                return (root, query, criteriaBuilder) ->
                        criteriaBuilder.in(root.get(input.getField())).value(
                                castToRequiredType(root.get(input.getField()).getJavaType(), input.getValues())).not();
//                return (root, query, criteriaBuilder) ->
//                {
//
//                    CriteriaBuilder.In<String> inClause = criteriaBuilder.in(root.get(input.getField()));
//
//                    for (Object value : input.getValues())
//                    {
//                        var castedValue = castToRequiredType(
//                                root.get(input.getField()).getJavaType(),
//                                input.getValues().get(0).toString());
//                        inClause.value(castedValue.toString());
//
//                    }
//                    return inClause.not();
//                };

            case BETWEEN:
                return (root, query, criteriaBuilder) ->
                {
                    var firstValue = input.getValues().get(0);
                    var secondValue = input.getValues().get(1);

                    if (firstValue == null || secondValue == null)
                    {
                        throw new RuntimeException("First and second parameter values for BETWEEN operator cannot be null");
                    }

                    return criteriaBuilder.between(root.get(input.getField()),
                            castToRequiredType(root.get(input.getField()).getJavaType(), firstValue.toString()).toString(),
                            castToRequiredType(root.get(input.getField()).getJavaType(), secondValue.toString()).toString()
                    );

                };


            default:
                throw new RuntimeException("Operator not supported");
        }
    }


    private Object castToRequiredType(Class fieldType, Object value)
    {
        if (fieldType.isAssignableFrom(Double.class))
        {
            return Double.valueOf(value.toString());
        }
        else if (fieldType.isAssignableFrom(Float.class))
        {
            return Float.valueOf(value.toString());
        }
        else if (fieldType.isAssignableFrom(Integer.class))
        {
            return Integer.valueOf(value.toString());
        }
        else if (fieldType.isAssignableFrom(Long.class))
        {
            System.out.println("Long value found->" + value.toString());
            return Long.valueOf(value.toString());
        }
        else if (fieldType.isAssignableFrom(Short.class))
        {
            return Short.valueOf(value.toString());
        }
        else if (fieldType.isAssignableFrom(Byte.class))
        {
            return Byte.valueOf(value.toString());
        }
        else if (fieldType.isAssignableFrom(LocalDate.class))
        {
            return LocalDate.parse(value.toString());
        }
        else if (fieldType.isAssignableFrom(LocalDateTime.class))
        {
            return LocalDateTime.parse(value.toString());
        }
        else if (fieldType.isAssignableFrom(LocalTime.class))
        {
            return LocalDate.parse(value.toString());
        }
        else if (fieldType.isAssignableFrom(Date.class))
        {
            return Date.parse(value.toString());
        }
        else if (Enum.class.isAssignableFrom(fieldType))
        {
            return Enum.valueOf(fieldType, value.toString());
        }
        return null;
    }

    private Object castToRequiredType(Class fieldType, List<Object> values)
    {
        List<Object> lists = new ArrayList<>();
        for (Object value : values)
        {
            lists.add(castToRequiredType(fieldType, value));
        }
        return lists;
    }
}


