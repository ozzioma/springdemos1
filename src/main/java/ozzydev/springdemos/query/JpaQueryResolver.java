package ozzydev.springdemos.query;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.format.FormatStyle;
import java.time.format.ResolverStyle;
import java.time.temporal.ChronoField;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

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
                return (root, query, criteriaBuilder) ->
                        criteriaBuilder.equal(root.get(entityFieldName),
                                castToEntityFieldType(root.get(entityFieldName).getJavaType(),
                                        firstValue));

            case NE:
            case NOT_EQUAL:
                return (root, query, criteriaBuilder) ->
                        criteriaBuilder.notEqual(root.get(entityFieldName),
                                castToEntityFieldType(root.get(entityFieldName).getJavaType(),
                                        firstValue));


            case GT:
            case GREATER_THAN:
                return (root, query, criteriaBuilder) ->
                        criteriaBuilder.greaterThan(root.get(entityFieldName),
                                castToEntityFieldType(root.get(entityFieldName).getJavaType(),
                                        firstValue).toString());

            case NGT:
            case NOT_GREATER_THAN:
                return (root, query, criteriaBuilder) ->
                        criteriaBuilder.greaterThan(root.get(entityFieldName),
                                castToEntityFieldType(root.get(entityFieldName).getJavaType(),
                                        firstValue).toString()).not();


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


    private static Object castToEntityFieldType(Class fieldType, Object value)
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
            //System.out.println("Long value found->" + value.toString());
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
            //var localDate = (LocalDate) value;

            var strDate = "24-11-1983";
            var df1 = DateTimeFormatter.ofPattern("dd-MM-yyyy");
            var strDate2 = "1983-11-24";
            var date1 = LocalDate.parse(strDate, df1);
            logger.info("date1 value->" + date1);
            logger.info("date1 string value->" + date1.toString());

            var date2 = LocalDate.parse(strDate2, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            logger.info("date2 value->" + date2);
            logger.info("date2 string value->" + date2.toString());


            logger.info("param value->" + value);
            logger.info("LocalDate value found->" + value.toString());
            logger.info("value type name->" + value.getClass().getTypeName());
            logger.info("value simple name->" + value.getClass().getSimpleName());

            DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            DateTimeFormatter format2 = new DateTimeFormatterBuilder().parseCaseInsensitive()
                    .append(DateTimeFormatter.ofPattern("yyy-MMM-dd")).toFormatter();
            //DateTimeFormatter format3 = DateTimeFormatter.ofPattern("dd-MM-yyyy");
            var format4 = DateTimeFormatter.ofLocalizedDate(FormatStyle.LONG);
            DateTimeFormatter format5 = DateTimeFormatter.ISO_LOCAL_DATE
                    //.ofPattern("yyyy-MM-dd", Locale.US)
                    //.ofPattern("yyyy-MM-dd")
                    .withResolverStyle(ResolverStyle.SMART);

            logger.info("LocalDate formatted value 1->" + LocalDate.parse(value.toString(), format5));
            //logger.info("LocalDate formatted value 2->" + LocalDate.parse(value.toString(), formatter));

            logger.info("date parse successful");

            var dateValue = LocalDate.parse(value.toString(), format5);
            logger.info("final LocalDate parsed value->" + dateValue);

            //ZoneId defaultZoneId = ZoneId.systemDefault();
            //Date jpaDate = Date.from(dateValue.atStartOfDay(defaultZoneId).toInstant());

            return dateValue;
            //return jpaDate;
        }
        else if (fieldType.isAssignableFrom(LocalDateTime.class))
        {
            DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
            return LocalDateTime.parse(value.toString(), format);
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

    private static Object castToEntityFieldType(Class fieldType, List<Object> values)
    {
        List<Object> lists = new ArrayList<>();
        for (Object value : values)
        {
            lists.add(castToEntityFieldType(fieldType, value));
        }
        return lists;
    }
}
