package ozzydev.springdemos.query;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.criteria.*;
import java.time.*;

import static ozzydev.springdemos.query.ValueToPathConverter.*;


public class JpaQueryResolver<T>
{
    final static Logger logger = LoggerFactory.getLogger(JpaQueryResolver.class);


    public JpaQuerySpecification<T> createSpecification(QueryFilter input)
    {

        if (input.getField() == null || input.getField().isEmpty() || input.getField().trim().isEmpty())
        {
            throw new RuntimeException("Query filter field name cannot be null or empty");
        }

        if (input.getValues() == null || input.getValues().isEmpty())
        {
            throw new RuntimeException("Query filter field parameter(s) must have atleast one value");
        }

        final var entityFieldName = input.getField();
        final var firstValue = input.getValues().size() > 0 ? input.getValues().get(0) : null;
        final var secondValue = input.getValues().size() > 1 ? input.getValues().get(1) : null;
        final var allValues = input.getValues();


        switch (input.getOperator())
        {

            case IS_NULL:
                return new JpaQuerySpecification<T>()
                {
                    @Override
                    public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) throws UnsupportedOperationException
                    {
                        return criteriaBuilder.isNull(root.get(entityFieldName));
                    }
                };

            case NOT_NULL:
                return (root, query, criteriaBuilder) -> criteriaBuilder.isNotNull(root.get(entityFieldName));

            case IS_TRUE:
                return (root, query, criteriaBuilder) ->
                        criteriaBuilder.isTrue(root.get(entityFieldName));

            case IS_FALSE:
                return (root, query, criteriaBuilder) ->
                        criteriaBuilder.isFalse(root.get(entityFieldName));

            case EQ:
            case EQUAL:
                return new JpaQuerySpecification<T>()
                {
                    @Override
                    public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) throws UnsupportedOperationException
                    {
                        var fieldClass = root.get(entityFieldName).getJavaType();
                        if (fieldClass.isAssignableFrom(LocalDate.class))
                        {
                            var tuple = getLocalDateBindInfo(root, input);
                            return criteriaBuilder.equal(tuple.item1, tuple.item2);
                        }
                        else if (fieldClass.isAssignableFrom(LocalDateTime.class))
                        {
                            var tuple = getLocalDateTimeBindInfo(root, input);
                            return criteriaBuilder.equal(tuple.item1, tuple.item2);
                        }
                        else if (fieldClass.isAssignableFrom(LocalTime.class))
                        {
                            var tuple = getLocalTimeBindInfo(root, input);
                            return criteriaBuilder.equal(tuple.item1, tuple.item2);
                        }
                        else if (fieldClass.isAssignableFrom(ZonedDateTime.class))
                        {
                            var tuple = getZonedDateTimeBindInfo(root, input);
                            return criteriaBuilder.equal(tuple.item1, tuple.item2);
                        }
                        else if (fieldClass.isAssignableFrom(OffsetDateTime.class))
                        {
                            var tuple = getOffsetDateTimeBindInfo(root, input);
                            return criteriaBuilder.equal(tuple.item1, tuple.item2);
                        }
                        else if (fieldClass.isAssignableFrom(OffsetTime.class))
                        {
                            var tuple = getOffsetTimeBindInfo(root, input);
                            return criteriaBuilder.equal(tuple.item1, tuple.item2);
                        }
                        else
                        {
                            return criteriaBuilder.equal(
                                    root.get(entityFieldName),
                                    castToEntityFieldType(root.get(entityFieldName).getJavaType(),
                                            firstValue));
                        }

                    }
                };
            //                return (root, query, criteriaBuilder) ->
            //                        criteriaBuilder.equal(root.get(entityFieldName),
            //                                castToEntityFieldType(root.get(entityFieldName).getJavaType(),
            //                                        firstValue));

