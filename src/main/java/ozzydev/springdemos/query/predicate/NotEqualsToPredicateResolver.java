package ozzydev.springdemos.query.predicate;

import ozzydev.springdemos.query.QueryFilter;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.time.*;

import static ozzydev.springdemos.query.ValueToPathConverter.*;

public class NotEqualsToPredicateResolver implements PredicateResolver
{
    @Override
    public <T> Predicate resolve(Root<?> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder, QueryFilter input)
    {

        final var entityFieldName = input.getField();
        final var firstValue = input.getValues().size() > 0 ? input.getValues().get(0) : null;
        final var secondValue = input.getValues().size() > 1 ? input.getValues().get(1) : null;
        final var allValues = input.getValues();


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

}