            case NE:
            case NOT_EQUAL:
                return new JpaQuerySpecification<T>()
                {
                    @Override
                    public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) throws UnsupportedOperationException
                    {
                        var fieldClass = root.get(entityFieldName).getJavaType();
                        if (fieldClass.isAssignableFrom(LocalDate.class))
                        {
                            var tuple = getLocalDateBindInfo(root, input);
                            return criteriaBuilder.equal(tuple.item1, tuple.item2).not();
                        }
                        else if (fieldClass.isAssignableFrom(LocalDateTime.class))
                        {
                            var tuple = getLocalDateTimeBindInfo(root, input);
                            return criteriaBuilder.equal(tuple.item1, tuple.item2).not();
                        }
                        else if (fieldClass.isAssignableFrom(LocalTime.class))
                        {
                            var tuple = getLocalTimeBindInfo(root, input);
                            return criteriaBuilder.equal(tuple.item1, tuple.item2).not();
                        }
                        else if (fieldClass.isAssignableFrom(ZonedDateTime.class))
                        {
                            var tuple = getZonedDateTimeBindInfo(root, input);
                            return criteriaBuilder.equal(tuple.item1, tuple.item2).not();
                        }
                        else if (fieldClass.isAssignableFrom(OffsetDateTime.class))
                        {
                            var tuple = getOffsetDateTimeBindInfo(root, input);
                            return criteriaBuilder.equal(tuple.item1, tuple.item2).not();
                        }
                        else if (fieldClass.isAssignableFrom(OffsetTime.class))
                        {
                            var tuple = getOffsetTimeBindInfo(root, input);
                            return criteriaBuilder.equal(tuple.item1, tuple.item2).not();
                        }
                        else
                        {
                            return criteriaBuilder.equal(
                                    root.get(entityFieldName),
                                    castToEntityFieldType(root.get(entityFieldName).getJavaType(),
                                            firstValue)).not();
                        }

                    }
                };

            //                return (root, query, criteriaBuilder) ->
            //                        criteriaBuilder.notEqual(root.get(entityFieldName),
            //                                castToEntityFieldType(root.get(entityFieldName).getJavaType(),
            //                                        firstValue));


            case GT:
            case GREATER_THAN:
                return new JpaQuerySpecification<T>()
                {
                    @Override
                    public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) throws UnsupportedOperationException
                    {
                        var fieldClass = root.get(entityFieldName).getJavaType();
                        if (fieldClass.isAssignableFrom(LocalDate.class))
                        {
                            var tuple = getLocalDateBindInfo(root, input);
                            return criteriaBuilder.greaterThan(tuple.item1, tuple.item2);
                        }
                        else if (fieldClass.isAssignableFrom(LocalDateTime.class))
                        {
                            var tuple = getLocalDateTimeBindInfo(root, input);
                            return criteriaBuilder.greaterThan(tuple.item1, tuple.item2);
                        }
                        else if (fieldClass.isAssignableFrom(LocalTime.class))
                        {
                            var tuple = getLocalTimeBindInfo(root, input);
                            return criteriaBuilder.greaterThan(tuple.item1, tuple.item2);
                        }
                        else if (fieldClass.isAssignableFrom(ZonedDateTime.class))
                        {
                            var tuple = getZonedDateTimeBindInfo(root, input);
                            return criteriaBuilder.greaterThan(tuple.item1, tuple.item2);
                        }
                        else if (fieldClass.isAssignableFrom(OffsetDateTime.class))
                        {
                            var tuple = getOffsetDateTimeBindInfo(root, input);
                            return criteriaBuilder.greaterThan(tuple.item1, tuple.item2);
                        }
                        else if (fieldClass.isAssignableFrom(OffsetTime.class))
                        {
                            var tuple = getOffsetTimeBindInfo(root, input);
                            return criteriaBuilder.greaterThan(tuple.item1, tuple.item2);
                        }
                        else
                        {
                            return criteriaBuilder.greaterThan(root.get(entityFieldName),
                                    castToEntityFieldType(root.get(entityFieldName).getJavaType(),
                                            firstValue).toString());
                        }

                    }
                };

            case NGT:
            case NOT_GREATER_THAN:

                return new JpaQuerySpecification<T>()
                {
                    @Override
                    public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) throws UnsupportedOperationException
                    {
                        var fieldClass = root.get(entityFieldName).getJavaType();
                        if (fieldClass.isAssignableFrom(LocalDate.class))
                        {
                            var tuple = getLocalDateBindInfo(root, input);
                            return criteriaBuilder.greaterThan(tuple.item1, tuple.item2).not();
                        }
                        else if (fieldClass.isAssignableFrom(LocalDateTime.class))
                        {
                            var tuple = getLocalDateTimeBindInfo(root, input);
                            return criteriaBuilder.greaterThan(tuple.item1, tuple.item2).not();
                        }
                        else if (fieldClass.isAssignableFrom(LocalTime.class))
                        {
                            var tuple = getLocalTimeBindInfo(root, input);
                            return criteriaBuilder.greaterThan(tuple.item1, tuple.item2).not();
                        }
                        else if (fieldClass.isAssignableFrom(ZonedDateTime.class))
                        {
                            var tuple = getZonedDateTimeBindInfo(root, input);
                            return criteriaBuilder.greaterThan(tuple.item1, tuple.item2).not();
                        }
                        else if (fieldClass.isAssignableFrom(OffsetDateTime.class))
                        {
                            var tuple = getOffsetDateTimeBindInfo(root, input);
                            return criteriaBuilder.greaterThan(tuple.item1, tuple.item2).not();
                        }
                        else if (fieldClass.isAssignableFrom(OffsetTime.class))
                        {
                            var tuple = getOffsetTimeBindInfo(root, input);
                            return criteriaBuilder.greaterThan(tuple.item1, tuple.item2).not();
                        }
                        else
                        {
                            return criteriaBuilder.greaterThan(root.get(entityFieldName),
                                    castToEntityFieldType(root.get(entityFieldName).getJavaType(),
                                            firstValue).toString()).not();
                        }

                    }
                };
            //                return (root, query, criteriaBuilder) ->
            //                        criteriaBuilder.greaterThan(root.get(entityFieldName),
            //                                castToEntityFieldType(root.get(entityFieldName).getJavaType(),
            //                                        firstValue).toString()).not();


            case GTE:
            case GREATER_THAN_OR_EQUAL:
                return (root, query, criteriaBuilder) ->
                        criteriaBuilder.greaterThanOrEqualTo(root.get(entityFieldName),
                                castToEntityFieldType(root.get(entityFieldName).getJavaType(),
                                        firstValue).toString());

            case NGTE:
            case NOT_GREATER_THAN_OR_EQUAL:
                return (root, query, criteriaBuilder) ->
                        criteriaBuilder.greaterThanOrEqualTo(root.get(entityFieldName),
                                castToEntityFieldType(root.get(entityFieldName).getJavaType(),
                                        firstValue).toString()).not();

            case LT:
            case LESS_THAN:
                return (root, query, criteriaBuilder) ->
                        criteriaBuilder.lessThan(root.get(entityFieldName),
                                castToEntityFieldType(root.get(entityFieldName).getJavaType(),
                                        firstValue).toString());

            case NLT:
            case NOT_LESS_THAN:
                return (root, query, criteriaBuilder) ->
                        criteriaBuilder.lessThan(root.get(entityFieldName),
                                castToEntityFieldType(root.get(entityFieldName).getJavaType(),
                                        firstValue).toString()).not();

            case LTE:
            case LESS_THAN_OR_EQUAL:
                return (root, query, criteriaBuilder) ->
                        criteriaBuilder.lessThanOrEqualTo(root.get(entityFieldName),
                                castToEntityFieldType(root.get(entityFieldName).getJavaType(),
                                        firstValue).toString());

            case NLTE:
            case NOT_LESS_THAN_OR_EQUAL:
                return (root, query, criteriaBuilder) ->
                        criteriaBuilder.lessThanOrEqualTo(root.get(entityFieldName),
                                castToEntityFieldType(root.get(entityFieldName).getJavaType(),
                                        firstValue).toString()).not();

            case LIKE:
            case CONTAINS:
                return (root, query, criteriaBuilder) ->
                        criteriaBuilder.like(root.get(entityFieldName), "%" + firstValue.toString() + "%");

            case NOT_LIKE:
                return (root, query, criteriaBuilder) ->
                        criteriaBuilder.notLike(root.get(entityFieldName), "%" + firstValue.toString() + "%");

            case SW:
            case STARTS_WITH:
                return (root, query, criteriaBuilder) ->
                        criteriaBuilder.like(root.get(entityFieldName), "%" + firstValue.toString());

            case EW:
            case ENDS_WITH:
                return (root, query, criteriaBuilder) ->
                        criteriaBuilder.like(root.get(entityFieldName), firstValue + "%");

            case IN:
                return (root, query, criteriaBuilder) ->
                        criteriaBuilder.in(root.get(entityFieldName))
                                .value(castToEntityFieldType(root.get(entityFieldName).getJavaType(),
                                        allValues));


            case NOT_IN:
                return (root, query, criteriaBuilder) ->
                        criteriaBuilder.in(root.get(entityFieldName))
                                .value(castToEntityFieldType(root.get(entityFieldName).getJavaType(),
                                        allValues)).not();

            case BW:
            case BETWEEN:
                return (root, query, criteriaBuilder) ->
                {
                    if (firstValue == null)
                    {
                        throw new RuntimeException("First parameter value for BETWEEN operator cannot be null");
                    }

                    if (secondValue == null)
                    {
                        throw new RuntimeException("Second parameter value for BETWEEN operator cannot be null");
                    }

                    return criteriaBuilder.between(root.get(entityFieldName),
                            castToEntityFieldType(root.get(entityFieldName).getJavaType(), firstValue).toString(),
                            castToEntityFieldType(root.get(entityFieldName).getJavaType(), secondValue).toString());

                };

            case NBW:
            case NOT_BETWEEN:
                return (root, query, criteriaBuilder) ->
                {
                    if (firstValue == null)
                    {
                        throw new RuntimeException("First parameter value for BETWEEN operator cannot be null");
                    }

                    if (secondValue == null)
                    {
                        throw new RuntimeException("Second parameter value for BETWEEN operator cannot be null");
                    }

                    return criteriaBuilder.between(root.get(entityFieldName),
                            castToEntityFieldType(root.get(entityFieldName).getJavaType(), firstValue).toString(),
                            castToEntityFieldType(root.get(entityFieldName).getJavaType(), secondValue).toString()).not();

                };


            default:
                throw new RuntimeException("Operator not supported");
        }
    }


}
